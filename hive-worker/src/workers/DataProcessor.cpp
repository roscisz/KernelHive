#include <string>
#include <iostream>

#include "commons/Logger.h"
#include "commons/KhUtils.h"
#include "commons/KernelHiveException.h"
#include "commons/OpenClHost.h"
#include "commons/OpenClEvent.h"
#include "threading/ThreadManager.h"
#include "DataDownloader.h"
#include "DataUploader.h"
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

DataProcessor::DataProcessor(NetworkAddress * clusterAddress) : Worker(clusterAddress) {
	dataDownloader = NULL;
	kernelDownloader = NULL;
	dataAddress = NULL;
	kernelAddress = NULL;
	buffer = NULL;
	kernelBuffer = NULL;
	resultBuffer = NULL;
	device = NULL;
	numberOfDimensions = 0;
	dimensionOffsets = NULL;
	globalSizes = NULL;
	localSizes = NULL;
}

DataProcessor::~DataProcessor() {
	if (dataAddress != NULL) {
		delete dataAddress;
	}
	if (kernelAddress != NULL) {
			delete kernelAddress;
		}
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
	if (dimensionOffsets != NULL) {
		delete [] dimensionOffsets;
	}
	if (globalSizes != NULL) {
		delete [] globalSizes;
	}
	if (localSizes != NULL) {
		delete [] localSizes;
	}
}

void DataProcessor::work(char *const argv[]) {
	init(argv);

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
// 							Private Members									 //
// ========================================================================= //

void DataProcessor::init(char *const argv[]) {
	// TODO Implement parameters existence check..

	dataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	buffer = new SynchronizedBuffer();
	dataDownloader = new DataDownloader(dataAddress, nextParam(argv), buffer);

	kernelAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	kernelBuffer = new SynchronizedBuffer();
	kernelDownloader = new DataDownloader(kernelAddress, nextParam(argv), kernelBuffer);

	resultBuffer = new SynchronizedBuffer();

	deviceId = nextParam(argv);
	device = OpenClHost::getInstance()->lookupDevice(deviceId);
	if (device == NULL) {
		throw KernelHiveException("Device not found!");
	}

	context = new ExecutionContext(*device);

	numberOfDimensions = KhUtils::atoi(nextParam(argv));
	if (numberOfDimensions <= 0) {
		throw KernelHiveException("Number of dimensions must a positive integer!");
	}
	dimensionOffsets = new size_t[numberOfDimensions];
	globalSizes = new size_t[numberOfDimensions];
	localSizes = new size_t[numberOfDimensions];
	for (int i = 0; i < numberOfDimensions; i++) {
		dimensionOffsets[i] = KhUtils::atoi(nextParam(argv));
	}
	for (int i = 0; i < numberOfDimensions; i++) {
		globalSizes[i] = KhUtils::atoi(nextParam(argv));
	}
	for (int i = 0; i < numberOfDimensions; i++) {
		localSizes[i] = KhUtils::atoi(nextParam(argv));
	}

	setPercentDone(0);
}

} /* namespace KernelHive */
