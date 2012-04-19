/*
 * UDPClient.h
 *
 *  Created on: 17-04-2012
 *      Author: roy
 */

#ifndef UDPCLIENT_H_
#define UDPCLIENT_H_

#include "NetworkClient.h"

namespace KernelHive {

class UDPClient : public NetworkClient {
public:
	UDPClient(const char* host, int port);
	void sendMessage(char* message);
	virtual ~UDPClient();
};

}

#endif /* UDPCLIENT_H_ */
