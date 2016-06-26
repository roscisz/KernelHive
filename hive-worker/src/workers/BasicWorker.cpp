/**
 * Copyright (c) 2016 Gdansk University of Technology
 * Copyright (c) 2016 Adrian Boguszewski
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

#include <commons/KhUtils.h>
#include "BasicWorker.h"
#include "../communication/DataDownloaderGridFs.h"
#include "../communication/DataUploaderGridFs.h"

namespace KernelHive {

BasicWorker::BasicWorker(char **argv): Worker(argv) {
    inputDataAddresses = NULL;
    outputDataAddress = NULL;
    resultBuffers = NULL;
    resultsCount = 0;
}

BasicWorker::~BasicWorker() {
    if (dataIds != NULL) {
        delete [] dataIds;
    }
    if (inputDataAddresses != NULL) {
        for (int i = 0; i < datasCount; i++) {
            delete inputDataAddresses[i];
        }
        delete [] inputDataAddresses;
    }
    if (outputDataAddress != NULL) {
        delete outputDataAddress;
    }
    if (resultBuffers != NULL) {
        for (int i = 0; i < resultsCount; i++) {
            if (resultBuffers[i] != NULL) {
                delete resultBuffers[i];
            }
        }
        delete [] resultBuffers;
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

void BasicWorker::init(char *const *argv) {
    datasCount = KhUtils::atoi(nextParam(argv));
    inputDataAddresses = new NetworkAddress*[datasCount];
    dataIds = new std::string[datasCount];
    for (int i = 0; i < datasCount; i++) {
        inputDataAddresses[i] = new NetworkAddress(nextParam(argv), nextParam(argv));
        dataIds[i] = std::string(nextParam(argv));
        buffers[dataIds[i]] = new SynchronizedBuffer();
        downloaders[dataIds[i]] = new DataDownloaderGridFs(inputDataAddresses[i], dataIds[i].c_str(), buffers[dataIds[i]]);
    }

    resultsCount = KhUtils::atoi(nextParam(argv));
    outputDataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
    resultBuffers = new SynchronizedBuffer*[resultsCount];
    for (int i = 0; i < resultsCount; i++) {
        resultBuffers[i] = new SynchronizedBuffer();
        uploaders.push_back(new DataUploaderGridFs(outputDataAddress, &resultBuffers[i], 1));
    }
}

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

}