/*
 * TCPConnection.cpp
 *
 *  Created on: 24-05-2012
 *      Author: roy
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
	if(listener != NULL)
		listener->onMessage(this->sockfd, message);
}

TCPMessage* TCPConnection::readMessage() {
	byte *message = (byte *)(calloc(MAX_MESSAGE_BYTES, sizeof (byte)));
        bzero(message, MAX_MESSAGE_BYTES);
        int n = read(sockfd, message, MAX_MESSAGE_BYTES);
        if(n < 0)
            throw (strerror(errno));

        if(n == 0)
            throw ("Server disconnected.");

        for (int i = 0; i < n; i++)
        {
        	if (i == 8) {
        		break;
        	}
        	printf("RAW BYTE LOG: %d\n", (int)message[i]);
        }

        return new TCPMessage(message, n);
}

/*
void TCPConnection::sendMessage(byte *msg)
{
	sendMessage(msg, strlen(msg));
}*/

void TCPConnection::sendMessage(TCPMessage *message) {
	//Logger::log(INFO, "Seding message: %s", message->data);
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
}

TCPConnection::~TCPConnection() {
	// TODO Auto-generated destructor stub
}

}
