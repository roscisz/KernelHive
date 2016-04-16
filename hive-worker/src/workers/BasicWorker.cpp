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
#include "BasicWorker.h"

#include <iostream>
#include <algorithm>

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
	std::cout << ">>> " << "BasicWorker::work " << std::endl;
	init(argv);
	workSpecific();
	std::string uploadStrings = "";
	getAllUploadIDStrings(&uploadStrings);
	const char* rawString = uploadStrings.c_str();
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

void BasicWorker::runAllUploadsSync() {
	for(UploaderList::iterator it = uploaders.begin(); it != uploaders.end(); it++ ) {
		if(*it != NULL) {
			threadManager->runThread(*it);
			threadManager->waitForThread(*it);
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
	for(UploaderList::iterator it = uploaders.begin(); it != uploaders.end(); it++) {
		if(*it != NULL) {
			std::string *tmp = new std::string("");
			(*it)->getDataURL(tmp);
			param->append(*tmp);
			param->append(" ");
			delete tmp;
		}
	}
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void BasicWorker::init(char *const argv[]) {
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
	delete context;
}

} /* namespace KernelHive */
