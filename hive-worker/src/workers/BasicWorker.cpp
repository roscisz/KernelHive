#include "BasicWorker.h"

#include <iostream>

#include "commons/OpenClHost.h"
#include "commons/KhUtils.h"
#include "commons/KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* BasicWorker::INPUT_BUFFER = "inputBuffer";

const char* BasicWorker::OUTPUT_BUFFER = "outputBuffer";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

BasicWorker::BasicWorker(char **argv) : Worker(argv) {
	kernelAddress = NULL;
	device = NULL;
	numberOfDimensions = 0;
	dimensionOffsets = NULL;
	globalSizes = NULL;
	localSizes = NULL;
}

BasicWorker::~BasicWorker() {
	deallocateResources();
}

void BasicWorker::work(char *const argv[]) {
	init(argv);
	workSpecific();
	std::string uploadStrings;
	getAllUploadIDStrings(&uploadStrings);
	const char* rawString = uploadStrings.c_str();
	std::cout << ">>> OBJECT UPLOAD ID STRING: { " << uploadStrings << " } " << std::endl;
	std::cout << ">>> RAW UPLOAD ID STRING: { " << rawString << " } " << std::endl;
	reportOver(rawString);
}

UploaderList* BasicWorker::getUploaders() {
	return &uploaders;
}

// ========================================================================= //
// 							Protected Members								 //
// ========================================================================= //

void BasicWorker::runAllDownloads() {
	for (DownloaderMap::iterator it = downloaders.begin(); it != downloaders.end(); it++ ) {
		if (it->second != NULL) {
			threadManager->runThread(it->second);
		}
	}
}

void BasicWorker::waitForAllDownloads() {
	for (DownloaderMap::iterator it = downloaders.begin(); it != downloaders.end(); it++ ) {
		if (it->second != NULL) {
			threadManager->waitForThread(it->second);
		}
	}
}

void BasicWorker::runAllUploads() {
	for (UploaderList::iterator it = uploaders.begin(); it != uploaders.end(); it++ ) {
		if (*it != NULL) {
			threadManager->runThread(*it);
		}
	}
}

void BasicWorker::waitForAllUploads() {
	for (UploaderList::iterator it = uploaders.begin(); it != uploaders.end(); it++ ) {
		if (*it != NULL) {
			threadManager->waitForThread(*it);
		}
	}
}

const void BasicWorker::getAllUploadIDStrings(std::string* param) {
	// TODO Debug logging..
	std::cout << ">>> GET UPLOAD ID STRING BEGIN" << std::endl;

	for(UploaderList::iterator it = uploaders.begin(); it != uploaders.end(); it++) {
		if(*it != NULL) {
			std::cout << ">>> WILL APPEND { " << (*it)->getDataURL() << " }" << std::endl;
			param->append((*it)->getDataURL());
			param->append(" ");
		}
	}

	std::cout << ">>> GET UPLOAD ID STRING END" << std::endl;
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void BasicWorker::init(char *const argv[]) {
	deviceId = nextParam(argv);
	device = OpenClHost::getInstance()->lookupDevice(deviceId);
	if (device == NULL) {
		throw KernelHiveException(deviceId);
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

	outputSize = (size_t)KhUtils::atoi(nextParam(argv));

	kernelAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	kernelDataId = nextParam(argv);
	kernelDataIdInt = KhUtils::atoi(kernelDataId.c_str());
	buffers[kernelDataIdInt] = new SynchronizedBuffer();

	setPercentDone(0);

	initSpecific(argv);
}

void BasicWorker::deallocateResources() {
	if (kernelAddress != NULL) {
		delete kernelAddress;
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
	for (DownloaderMap::iterator it = downloaders.begin(); it != downloaders.end(); it++ ) {
		if (it->second != NULL) {
			delete it->second;
		}
	}
	for (DataBufferMap::iterator it = buffers.begin(); it != buffers.end(); it++ ) {
		if (it->second != NULL) {
			delete it->second;
		}
	}
	for (UploaderList::iterator it = uploaders.begin(); it != uploaders.end(); it++ ) {
		if (*it != NULL) {
			delete *it;
		}
	}
}

} /* namespace KernelHive */
