#include <string>

#include "threading/ThreadManager.h"
#include "DownloaderThread.h"
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
	DownloaderThread* thread = new DownloaderThread(dataAddress, buffer);
	threadManager->runThread(thread);
	threadManager->waitForThreads();
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataProcessor::init(char *const argv[]) {
	// TODO Implement parameters existence check..
	dataAddress = new NetworkAddress(argv[1], argv[2]);
	buffer = new SynchronizedBuffer();
}

} /* namespace KernelHive */
