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
	// TODO Implement processing logic..
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

} /* namespace KernelHive */
