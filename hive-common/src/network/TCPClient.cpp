/*
 * ClusterProxy.cpp
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include "TCPClient.h"
#include "../commons/Logger.h"
#include "../commons/OpenClPlatform.h"

namespace KernelHive {

TCPClient::TCPClient(NetworkAddress *serverAddress) : NetworkClient(serverAddress) {

}

void TCPClient::registerListener(TCPClientListener *listener) {
	this->listener = listener;
}

void TCPClient::listenOnSocket() {

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

void TCPClient::tryConnectingUntilDone() {
	while(true) {
		try {
			this->sockfd = openSocket(SOCK_STREAM);
			connectToSocket();
		}
		catch(const char *msg) {
			Logger::log(ERROR, "Couldn't connect to the cluster: %s. Retrying.\n", msg);
			sleep(CONNECTION_RETRY_SECONDS);
			continue;
		}
		listener->onConnected();
		break;
	}
}

void TCPClient::reconnectSocket() {
	disconnectFromSocket();
	tryConnectingUntilDone();
}

void TCPClient::connectToSocket() {
	if (connect(sockfd, (struct sockaddr *)&serverAddress, sizeof(serverAddress)) < 0)
		throw(strerror(errno));
}

char* TCPClient::readMessage() {
	char message[MAX_MESSAGE_BYTES];

	bzero(message, MAX_MESSAGE_BYTES);
	int n = read(sockfd, message, MAX_MESSAGE_BYTES);
	if (n < 0) throw(strerror(errno));
	if (n == 0) throw("Server disconnected.");

	return message;
}

void TCPClient::sendMessage(char *msg) {
	if (write(sockfd, msg, strlen(msg)) < 0)
		Logger::log(ERROR, "Error writing to socket.\n");
	printf("Sent TCP message %s\n");
}

void TCPClient::disconnectFromSocket() {
	shutdown(sockfd, SHUT_RDWR);
}

TCPClient::~TCPClient() {
}

}
