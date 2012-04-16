/*
 * ClusterProxy.cpp
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include "ClusterProxy.h"
#include "Logger.h"
#include "commons/OpenClPlatform.h"

namespace KernelHive {

ClusterProxy::ClusterProxy(const char *host, int port) {
	this->clusterAddress = prepareClusterAddress(host, port);
}

void ClusterProxy::registerListener(IClusterListener *listener) {
	this->listener = listener;
}

void ClusterProxy::listenOnSocket() {

	tryConnectingUntilDone();

	char *message;
	while(true) {
		try {
			message = readMessage();
		}
		catch(const char *msg) {
			Logger::log(ERROR, "Couldn't read from socket: %s. Reconnecting.\n", msg);
			reconnectSocket();
			continue;
		}
		listener->onMessage(message);
	}
}

void ClusterProxy::tryConnectingUntilDone() {
	while(true) {
		try {
			this->sockfd = openSocket();
			connectToSocket();
		}
		catch(const char *msg) {
			Logger::log(ERROR, "Couldn't connect to the cluster: %s. Retrying.\n", msg);
			sleep(CONNECTION_RETRY_SECONDS);
			continue;
		}
		sendUpdate();
		break;
	}
}

void ClusterProxy::reconnectSocket() {
	disconnectFromSocket();
	tryConnectingUntilDone();
}

int ClusterProxy::openSocket() {
	int sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0)	throw("Couldn't open socket.\n");
	return sockfd;
}

void ClusterProxy::connectToSocket() {
	if (connect(sockfd, (struct sockaddr *)&clusterAddress, sizeof(clusterAddress)) < 0)
		throw(strerror(errno));
}

char* ClusterProxy::readMessage() {
	char message[MAX_MESSAGE_BYTES];

	bzero(message, MAX_MESSAGE_BYTES);
	int n = read(sockfd, message, MAX_MESSAGE_BYTES);
	if (n < 0) throw(strerror(errno));
	if (n == 0) throw("Server disconnected.");

	return message;
}

void ClusterProxy::sendMessage(char *msg) {
	if (write(sockfd, msg, strlen(msg)) < 0)
		Logger::log(ERROR, "Error writing to socket.\n");
	printf("Sent message %s\n");
}

/* FIXME: this method should be rewritten
 * after OpenClPlatrorm changes.
 */
void ClusterProxy::sendUpdate() {
	//const char *cpus = OpenClPlatform::getCpuDevicesInfo().c_str();
	char message[MAX_MESSAGE_BYTES];
	//sprintf(message, "UPDATE %s", cpus);
	//sendMessage(message);


	const char *gpus = OpenClPlatform::getGpuDevicesInfo().c_str();
	sprintf(message, "UPDATE %s", gpus);
	sendMessage(message);
}

void ClusterProxy::disconnectFromSocket() {
	shutdown(sockfd, SHUT_RDWR);
}

struct sockaddr_in ClusterProxy::prepareClusterAddress(const char *host, int port) {
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

ClusterProxy::~ClusterProxy() {
}

}
