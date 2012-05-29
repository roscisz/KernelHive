#include <string>
#include <iostream>

#include "commons/Logger.h"
#include "commons/KhUtils.h"
#include "commons/KernelHiveException.h"
#include "threading/ThreadManager.h"
#include "DataDownloader.h"
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

	threadManager->runThread(dataDownloader);
	threadManager->runThread(kernelDownloader);

	threadManager->waitForThread(dataDownloader);
	threadManager->waitForThread(kernelDownloader);
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataProcessor::init(char *const argv[]) {
	// TODO Implement parameters existence check..
	dataAddress = new NetworkAddress(argv[0], argv[1]);
	buffer = new SynchronizedBuffer();
	dataDownloader = new DataDownloader(dataAddress, buffer);
	kernelAddress = new NetworkAddress(argv[2], argv[3]);
	kernelBuffer = new SynchronizedBuffer();
	kernelDownloader = new DataDownloader(kernelAddress, kernelBuffer);
	kernelId = argv[4];
	numberOfDimensions = KhUtils::atoi(argv[5]);
	if (numberOfDimensions <= 0) {
		throw KernelHiveException("Number of dimensions must a positive integer!");
	}
	dimensionOffsets = new size_t[numberOfDimensions];
	globalSizes = new size_t[numberOfDimensions];
	localSizes = new size_t[numberOfDimensions];
	// TODO Implement size amounts parsing...
	dimensionOffsets[0] = KhUtils::atoi(argv[6]);
	globalSizes[0] = KhUtils::atoi(argv[7]);
	localSizes[0] = KhUtils::atoi(argv[8]);
}

} /* namespace KernelHive */
