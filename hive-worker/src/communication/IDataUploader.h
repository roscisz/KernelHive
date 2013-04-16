/*
 * IDataUploader.h
 *
 *  Created on: 13-04-2013
 *      Author: roy
 */

#ifndef I_DATA_UPLOADER_H_
#define I_DATA_UPLOADER_H_

#include "network/TCPClient.h"

namespace KernelHive {

class IDataUploader : public TCPClient {
public:
	IDataUploader(NetworkAddress *serverAddress, TCPClientListener *listener) : TCPClient(serverAddress, listener) {};

	virtual void getDataURL(std::string *param) = 0;

};

}

#endif /* I_DATA_UPLOADER */
