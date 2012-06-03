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
	inBuffer = "input";
	outBuffer = "output";
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
	threadManager->waitForThread(dataDownloader);
	threadManager->waitForThread(kernelDownloader);

	size_t size = buffer->getSize();

	resultBuffer->allocate(size); // Allocate local result buffer

	// Allocate input and output buffers on the device
	context->createBuffer(inBuffer, size*sizeof(byte), CL_MEM_READ_ONLY);
	context->createBuffer(outBuffer, size*sizeof(byte), CL_MEM_WRITE_ONLY);

	// Begin copying data to the device
	OpenClEvent dataCopy = context->enqueueWrite(inBuffer, 0,
			size*sizeof(byte), (void*)buffer->getRawData());

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource(kernelBuffer->getRawData(),
			kernelBuffer->getSize());
	context->prepareKernel(KERNEL_NAME);

	// Wait for data copy to finish
	context->waitForEvents(1, &dataCopy);

	// Set kernel agrguments
	cl_mem clBuffer = context->getRawBuffer(inBuffer);
	context->setKernelArgument(0, sizeof(cl_mem), (void*)&clBuffer);
	context->setKernelArgument(1, sizeof(unsigned int), (void*)&size);
	clBuffer = context->getRawBuffer(outBuffer);
	context->setKernelArgument(2, sizeof(cl_mem), (void*)&clBuffer);

	// Execute the kernel
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);

	// Copy the result:
	context->read(outBuffer, 0, size*sizeof(byte), (void*)resultBuffer->getRawData());

	// Upload data to repository
	DataUploader* uploader = new DataUploader(dataAddress, resultBuffer);
	threadManager->runThread(uploader);
	threadManager->waitForThread(uploader);
	delete uploader;
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

const char* DataProcessor::KERNEL_NAME = "processData";

void DataProcessor::init(char *const argv[]) {
	// TODO Implement parameters existence check..

	dataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	buffer = new SynchronizedBuffer();
	dataDownloader = new DataDownloader(dataAddress, buffer);

	kernelAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	kernelBuffer = new SynchronizedBuffer();
	kernelDownloader = new DataDownloader(kernelAddress, kernelBuffer);

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
}

} /* namespace KernelHive */
