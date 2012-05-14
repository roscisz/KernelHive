/*
 * NetworkClient.cpp
 *
 *  Created on: 17-04-2012
 *      Author: roy
 */

#include <stdlib.h>
#include <string.h>
#include "NetworkClient.h"

namespace KernelHive {

NetworkClient::NetworkClient(NetworkAddress *serverAddress) {
	this->serverAddress = prepareServerAddress(serverAddress);
}

struct sockaddr_in NetworkClient::prepareServerAddress(NetworkAddress *serverAddress) {
	char *host = serverAddress->host;
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

int NetworkClient::openSocket(int type) {
	int sockfd = socket(AF_INET, type, 0);
	if (sockfd < 0)	throw("Couldn't open socket.\n");
	return sockfd;
}

NetworkClient::~NetworkClient() {
	// TODO Auto-generated destructor stub
}

}
