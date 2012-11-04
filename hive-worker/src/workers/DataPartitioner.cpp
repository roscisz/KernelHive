#include <iostream>

#include "DataPartitioner.h"

#include "commons/KhUtils.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataPartitioner::KERNEL = "partitionData";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataPartitioner::DataPartitioner(char **argv) : BasicWorker(argv) {
	partsCount = 0;
	totalDataSize = 0;
	outputDataAddress = NULL;
	resultBuffers = NULL;
}

DataPartitioner::~DataPartitioner() {
	DataPartitioner::cleanupResources();
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataPartitioner::getKernelName() {
	return KERNEL;
}

void DataPartitioner::workSpecific() {
	std::cout << ">>> DataPartitioner work BEGIN" << std::endl;
	runAllDownloads(); // Download data and the kernel

	// Wait for the data to be ready
	waitForAllDownloads();

//	std::cout << ">>> data size: " << buffers[dataIdInt]->getSize() << std::endl;
//	buffers[dataIdInt]->logMyFloatData();

	setPercentDone(40);

	totalDataSize = buffers[dataIdInt]->getSize();

	// Allocate local result buffers
	for (int i = 0; i < partsCount; i++) {
		resultBuffers[i]->allocate(outputSize);
	}

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, totalDataSize*sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(OUTPUT_BUFFER, outputSize*partsCount*sizeof(byte), CL_MEM_READ_WRITE);

	// Begin copying data to the device
	OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, 0,
			totalDataSize*sizeof(byte), (void*)buffers[dataIdInt]->getRawData());

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource((char *)buffers[kernelDataIdInt]->getRawData(),
			buffers[kernelDataIdInt]->getSize());
	context->prepareKernel(getKernelName());

	// Wait for data copy to finish
	OpenClEvent* tmp = &dataCopy;
	context->waitForEvents(1, &tmp);
	setPercentDone(60);

	// Set kernel agrguments
	context->setBufferArg(0, INPUT_BUFFER);
	context->setValueArg(1, sizeof(unsigned int), (void*)&totalDataSize);
	context->setValueArg(2, sizeof(unsigned int), (void*)&partsCount);
	context->setBufferArg(3, OUTPUT_BUFFER);
	context->setValueArg(4, sizeof(unsigned int), (void*)&outputSize);

	// Execute the kernel
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);
	setPercentDone(80);

	// Copy the result:
	size_t readOffset = 0;
	OpenClEvent** copyEvents = new OpenClEvent*[partsCount];
	for (int i = 0; i < partsCount; i++) {
		OpenClEvent dataCopy  = context->enqueueRead(OUTPUT_BUFFER, readOffset,
				outputSize*sizeof(byte), (void*)resultBuffers[i]->getRawData());
		copyEvents[i] = &dataCopy;
		uploaders.push_back(new DataUploader(outputDataAddress, resultBuffers[i]));
		readOffset += outputSize;
	}
	context->waitForEvents(partsCount, copyEvents);
	setPercentDone(90);

	runAllUploads();
	waitForAllUploads();
	setPercentDone(100);
}

void DataPartitioner::initSpecific(char *const argv[]) {
	std::cout << ">>> DataPartitioner init BEGIN" << std::endl;
	// TODO For partitioner only:
	nextParam(argv);
	std::cout << ">>> param skip" << std::endl;
	inputDataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	std::cout << ">>> input address ready" << std::endl;
	dataId = nextParam(argv);
	dataIdInt = KhUtils::atoi(dataId.c_str());
	std::cout << ">>> dataId ready" << std::endl;

	std::cout << ">>> dataId: " << dataId << std::endl;
	std::cout << ">>> dataIdInt: " << dataIdInt << std::endl;

	buffers[dataIdInt] = new SynchronizedBuffer();

	partsCount = KhUtils::atoi(nextParam(argv));
	std::cout << ">>> partsCount ready" << std::endl;
	resultBuffers = new SynchronizedBuffer*[partsCount];
	for (int i = 0; i < partsCount; i++) {
		resultBuffers[i] = new SynchronizedBuffer();
	}
	outputDataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	std::cout << ">>> output address ready" << std::endl;

	downloaders[dataIdInt] = new DataDownloader(inputDataAddress,
			dataId.c_str(), buffers[dataIdInt]);

	downloaders[kernelDataIdInt] = new DataDownloader(kernelAddress,
			kernelDataId.c_str(), buffers[kernelDataIdInt]);
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataPartitioner::cleanupResources() {
	delete outputDataAddress;
	if (resultBuffers != NULL) {
		for (int i = 0; i < partsCount; i++) {
			if (resultBuffers[i] != NULL) {
				delete resultBuffers[i];
			}
		}
		delete [] resultBuffers;
	}
}

} /* namespace KernelHive */
