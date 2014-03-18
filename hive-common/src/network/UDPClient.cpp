/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
