/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
#include "mongo/client/dbclient.h"
#include "mongo/client/gridfs.h"
#include "DataUploaderGridFs.h"
#include "commons/KhUtils.h"

namespace KernelHive {

DataUploaderGridFs::DataUploaderGridFs(NetworkAddress* address, SynchronizedBuffer** buffers, int partsCount) :
			IDataUploader(address, buffers, partsCount) {
	this->serverAddress = address; 
	this->partsCount = partsCount;
	baseOutputId = 1000000;
}

void DataUploaderGridFs::run() {

	mongo::HostAndPort hostAndPort(this->serverAddress->host, this->serverAddress->port);

	mongo::DBClientConnection connection;
    connection.connect(hostAndPort);

    std::string errmsg;
	connection.auth("admin", "hive-dataserver", "hive-dataserver", errmsg);
	mongo::GridFS database = mongo::GridFS(connection, "hive-dataserver");

	std::stringstream ss;
	for (int i = 0; i < partsCount; i++) {
		ss.str("");
		ss << (baseOutputId + i);
		database.storeFile((const char *) buffers[i]->getRawData(), buffers[i]->getSize(), ss.str());
	}
}

void DataUploaderGridFs::pleaseStop() {
}

void DataUploaderGridFs::getDataURL(std::string *param) {
	std::stringstream ret;

	for (int i = 0; i != this->partsCount; i++) {
		ret << serverAddress->host;
		ret << " ";
		ret << KhUtils::itoa(serverAddress->port);
		ret << " ";
		ret << (baseOutputId + i);
		ret << " ";
	}

	param->append(ret.str());
}

DataUploaderGridFs::~DataUploaderGridFs() {
	// TODO
}

}
