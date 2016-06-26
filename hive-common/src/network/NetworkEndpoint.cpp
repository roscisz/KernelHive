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
#include <stdlib.h>
#include <string.h>
#include "NetworkEndpoint.h"

namespace KernelHive {

NetworkEndpoint::NetworkEndpoint(NetworkAddress *serverAddress) {
	this->serverAddress = prepareServerAddress(serverAddress);
}

struct sockaddr_in NetworkEndpoint::prepareServerAddress(NetworkAddress *serverAddress) {
	const char *host = serverAddress->host;
	int port = serverAddress->port;
	struct hostent *server;
	struct sockaddr_in serveraddr;

	server = gethostbyname(host);
	if (server == NULL) throw("No such host.\n");

	bzero((char *) &serveraddr, sizeof(serveraddr));
	serveraddr.sin_family = AF_INET;
	bcopy((char *)server->h_addr,
			(char *)&serveraddr.sin_addr.s_addr, server->h_length);
	serveraddr.sin_port = htons(port);

	return serveraddr;
}

int NetworkEndpoint::openSocket(int type) {
	int sockfd = socket(AF_INET, type, 0);
	if (sockfd < 0)	throw("Couldn't open socket.\n");
	return sockfd;
}

NetworkEndpoint::~NetworkEndpoint() {
	// TODO Auto-generated destructor stub
}

}
