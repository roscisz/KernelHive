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
#include <netinet/tcp.h>
#include <errno.h>
#include "TCPClient.h"
#include "../commons/Logger.h"
#include "../commons/OpenClPlatform.h"
#include "../threading/ThreadManager.h"

namespace KernelHive {

TCPClient::TCPClient(NetworkAddress *serverAddress, TCPClientListener *listener) : NetworkEndpoint(serverAddress) {
	this->listener = listener;
	this->connection = NULL;
}

void TCPClient::onDisconnected(int sockfd) {
	disconnectFromSocket();
}

void TCPClient::onMessage(int sockfd, TCPMessage *message) {
	listener->onMessage(message);
}

void TCPClient::sendMessage(const char *message) {
	connection->sendMessage(message);
}

void TCPClient::sendMessage(TCPMessage *message) {
	connection->sendMessage(message->data, message->nBytes);
}

void TCPClient::executeLoopCycle() {
	if(this->connection == NULL) {
		try {
			this->sockfd = openSocket(SOCK_STREAM);
			this->setNoDelay();
			connectToSocket();
			listener->onConnected();
		}
		catch(const char *msg) {
			Logger::log(ERROR, "Couldn't connect to the cluster: %s.\n", msg);
			disconnectFromSocket();
		}
	}
	sleep(CONNECTION_RETRY_SECONDS);
}

void TCPClient::connectToSocket() {
	if (connect(sockfd, (struct sockaddr *)&serverAddress, sizeof(serverAddress)) < 0)
		throw(strerror(errno));
	connection = new TCPConnection(sockfd, this);
	ThreadManager::Get()->runThread(this->connection);
}

void TCPClient::disconnectFromSocket() {
	if(this->connection != NULL) {
		ThreadManager::Get()->pleaseStopThread(this->connection);
		this->connection->disconnect();
	}
	this->connection = NULL;
}

void TCPClient::pleaseStop() {
	LoopedThread::pleaseStop();
	disconnectFromSocket();
}

void TCPClient::setNoDelay() {
	this->noDelayFlag = 1;
	int ret = setsockopt(this->sockfd, IPPROTO_TCP, TCP_NODELAY, (char *)&noDelayFlag, sizeof(int));
	if(ret < 0) throw(strerror(errno));
}

TCPClient::~TCPClient() {
}

}
