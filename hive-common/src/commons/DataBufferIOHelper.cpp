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

#include <fstream>
#include <boost/filesystem.hpp>
#include "DataBufferIOHelper.h"
#include "KernelHiveException.h"

namespace KernelHive {

using namespace boost::filesystem;

std::string DataBufferIOHelper::writeDataBufferToTmpFile(std::string id, SynchronizedBuffer* buffer) {
    // path to /tmp/filename
    path p = temp_directory_path();
    p /= id;

    std::fstream tmpFile;
    tmpFile.open(p.string().c_str(), std::ios::out | std::ios::binary);

    if (!tmpFile.is_open()) {
        throw new KernelHiveException("Cannot open tmp file to write");
    }
    tmpFile.write((const char *) buffer->getRawData(), buffer->getSize());
    tmpFile.close();

    return p.string();
}

void DataBufferIOHelper::readDataBufferFromTmpFile(const std::string &path, SynchronizedBuffer* buffer) {
    std::fstream file;
    file.open(path.c_str(), std::ios::in | std::ios::binary | std::ios::ate);

    if (!file.is_open()) {
        throw new KernelHiveException("Cannot open tmp file to read");
    }
    buffer->allocate((size_t) file.tellg());
    file.seekg(0);
    file.read((char *) buffer->getRawData(), buffer->getSize());
    file.close();
}

}

