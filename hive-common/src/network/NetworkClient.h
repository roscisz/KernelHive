/*
 * NetworkClient.h
 *
 *  Created on: 17-04-2012
 *      Author: roy
 */

#ifndef NETWORKCLIENT_H_
#define NETWORKCLIENT_H_

#include <netinet/in.h>
#include <netdb.h>
#include "NetworkAddress.h"

namespace KernelHive {

class NetworkClient {
protected:
	struct sockaddr_in serverAddress;
	int sockfd;

	struct sockaddr_in prepareServerAddress(NetworkAddress *serverAddress);
	int openSocket(int type);
public:
	NetworkClient(NetworkAddress *serverAddress);
	virtual ~NetworkClient();
};

}

#endif /* NETWORKCLIENT_H_ */
