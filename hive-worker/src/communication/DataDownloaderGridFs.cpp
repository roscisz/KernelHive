/*
 * DataDownloaderGridFs.cpp
 *
 *  Created on: 18-01-2014
 *      Author: roy
 */
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

	mongo::DBClientConnection connection;
	//mongo::HostAndPort address(this->serverAddress->host, this->serverAddress->port);
	connection.connect(this->serverAddress->host);
	connection.auth(BSON("user" << "hive-dataserver" <<
				"userSource" << "hive-dataserver" <<
				"pwd" << "hive-dataserver" <<
				"mechanism" << "MONGODB-CR"));
	printf("after connect\n");
	mongo::GridFS database = mongo::GridFS(connection, "hive-dataserver");
//	printf("chunk size: %d\n", database.getChunkSize());
	
	mongo::BSONObj query = BSON("_id"<< KhUtils::atoi(dataId));
	mongo::GridFile gFile=database.findFile(query);
	printf("test\n");
	
	printf("robote %s\n", gFile.getMD5().c_str());
	printf("chunk size %d\n", gFile.getChunkSize());
//	gFile.write(std::cout);
	printf("length %d\n", gFile.getContentLength());
	
	std::stringstream ss;
	gFile.write(ss);
	
	this->buffer->allocate(gFile.getContentLength());
	buffer->append((byte *)ss.str().c_str(), gFile.getContentLength() * sizeof(char));
	
//	printf("%d %s\n", buffer->getSize(), buffer->getRawData());
}

void DataDownloaderGridFs::pleaseStop() {

}

DataDownloaderGridFs::~DataDownloaderGridFs() {
	// TODO Auto-generated destructor stub
}

} /* namespace KernelHive */
