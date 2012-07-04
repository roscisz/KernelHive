#include <string>
#include <iostream>

#include "commons/Logger.h"
#include "commons/OpenClEvent.h"
#include "threading/ThreadManager.h"
#include "../communication/DataUploader.h"
#include "DataProcessor.h"
#include "commons/KhUtils.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataProcessor::KERNEL = "processData";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataProcessor::DataProcessor(char **argv) : BasicWorker(argv) {
	resultBuffer = NULL;
}

DataProcessor::~DataProcessor() {
	DataProcessor::cleanupResources();
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataProcessor::getKernelName() {
	return KERNEL;
}

void DataProcessor::initSpecific(char *const argv[]) {
	dataId = nextParam(argv);
	dataIdInt = KhUtils::atoi(dataId.c_str());

	buffers[dataIdInt] = new SynchronizedBuffer();
	resultBuffer = new SynchronizedBuffer();

	downloaders[dataIdInt] = new DataDownloader(dataAddress,
			dataId.c_str(), buffers[dataIdInt]);
	downloaders[kernelDataIdInt] = new DataDownloader(kernelAddress,
			kernelDataId.c_str(), buffers[kernelDataIdInt]);

}

void DataProcessor::workSpecific() {
	runAllDownloads(); // Download data and the kernel

	// Wait for the data to be ready
	waitForAllDownloads();
	setPercentDone(40);

	size_t size = buffers[dataIdInt]->getSize();

	resultBuffer->allocate(size); // Allocate local result buffer

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, size*sizeof(byte), CL_MEM_READ_ONLY);
	context->createBuffer(OUTPUT_BUFFER, size*sizeof(byte), CL_MEM_WRITE_ONLY);

	// Begin copying data to the device
	OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, 0,
			size*sizeof(byte), (void*)buffers[dataIdInt]->getRawData());

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource(buffers[kernelDataIdInt]->getRawData(),
			buffers[kernelDataIdInt]->getSize());
	context->prepareKernel(getKernelName());

	// Wait for data copy to finish
	OpenClEvent* tmp = &dataCopy;
	context->waitForEvents(1, &tmp);
	setPercentDone(60);

	// Set kernel agrguments
	context->setBufferArg(0, INPUT_BUFFER);
	context->setValueArg(1, sizeof(unsigned int), (void*)&size);
	context->setBufferArg(2, OUTPUT_BUFFER);

	// Execute the kernel
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);
	setPercentDone(80);

	// Copy the result:
	context->read(OUTPUT_BUFFER, 0, size*sizeof(byte), (void*)resultBuffer->getRawData());
	setPercentDone(90);

	// Upload data to repository
	DataUploader* uploader = new DataUploader(dataAddress, resultBuffer);
	threadManager->runThread(uploader);
	threadManager->waitForThread(uploader);
	setPercentDone(100);
	delete uploader;
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

void DataProcessor::cleanupResources() {
	if (resultBuffer != NULL) {
		delete resultBuffer;
	}
}

} /* namespace KernelHive */
