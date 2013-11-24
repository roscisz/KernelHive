/*
 * UDPReporter.cpp
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#include <unistd.h>
#include <cstdio>
#include <cstring>
#include "ProgressReporter.h"

namespace KernelHive {

ProgressReporter::ProgressReporter(int jobID, NetworkAddress *serverAddress) {
	this->jobID = jobID;
	this->udpClient = new UDPClient(serverAddress);

}

void ProgressReporter::sendReport(SynchronizedBuffer *buffer) {
	// FIXME: message size
	char *message = new char[buffer->getSize() + 32];
	sprintf(message, "2 %d %u \0", jobID, (unsigned int) buffer->getSize());
	int prefixLength = strlen(message);
	int messageLength = prefixLength + buffer->getSize() + 1;

	memcpy(message + prefixLength, buffer->getRawData(), buffer->getSize());

	/*printf("Report message of size %d: ", buffer->getSize());
	for (int i = prefixLength; i < messageLength; i++) {
		printf("%.2X ", message[i]);
	}
	printf("\n");*/
	udpClient->sendMessage(message, messageLength);

	delete message;
}

ProgressReporter::~ProgressReporter() {

}

}
