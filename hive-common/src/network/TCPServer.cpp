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
#include <string.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include "TCPServer.h"
#include "../commons/Logger.h"
#include "../threading/ThreadManager.h"

namespace KernelHive {

TCPServer::TCPServer(NetworkAddress *serverAddress, TCPServerListener *listener) : NetworkEndpoint(serverAddress) {
	this->listener = listener;

	this->sockfd = openSocket(SOCK_STREAM);
	bindSocket();
	listen(this->sockfd, 10); //TODO: backlog size definition

}

void TCPServer::bindSocket() {
	if(bind(this->sockfd, (struct sockaddr *)&this->serverAddress, sizeof(this->serverAddress)) < 0) {
		Logger::log(FATAL, "TCPSocket couldn't bind socket: %s", strerror(errno));
	}
}

void TCPServer::executeLoopCycle() {
	sockaddr clientAddress;
	int clientAddressSize = sizeof(clientAddress);
	int clientSockfd = accept(this->sockfd, (struct sockaddr *)&clientAddress, (socklen_t *)&clientAddressSize);

	if(clientSockfd < 0) {
		Logger::log(FATAL, "TCPServer accept() failed: %s", strerror(errno));
	}

	TCPConnection *connection = new TCPConnection(clientSockfd, this);
	connectionMap[clientSockfd] = connection;
	ThreadManager::Get()->runThread(connection);
}

void TCPServer::onMessage(int sockfd, TCPMessage *message) {
	this->listener->onMessage(sockfd, message);
}

void TCPServer::onDisconnected(int sockfd) {
	disconnectFromSocket(sockfd);
}

void TCPServer::disconnectFromSocket(int sockfd) {
	ConnectionMap::iterator iterator = connectionMap.find(sockfd);
	if (iterator != connectionMap.end()) {
		disconnectFromSocket(connectionMap[sockfd]);
		connectionMap.erase(sockfd);
	}
	Logger::log(INFO, "Client disconnected, %d left.", connectionMap.size());
}

void TCPServer::disconnectFromSocket(TCPConnection *connection) {
	ThreadManager::Get()->pleaseStopThread(connection);
	connection->disconnect();
}

void TCPServer::sendMessage(int sockfd, TCPMessage *message) {
	ConnectionMap::iterator iterator = connectionMap.find(sockfd);
	if(iterator != connectionMap.end()) {
		connectionMap[sockfd]->sendMessage(message);
	}
}

TCPServer::~TCPServer() {

}

}
