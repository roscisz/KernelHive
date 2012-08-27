/*
 * TCPMessage.cpp
 *
 *  Created on: 23-05-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#include "TCPMessage.h"

namespace KernelHive {

TCPMessage::TCPMessage(byte *data, int nBytes) {
	this->data = data;
	this->nBytes = nBytes;
}

TCPMessage::~TCPMessage() {
	// TODO Auto-generated destructor stub
}

}
