/*
 * TCPConnectionListener.h
 *
 *  Created on: 24-05-2012
 *      Author: roy
 */

#ifndef TCPCONNECTIONLISTENER_H_
#define TCPCONNECTIONLISTENER_H_

#include "TCPMessage.h"

namespace KernelHive {

class TCPConnectionListener {
public:
	TCPConnectionListener();

	virtual void onMessage(int sockfd, TCPMessage *message) {} ;
	virtual void onDisconnected(int sockfd) {};

	virtual ~TCPConnectionListener();
};

}

#endif /* TCPCONNECTIONLISTENER_H_ */
