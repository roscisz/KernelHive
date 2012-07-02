#include "DataMerger.h"

#include "commons/KhUtils.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataMerger::KERNEL = "mergeData";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataMerger::DataMerger(char **argv) : BasicWorker(argv) {
	datasCount = 0;
	dataIds = NULL;
	dataIdsInt = NULL;
	resultBuffer = NULL;
}

DataMerger::~DataMerger() {
	DataMerger::cleanupResources();
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataMerger::getKernelName() {
	return KERNEL;
}

void DataMerger::workSpecific() {

}

void DataMerger::initSpecific(char *const argv[]) {
	datasCount = KhUtils::atoi(nextParam(argv));
	dataIds = new std::string*[datasCount];
	dataIdsInt = new int[datasCount];
	for (int i = 0; i < datasCount; i++) {
		dataIds[i] = new std::string(nextParam(argv));
		dataIdsInt[i] = KhUtils::atoi(dataIds[i]->c_str());
		buffers[dataIdsInt[i]] = new SynchronizedBuffer();
		downloaders[dataIdsInt[i]] = new DataDownloader(dataAddress,
			dataIds[i]->c_str(), buffers[dataIdsInt[0]]);
	}
	downloaders[kernelDataIdInt] = new DataDownloader(kernelAddress,
			kernelDataId.c_str(), buffers[kernelDataIdInt]);
	resultBuffer = new SynchronizedBuffer();
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataMerger::cleanupResources() {
	if (dataIds != NULL) {
		for (int i = 0; i < datasCount; i++) {
			delete dataIds[i];
		}
		delete [] dataIds;
	}
	if (dataIdsInt != NULL) {
		delete [] dataIdsInt;
	}
	if (resultBuffer != NULL) {
		delete resultBuffer;
	}
}

} /* namespace KernelHive */
