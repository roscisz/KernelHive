/*
 * DataDownloaderGridFs.h
 *
 *  Created on: 18-01-2014
 *      Author: roy
 */

#ifndef DATADOWNLOADERGRIDFS_H_
#define DATADOWNLOADERGRIDFS_H_

#include "IDataDownloader.h"

namespace KernelHive {

class DataDownloaderGridFs: public KernelHive::IDataDownloader {
public:
	DataDownloaderGridFs(NetworkAddress *serverAddress, const char* dataId, SynchronizedBuffer* buffer);
	void run();
	void pleaseStop();
	virtual ~DataDownloaderGridFs();
private:
	const char* dataId;
	NetworkAddress *serverAddress;	
};

} /* namespace KernelHive */

#endif /* DATADOWNLOADERGRIDFS_H_ */
