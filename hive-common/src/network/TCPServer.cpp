/*
 * TCPServer.cpp
 *
 *  Created on: 15-05-2012
 *      Author: roy
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

void TCPServer::sendMessage(int sockfd, const char *message) {
	ConnectionMap::iterator iterator = connectionMap.find(sockfd);
	if(iterator != connectionMap.end()) {
		connectionMap[sockfd]->sendMessage(message);
	}
}

TCPServer::~TCPServer() {

}

}
