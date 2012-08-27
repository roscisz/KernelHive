/*
 * TCPServer.h
 *
 *  Created on: 15-05-2012
 *      Author: roy
 */

#ifndef TCPSERVER_H_
#define TCPSERVER_H_

#include <map>
#include "NetworkEndpoint.h"
#include "NetworkAddress.h"
#include "TCPServerListener.h"
#include "TCPConnection.h"

namespace KernelHive {

typedef std::map<int, TCPConnection*> ConnectionMap;

class TCPServer : public NetworkEndpoint, public TCPConnectionListener, public LoopedThread {
public:
	TCPServer(NetworkAddress *serverAddress, TCPServerListener *listener);
	virtual ~TCPServer();

	void onMessage(int sockfd, TCPMessage *message);
	void onDisconnected(int sockfd);

	void executeLoopCycle();

	void sendMessage(int sockfd, TCPMessage *message);

private:
	void bindSocket();
	void disconnectFromSocket(int sockfd);
	void disconnectFromSocket(TCPConnection *connection);

	TCPServerListener *listener;
	ConnectionMap connectionMap;
};

}

#endif /* TCPSERVER_H_ */
