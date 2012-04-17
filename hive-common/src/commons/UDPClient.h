/*
 * UDPClient.h
 *
 *  Created on: 17-04-2012
 *      Author: roy
 */

#ifndef UDPCLIENT_H_
#define UDPCLIENT_H_

class UDPClient {
public:
	UDPClient(const char* host, int port);
	virtual ~UDPClient();
};

#endif /* UDPCLIENT_H_ */
