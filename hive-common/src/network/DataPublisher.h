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
#ifndef DATAPUBLISHER_H_
#define DATAPUBLISHER_H_

#include "NetworkAddress.h"
#include "TCPServer.h"
#include "TCPServerListener.h"

namespace KernelHive {

#define		CMD_PUT		0
#define 	CMD_GETSIZE	1
#define		CMD_GET		2
#define		CMD_DELETE	3

typedef std::map<int, char *> DataMap;

class DataPublisher : public TCPServerListener {
public:
	DataPublisher(NetworkAddress *serverAddress);
	virtual ~DataPublisher();

	void onMessage(int sockfd, TCPMessage *message);

	int publish(char *data);
	void publish(int id, char *data);
private:
	const char* processMessage(TCPMessage *message);

	const char *putData(int size, TCPMessage *message);
	const char *getSize(int id);
	const char *getData(int id);
	const char *deleteData(int id);

	int generateID();

	void throwIfIdOutOfRange(int id);

	TCPServer *server;
	DataMap dataMap;
	int prevId;
};

}

#endif /* DATAPUBLISHER_H_ */
