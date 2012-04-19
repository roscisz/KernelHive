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

UDPClient::UDPClient(const char *host, int port) : NetworkClient(host, port) {

	this->sockfd = openSocket(SOCK_DGRAM);

}

void UDPClient::sendMessage(char *message) {
	if (sendto(this->sockfd, message, strlen(message), 0,
			   (struct sockaddr *)&this->serverAddress, sizeof(this->serverAddress)) < 0)
		Logger::log(ERROR, "Error writing to socket.\n");
	printf("Sent UDP message %s\n", message);
}

UDPClient::~UDPClient() {
}

}
