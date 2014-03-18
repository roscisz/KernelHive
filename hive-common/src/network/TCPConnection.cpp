/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
#include <cstdio>
#include <cstdlib>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/socket.h>

#include "../commons/Logger.h"
#include "TCPConnection.h"

namespace KernelHive {

TCPConnection::TCPConnection(int sockfd, TCPConnectionListener *listener) {
	this->listener = listener;
	this->sockfd = sockfd;
}

void TCPConnection::executeLoopCycle() {
	TCPMessage *message;
	try {
		message = readMessage();
	}
	catch(const char *msg) {
		Logger::log(ERROR, "Couldn't read from socket: %s.\n", msg);
		listener->onDisconnected(this->sockfd);
		return;
	}
	if(listener != NULL && message != NULL)
		listener->onMessage(this->sockfd, message);
}

TCPMessage* TCPConnection::readMessage() {
	byte *message = (byte *)(calloc(MAX_MESSAGE_BYTES, sizeof (byte)));
        bzero(message, MAX_MESSAGE_BYTES);
        int n = read(sockfd, message, MAX_MESSAGE_BYTES);
        if(n < 0)
            throw (strerror(errno));

        if(n == 0) {
            //throw ("Server disconnected.");
        	return NULL;
        }

        return new TCPMessage(message, n);
}

/*
void TCPConnection::sendMessage(byte *msg)
{
	sendMessage(msg, strlen(msg));
}*/

void TCPConnection::sendMessage(TCPMessage *message) {
	if(this->sockfd == 0)
		printf("Trying to send message without connection");
	if(write(sockfd, &message->nBytes, sizeof(int)) < 0)
		Logger::log(ERROR, "Error writing to socket.\n");
	if(write(sockfd, message->data, message->nBytes) < 0)
		Logger::log(ERROR, "Error writing to socket.\n");
}

void TCPConnection::disconnect()
{
	if(shutdown(sockfd, SHUT_RDWR) < 0) {
		Logger::log(FATAL, "Error disconnecting socket: %s\n", strerror(errno));
	}
	this->sockfd = 0;
}

TCPConnection::~TCPConnection() {
	// TODO Auto-generated destructor stub
}

}
