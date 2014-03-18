/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
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
