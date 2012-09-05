/*
 * TCPReporter.h
 *
 *  Created on: 05-09-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#ifndef TCPREPORTER_H_
#define TCPREPORTER_H_

#include "../network/TCPClient.h"
#include "../network/TCPClientListener.h"

namespace KernelHive {

class TCPReporter : public TCPClient, public TCPClientListener {
public:
	TCPReporter(NetworkAddress* address, TCPMessage* message);
	virtual ~TCPReporter();

	void onConnected();
	void onMessage(TCPMessage* message);

private:
	TCPMessage* message;


};

}

#endif /* TCPREPORTER_H_ */
