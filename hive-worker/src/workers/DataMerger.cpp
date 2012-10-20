#include "DataMerger.h"

#include "commons/KhUtils.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataMerger::KERNEL = "mergeData";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataMerger::DataMerger(char **argv) : BasicWorker(argv) {
	datasCount = 0;
	totalDataSize = 0;
	inputDataAddresses = NULL;
	outputDataAddress = NULL;
	dataIds = NULL;
	dataIdsInt = NULL;
	resultBuffer = NULL;
}

DataMerger::~DataMerger() {
	DataMerger::cleanupResources();
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataMerger::getKernelName() {
	return KERNEL;
}

void DataMerger::workSpecific() {
	runAllDownloads(); // Download data and the kernel

	setPercentDone(20);

	// Wait for the data to be ready
	waitForAllDownloads();
	setPercentDone(40);

	for (int i = 0; i < datasCount; i++) {
		totalDataSize += buffers[dataIdsInt[i]]->getSize();
	}

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, totalDataSize*sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(OUTPUT_BUFFER, outputSize*sizeof(byte), CL_MEM_READ_WRITE);

	// Begin copying data to the device
	size_t copyOffset = 0;
	OpenClEvent** copyEvents = new OpenClEvent*[datasCount];
	for (int i = 0; i < datasCount; i++) {
		OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, copyOffset,
				buffers[dataIdsInt[i]]->getSize()*sizeof(byte), (void*)buffers[dataIdsInt[i]]->getRawData());
		copyEvents[i] = &dataCopy;
		copyOffset += buffers[dataIdsInt[i]]->getSize();
	}

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource((char *)buffers[kernelDataIdInt]->getRawData(),
			buffers[kernelDataIdInt]->getSize());
	context->prepareKernel(getKernelName());

	// Wait for copy to finish.
	context->waitForEvents(datasCount, copyEvents);
	setPercentDone(60);

	// Set kernel agrguments
	context->setBufferArg(0, INPUT_BUFFER);
	context->setValueArg(1, sizeof(unsigned int), (void*)&totalDataSize);
	context->setValueArg(2, sizeof(unsigned int), (void*)&datasCount);
	context->setBufferArg(3, OUTPUT_BUFFER);
	context->setValueArg(4, sizeof(unsigned int), (void*)&outputSize);

	// Execute the kernel
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);
	setPercentDone(80);

	// Copy the result:
	context->read(OUTPUT_BUFFER, 0, outputSize*sizeof(byte), (void*)resultBuffer->getRawData());
	setPercentDone(90);

	// Upload data to repository
	uploaders.push_back(new DataUploader(outputDataAddress, resultBuffer));
	runAllUploads();
	waitForAllUploads();
	setPercentDone(100);
}

void DataMerger::initSpecific(char *const argv[]) {
	datasCount = KhUtils::atoi(nextParam(argv));
	inputDataAddresses = new NetworkAddress*[datasCount];
	dataIds = new std::string*[datasCount];
	dataIdsInt = new int[datasCount];
	for (int i = 0; i < datasCount; i++) {
		inputDataAddresses[i] = new NetworkAddress(nextParam(argv), nextParam(argv));
		dataIds[i] = new std::string(nextParam(argv));
		dataIdsInt[i] = KhUtils::atoi(dataIds[i]->c_str());
		buffers[dataIdsInt[i]] = new SynchronizedBuffer();
		downloaders[dataIdsInt[i]] = new DataDownloader(inputDataAddresses[i],
			dataIds[i]->c_str(), buffers[dataIdsInt[i]]);
	}
	downloaders[kernelDataIdInt] = new DataDownloader(kernelAddress,
			kernelDataId.c_str(), buffers[kernelDataIdInt]);

	// TODO For merger only - skip the number of outputs:
	nextParam(argv);
	outputDataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	resultBuffer = new SynchronizedBuffer(outputSize);
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataMerger::cleanupResources() {
	if (dataIds != NULL) {
		for (int i = 0; i < datasCount; i++) {
			delete dataIds[i];
		}
		delete [] dataIds;
	}
	if (inputDataAddresses != NULL) {
		for (int i = 0; i < datasCount; i++) {
			delete inputDataAddresses[i];
		}
		delete [] inputDataAddresses;
	}
	if (outputDataAddress != NULL) {
		delete outputDataAddress;
	}
	if (dataIdsInt != NULL) {
		delete [] dataIdsInt;
	}
	if (resultBuffer != NULL) {
		delete resultBuffer;
	}
}

} /* namespace KernelHive */
