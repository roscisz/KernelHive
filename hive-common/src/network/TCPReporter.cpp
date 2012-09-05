/*
 * TCPReporter.cpp
 *
 *  Created on: 05-09-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#include "TCPReporter.h"

namespace KernelHive {

TCPReporter::TCPReporter(NetworkAddress* address, TCPMessage* message) : TCPClient(address, this){
	this->message = message;
}

TCPReporter::~TCPReporter() {
	// TODO Auto-generated destructor stub
}

void TCPReporter::onConnected() {
	sendMessage(this->message);
	pleaseStop();
}

void TCPReporter::onMessage(TCPMessage *message) {
	// Nothing to do here...
}

}
