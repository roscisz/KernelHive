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
	printf("UDP PORT %d", serverAddress->port);

}

void UDPClient::sendMessage(char *message) {
	if (sendto(this->sockfd, message, strlen(message), 0,
			   (struct sockaddr *)&this->serverAddress, sizeof(this->serverAddress)) < 0)
		Logger::log(ERROR, "Error writing to socket.\n");
	Logger::log(INFO, "Sent UDP message %s\n", message);
}

UDPClient::~UDPClient() {
}

}
