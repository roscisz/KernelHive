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

#ifndef KERNELHIVE_BASICWORKER_H
#define KERNELHIVE_BASICWORKER_H

#include "commons/Worker.h"
#include "../communication/IDataDownloader.h"
#include "../communication/IDataUploader.h"

namespace KernelHive {

typedef std::map<std::string, IDataDownloader*> DownloaderMap;
typedef std::vector<IDataUploader*> UploaderList;
typedef std::map<std::string, SynchronizedBuffer*> DataBufferMap;

class BasicWorker: public Worker {
public:
    BasicWorker(char **argv);
    virtual ~BasicWorker();
    virtual void work(char *const argv[]) = 0;

protected:
    int datasCount;
    NetworkAddress** inputDataAddresses;
    std::string* dataIds;
    DataBufferMap buffers;
    int resultsCount;
    NetworkAddress *outputDataAddress;
    SynchronizedBuffer** resultBuffers;
    DownloaderMap downloaders;
    UploaderList uploaders;
    void runAllDownloads();
    void waitForAllDownloads();
    void runAllUploads();
    void runAllUploadsSync();
    void waitForAllUploads();
    const void getAllUploadIDStrings(std::string* param);
    virtual void init(char* const argv[]);

};

}

#endif //KERNELHIVE_BASICWORKER_H
