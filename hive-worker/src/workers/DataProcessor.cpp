#include <string>
#include <iostream>

#include "commons/Logger.h"
#include "commons/OpenClEvent.h"
#include "threading/ThreadManager.h"
#include "../communication/DataUploader.h"
#include "DataProcessor.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataProcessor::KERNEL = "processData";

const char* DataProcessor::INPUT_BUFFER = "inputBuffer";

const char* DataProcessor::OUTPUT_BUFFER = "outputBuffer";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataProcessor::DataProcessor(char **argv) : BasicWorker(argv) {
	dataDownloader = NULL;
	kernelDownloader = NULL;
	buffer = NULL;
	kernelBuffer = NULL;
	resultBuffer = NULL;
}

DataProcessor::~DataProcessor() {
	DataProcessor::cleanupResources();
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

void DataProcessor::initSpecific(char *const argv[]) {
	dataId = nextParam(argv);

	buffer = new SynchronizedBuffer();
	resultBuffer = new SynchronizedBuffer();

	dataDownloader = new DataDownloader(dataAddress, dataId.c_str(), buffer);
	kernelDownloader = new DataDownloader(kernelAddress, kernelDataId.c_str(), kernelBuffer);

}

void DataProcessor::workSpecific() {
	threadManager->runThread(dataDownloader); // Run data downloading
	threadManager->runThread(kernelDownloader); // Run kernel downloading

	// Wait for the data to be ready
	threadManager->waitForThread(kernelDownloader);
	setPercentDone(20);
	threadManager->waitForThread(dataDownloader);
	setPercentDone(40);

	size_t size = buffer->getSize();

	resultBuffer->allocate(size); // Allocate local result buffer

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, size*sizeof(byte), CL_MEM_READ_ONLY);
	context->createBuffer(OUTPUT_BUFFER, size*sizeof(byte), CL_MEM_WRITE_ONLY);

	// Begin copying data to the device
	OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, 0,
			size*sizeof(byte), (void*)buffer->getRawData());

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource(kernelBuffer->getRawData(),
			kernelBuffer->getSize());
	context->prepareKernel(KERNEL);

	// Wait for data copy to finish
	context->waitForEvents(1, &dataCopy);
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
	if (buffer != NULL) {
		delete buffer;
	}
	if (kernelBuffer != NULL)
	{
		delete kernelBuffer;
	}
	if (resultBuffer != NULL) {
		delete resultBuffer;
	}
}

} /* namespace KernelHive */
