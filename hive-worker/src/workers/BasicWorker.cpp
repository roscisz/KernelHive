#include "BasicWorker.h"

#include "commons/OpenClHost.h"
#include "commons/KhUtils.h"
#include "commons/KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

BasicWorker::BasicWorker(char **argv) : Worker(argv) {
	dataAddress = NULL;
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
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void BasicWorker::init(char *const argv[]) {
	dataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	kernelAddress = new NetworkAddress(nextParam(argv), nextParam(argv));

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

	kernelDataId = nextParam(argv);
	kernelBuffer = new SynchronizedBuffer();

	setPercentDone(0);

	initSpecific(argv);
}

void BasicWorker::deallocateResources() {
	if (dataAddress != NULL) {
		delete dataAddress;
	}
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
}

} /* namespace KernelHive */
