/*
 * DataPublisher.h
 *
 *  Created on: 30-05-2012
 *      Author: roy
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
