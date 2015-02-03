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

namespace KernelHive {

DataUploaderGridFs::DataUploaderGridFs(NetworkAddress* address, SynchronizedBuffer** buffers, int partsCount) :
			IDataUploader(address, buffers, partsCount) {
	this->serverAddress = address; 

}

void DataUploaderGridFs::run() {

	mongo::HostAndPort hostAndPort(this->serverAddress->host, this->serverAddress->port);

        mongo::DBClientConnection connection;
        connection.connect(hostAndPort);

        std::string errmsg;
        connection.auth("admin", "hive-dataserver", "hive-dataserver", errmsg);
        mongo::GridFS database = mongo::GridFS(connection, "hive-dataserver");

//        mongo::GridFile gFile=database.findFile(std::string(dataId));
	database.storeFile((const char *) buffers[0]->getRawData(), buffers[0]->getSize(), "tester33");

	// private String costam = " ustaw"+ ';;;;
}

// GetURL() { return costam wyzej; }

void DataUploaderGridFs::pleaseStop() {
}

void DataUploaderGridFs::getDataURL(std::string *param) {
	// TODO
}

DataUploaderGridFs::~DataUploaderGridFs() {
	// TODO
}

}
