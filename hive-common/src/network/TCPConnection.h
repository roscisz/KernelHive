/*
 * TCPConnection.h
 *
 *  Created on: 24-05-2012
 *      Author: roy
 */

#ifndef TCPCONNECTION_H_
#define TCPCONNECTION_H_

#include "../threading/LoopedThread.h"
#include "TCPConnectionListener.h"
#include "TCPMessage.h"
#include "../commons/byte.h"

#define MAX_MESSAGE_BYTES 1492

namespace KernelHive {

class TCPConnection : public LoopedThread {
public:
	TCPConnection(int sockfd, TCPConnectionListener *listener);
	void executeLoopCycle();
	//void sendMessage(byte *msg); // FIXME: Should be protected?
	void sendMessage(TCPMessage *message);
	void disconnect();
	virtual ~TCPConnection();
private:
	int sockfd;
	TCPMessage *readMessage();
	TCPConnectionListener *listener;
};

}

#endif /* TCPCONNECTION_H_ */
