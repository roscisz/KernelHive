/*
 * TCPServerListener.h
 *
 *  Created on: 15-05-2012
 *      Author: roy
 */

#ifndef TCPSERVERLISTENER_H_
#define TCPSERVERLISTENER_H_

#include "TCPMessage.h"

namespace KernelHive {

class TCPServerListener {
public:
	TCPServerListener();
	virtual ~TCPServerListener();
	virtual void onMessage(int sockfd, TCPMessage *message) {};
};

}

#endif /* TCPSERVERLISTENER_H_ */
