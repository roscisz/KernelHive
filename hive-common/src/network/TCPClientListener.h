/*
 * IClusterListener.h
 *
 *  Created on: 09-04-2012
 *      Author: roy
 */

#ifndef TCPCLIENTLISTENER_H_
#define TCPCLIENTLISTENER_H_

#include "TCPMessage.h"

namespace KernelHive {

class TCPClientListener {
public:
	TCPClientListener();
	virtual ~TCPClientListener();
	virtual void onMessage(TCPMessage *message) {};
	virtual void onConnected() {};
};

}

#endif /* TCPCLIENTLISTENER_H_ */
