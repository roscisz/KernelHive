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
#include <cstdio>
#include <cstdlib>
#include <string>
#include <string.h>
#include <stdexcept>
#include "../commons/KhUtils.h"
#include "../commons/Logger.h"
#include "DataPublisher.h"
#include "../threading/ThreadManager.h"

namespace KernelHive {

DataPublisher::DataPublisher(NetworkAddress *serverAddress) {
	this->server = new TCPServer(serverAddress, this);
	ThreadManager::Get()->runThread(this->server);
	this->prevId = 0;
}

int DataPublisher::publish(char *data) {
	int id = generateID();
	publish(id, data);
	return id;
}

void DataPublisher::publish(int id, char *data) {
	this->dataMap[id] = data;
}

void DataPublisher::onMessage(int sockfd, TCPMessage *message) {
	/*
	const char *answer;

	try {
		answer = processMessage(message);
	}
	catch(std::out_of_range oor) {
		answer = "No such ID";
	}

	this->server->sendMessage(sockfd, answer);
	*/
}

const char *DataPublisher::processMessage(TCPMessage *message) {
	//int command, arg;



	//sscanf(message->data, "%d %d", &command, &arg);
/*
	switch(command) {
	case CMD_PUT: return putData(arg, message);
	case CMD_GETSIZE: return getSize(arg);
	case CMD_GET: return getData(arg);
	case CMD_DELETE: return deleteData(arg);
	default: return "No such command";
	}
		*/
}

const char *DataPublisher::putData(int size, TCPMessage *message) {
	int cmd, arg, id;
	/*char *buffer = (char *) malloc(size);

	sscanf(message->data, "%d %d %s", &cmd, &arg, buffer);
	id = publish(buffer);

	return KhUtils::itoa(id);
	*/
}

const char *DataPublisher::getSize(int id) {
	/*
	throwIfIdOutOfRange(id);
	int size = strlen(dataMap[id]);
	return KhUtils::itoa(size);
	*/
}

const char *DataPublisher::getData(int id) {
	/*
	throwIfIdOutOfRange(id);
	return dataMap[id];
	*/
}

const char *DataPublisher::deleteData(int id) {
	/*
	throwIfIdOutOfRange(id);
	free(dataMap[id]);
	dataMap.erase(id);
	return "OK";
	*/
}

void DataPublisher::throwIfIdOutOfRange(int id) {
	/*
	DataMap::iterator iterator = dataMap.find(id);
	if(iterator == dataMap.end()) throw std::out_of_range("");
	*/
}

int DataPublisher::generateID() {
	return ++prevId;
}

/*
void DataPublisher::sendData(int id, int sockfd) {
	DataMap::iterator iterator = dataMap.find(id);
	if (iterator != dataMap.end())
		this->server->sendMessage(sockfd, dataMap[id]);
	else this->server->sendMessage(sockfd, "0\n");
}*/

DataPublisher::~DataPublisher() {
	// TODO Auto-generated destructor stub
}

}
