/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
#include "OpenCLWorker.h"

#include <iostream>
#include <algorithm>

#include "commons/OpenClHost.h"
#include "commons/KhUtils.h"
#include "commons/KernelHiveException.h"
#include "../communication/DataDownloaderGridFs.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* OpenCLWorker::INPUT_BUFFER = "inputBuffer";
const char* OpenCLWorker::PREVIEW_BUFFER = "previewBuffer";
const char* OpenCLWorker::OUTPUT_BUFFER = "outputBuffer";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

OpenCLWorker::OpenCLWorker(char **argv) : BasicWorker(argv) {
	kernelAddress = NULL;
	device = NULL;
	numberOfDimensions = 0;
	dimensionOffsets = NULL;
	globalSizes = NULL;
	localSizes = NULL;
}

OpenCLWorker::~OpenCLWorker() {
	deallocateResources();
}

void OpenCLWorker::work(char *const argv[]) {
	std::cout << ">>> " << "OpenCLWorker::work " << std::endl;
	init(argv);
	workSpecific();
	std::string uploadStrings = "";
	getAllUploadIDStrings(&uploadStrings);
	const char* rawString = uploadStrings.c_str();
	reportOver(rawString);
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void OpenCLWorker::init(char *const argv[]) {
	BasicWorker::init(argv);
	deviceId = nextParam(argv);

	std::cout << deviceId;

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
	buffers[kernelDataId] = new SynchronizedBuffer();
	downloaders[kernelDataId] = new DataDownloaderGridFs(kernelAddress, kernelDataId.c_str(), buffers[kernelDataId]);
	setPercentDone(0);

	initSpecific(argv);
}

void OpenCLWorker::deallocateResources() {
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
	delete context;
}

} /* namespace KernelHive */
