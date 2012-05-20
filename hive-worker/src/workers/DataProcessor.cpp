#include <string>

#include "threading/ThreadManager.h"
#include "DownloaderThread.h"
#include "DataProcessor.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataProcessor::DataProcessor(NetworkAddress * clusterAddress) : Worker(clusterAddress) {
	// TODO Auto-generated constructor stub
}

DataProcessor::~DataProcessor() {
	// TODO Auto-generated destructor stub
}

void DataProcessor::work(char *const argv[]) {
	NetworkAddress* address = new NetworkAddress(argv[1], argv[2]);
	SynchronizedBuffer* buffer = new SynchronizedBuffer();
	DownloaderThread* thread = new DownloaderThread(address, buffer);
	threadManager->runThread(thread);
	threadManager->waitForThreads();
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

} /* namespace KernelHive */
