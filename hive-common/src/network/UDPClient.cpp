/*
 * UDPClient.cpp
 *
 *  Created on: 17-04-2012
 *      Author: roy
 */

#include <cstdio>
#include <unistd.h>
#include <cstdlib>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <error.h>
#include <string.h>
#include "../commons/Logger.h"
#include "UDPClient.h"

namespace KernelHive {

UDPClient::UDPClient(NetworkAddress *serverAddress) : NetworkEndpoint(serverAddress) {

	this->sockfd = openSocket(SOCK_DGRAM);

}

void UDPClient::sendMessage(char *message) {
	sendMessage(message, strlen(message));
}

void UDPClient::sendMessage(char *message, size_t length) {
	if (sendto(this->sockfd, message, length, 0,
			   (struct sockaddr *)&this->serverAddress, sizeof(this->serverAddress)) < 0) {
		Logger::log(ERROR, "Error writing to socket.\n");
	}
}

UDPClient::~UDPClient() {
}

}
