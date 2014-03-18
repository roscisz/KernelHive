/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Rafal Lewandowski
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
	if(listener != NULL)
		listener->onMessage(message);
}

/*
void TCPClient::sendMessage(byte *message) {
	connection->sendMessage(message);
}
*/

void TCPClient::sendMessage(TCPMessage *message) {
	connection->sendMessage(message);
}

void TCPClient::executeLoopCycle() {
	if(this->connection == NULL) {
		try {
			this->sockfd = openSocket(SOCK_STREAM);
			this->setNoDelay();
			connectToSocket();
			if(listener != NULL)
				listener->onConnected();
		}
		catch(const char *msg) {
			Logger::log(ERROR, "Couldn't connect the TCP client: %s, port %d\n", msg, ntohs(this->serverAddress.sin_port));
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
