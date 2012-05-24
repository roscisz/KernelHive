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
}

void TCPClient::start() {
	tryConnectingUntilDone();
}

void TCPClient::onDisconnected(int sockfd) {
	reconnectSocket();
}

void TCPClient::onMessage(int sockfd, TCPMessage *message) {
	listener->onMessage(message);
}

void TCPClient::sendMessage(char *message) {
	connection->sendMessage(message);
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
	connection = new TCPConnection(sockfd, this);
	ThreadManager::Get()->runThread(this->connection);
}

void TCPClient::disconnectFromSocket() {
	// ThreadManager::Get()->stopThread(this->connection);
	this->connection->disconnect();
}

TCPClient::~TCPClient() {
}

}
