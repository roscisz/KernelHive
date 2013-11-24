/*
 * UDPClient.h
 *
 *  Created on: 17-04-2012
 *      Author: roy
 */

#ifndef UDPCLIENT_H_
#define UDPCLIENT_H_

#include "NetworkEndpoint.h"
#include "NetworkAddress.h"

namespace KernelHive {

class UDPClient : public NetworkEndpoint {
public:
	UDPClient(NetworkAddress *serverAddress);
	void sendMessage(char* message);
	void sendMessage(char* message, size_t length);
	virtual ~UDPClient();
};

}

#endif /* UDPCLIENT_H_ */
