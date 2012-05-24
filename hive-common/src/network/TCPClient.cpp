/*
 * ClusterProxy.cpp
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include "TCPClient.h"
#include "../commons/Logger.h"
#include "../commons/OpenClPlatform.h"
#include "../threading/ThreadManager.h"

namespace KernelHive {

TCPClient::TCPClient(NetworkAddress *serverAddress, TCPClientListener *listener) : NetworkEndpoint(serverAddress) {
	this->listener = listener;
	ThreadManager::Get()->runThread(this);
}

void TCPClient::executeLoopCycle() {
	TCPMessage *message;
	try {
		message = readMessage();
	}
	catch(const char *msg) {
		Logger::log(ERROR, "Couldn't read from socket: %s. Reconnecting.\n", msg);
		reconnectSocket();
		return;
	}
	if(listener != NULL)
		listener->onMessage(message);
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
		if(listener != NULL)
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

TCPMessage* TCPClient::readMessage() {
	char *message = (char *) calloc(MAX_MESSAGE_BYTES, sizeof(char));

	bzero(message, MAX_MESSAGE_BYTES);
	int n = read(sockfd, message, MAX_MESSAGE_BYTES);
	if (n < 0) throw(strerror(errno));
	if (n == 0) throw("Server disconnected.");

	return new TCPMessage(message, n);
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
