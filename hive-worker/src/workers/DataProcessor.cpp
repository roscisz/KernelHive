#include <string>

#include "commons/Logger.h"
#include "threading/ThreadManager.h"
#include "DataDownloader.h"
#include "DataProcessor.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataProcessor::DataProcessor(NetworkAddress * clusterAddress) : Worker(clusterAddress) {
	dataAddress = NULL;
	buffer = NULL;
}

DataProcessor::~DataProcessor() {
	if (dataAddress != NULL) {
		delete dataAddress;
	}
	if (buffer != NULL) {
		delete buffer;
	}
}

void DataProcessor::work(char *const argv[]) {
	init(argv);
	dataDownloader = new DataDownloader(dataAddress, buffer);
	dataDownloader->start();
	threadManager->waitForThreads();
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataProcessor::init(char *const argv[]) {
	// TODO Implement parameters existence check..
	dataAddress = new NetworkAddress(argv[0], argv[1]);
	buffer = new SynchronizedBuffer();
}

} /* namespace KernelHive */
