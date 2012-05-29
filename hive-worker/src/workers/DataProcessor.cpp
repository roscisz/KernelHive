#include <string>
#include <iostream>

#include "commons/Logger.h"
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
}

} /* namespace KernelHive */
