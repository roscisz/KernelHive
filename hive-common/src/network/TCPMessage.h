/*
 * TCPMessage.h
 *
 *  Created on: 23-05-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#ifndef TCPMESSAGE_H_
#define TCPMESSAGE_H_

namespace KernelHive {

class TCPMessage {
public:
	TCPMessage(char *data, int nBytes);
	virtual ~TCPMessage();

	char *data;
	int nBytes;
};

}

#endif /* TCPMESSAGE_H_ */
