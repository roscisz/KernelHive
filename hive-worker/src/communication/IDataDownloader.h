/*
 * IDataUploader.h
 *
 *  Created on: 18-01-2014
 *      Author: roy
 */

#ifndef I_DATA_DOWNLOADER_H_
#define I_DATA_DOWNLOADER_H_

#include "threading/Thread.h"
#include "network/NetworkAddress.h"
#include "threading/SynchronizedBuffer.h"

namespace KernelHive {

class IDataDownloader : public Thread {
public:
	IDataDownloader(NetworkAddress *serverAddress, const char* dataId, SynchronizedBuffer* buffer) {
	  this->buffer = buffer;
	}
protected:
  	/** A pointer to the buffer in which the downloaded data should be stored. */
	SynchronizedBuffer* buffer;
};

}

#endif /* I_DATA_DOWNLOADER */
