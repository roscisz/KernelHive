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
// FIXME: mongo hack, sometimes needed
//#define MONGO_BOOST_TIME_UTC_HACK

#include "DataDownloaderGridFs.h"
#include "mongo/client/dbclient.h"
#include "mongo/client/gridfs.h"
#include "commons/KhUtils.h"

namespace KernelHive {

DataDownloaderGridFs::DataDownloaderGridFs(NetworkAddress *serverAddress, const char* dataId, SynchronizedBuffer* buffer)
	: IDataDownloader(serverAddress, dataId, buffer) {
	this->dataId = dataId;
	this->serverAddress = serverAddress;
}

void DataDownloaderGridFs::run() {

	mongo::HostAndPort hostAndPort(this->serverAddress->host, this->serverAddress->port);
  
	std::string errmsg;
	mongo::DBClientConnection connection;
	connection.connect(hostAndPort, errmsg);

	connection.auth("admin", "hive-dataserver", "hive-dataserver", errmsg);
	mongo::GridFS database = mongo::GridFS(connection, "hive-dataserver");
	
	mongo::GridFile gFile=database.findFile(std::string(dataId));
	
	std::stringstream ss;
	gFile.write(ss);
	
	this->buffer->allocate(gFile.getContentLength());
	buffer->append((byte *)ss.str().c_str(), gFile.getContentLength() * sizeof(char));
}

void DataDownloaderGridFs::pleaseStop() {

}

DataDownloaderGridFs::~DataDownloaderGridFs() {
	// TODO Auto-generated destructor stub
}

} /* namespace KernelHive */
