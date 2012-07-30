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

	// Wait for the data to be ready
	waitForAllDownloads();
	setPercentDone(40);

	for (int i = 0; i < datasCount; i++) {
		totalDataSize += buffers[i]->getSize();
	}

	resultBuffer->allocate(totalDataSize); // Allocate local result buffer

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, totalDataSize*sizeof(byte), CL_MEM_READ_ONLY);
	context->createBuffer(OUTPUT_BUFFER, totalDataSize*sizeof(byte), CL_MEM_WRITE_ONLY);

	// Begin copying data to the device
	size_t copyOffset = 0;
	OpenClEvent** copyEvents = new OpenClEvent*[datasCount];
	for (int i = 0; i < datasCount; i++) {
		OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, copyOffset,
				buffers[i]->getSize()*sizeof(byte), (void*)buffers[i]->getRawData());
		copyEvents[i] = &dataCopy;
		copyOffset += buffers[i]->getSize();
	}

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource(buffers[kernelDataIdInt]->getRawData(),
			buffers[kernelDataIdInt]->getSize());
	context->prepareKernel(getKernelName());

	// Wait for copy to finish.
	context->waitForEvents(datasCount, copyEvents);
	setPercentDone(60);

	// Set kernel agrguments
	context->setBufferArg(0, INPUT_BUFFER);
	context->setValueArg(1, sizeof(unsigned int), (void*)&totalDataSize);
	context->setBufferArg(2, OUTPUT_BUFFER);

	// Execute the kernel
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);
	setPercentDone(80);

	// Copy the result:
	context->read(OUTPUT_BUFFER, 0, totalDataSize*sizeof(byte), (void*)resultBuffer->getRawData());
	setPercentDone(90);

	// Upload data to repository
	/* TODO Conform to new parameters style
	DataUploader* uploader = new DataUploader(dataAddress, resultBuffer);
	threadManager->runThread(uploader);
	threadManager->waitForThread(uploader);
	setPercentDone(100);
	delete uploader;
	*/
}

void DataMerger::initSpecific(char *const argv[]) {
	datasCount = KhUtils::atoi(nextParam(argv));
	dataIds = new std::string*[datasCount];
	dataIdsInt = new int[datasCount];
	for (int i = 0; i < datasCount; i++) {
		dataIds[i] = new std::string(nextParam(argv));
		dataIdsInt[i] = KhUtils::atoi(dataIds[i]->c_str());
		buffers[dataIdsInt[i]] = new SynchronizedBuffer();
		/* TODO Conform to new parameters style
		downloaders[dataIdsInt[i]] = new DataDownloader(dataAddress,
			dataIds[i]->c_str(), buffers[dataIdsInt[0]]);
			*/
	}
	downloaders[kernelDataIdInt] = new DataDownloader(kernelAddress,
			kernelDataId.c_str(), buffers[kernelDataIdInt]);
	resultBuffer = new SynchronizedBuffer();
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
	if (dataIdsInt != NULL) {
		delete [] dataIdsInt;
	}
	if (resultBuffer != NULL) {
		delete resultBuffer;
	}
}

} /* namespace KernelHive */
