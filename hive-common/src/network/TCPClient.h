/*
 * ClusterProxy.h
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#ifndef TCPCLIENT_H_
#define TCPCLIENT_H_

#include <sys/types.h>
#include <sys/socket.h>

#include "../threading/LoopedThread.h"
#include "NetworkAddress.h"
#include "NetworkEndpoint.h"
#include "TCPMessage.h"
#include "TCPClientListener.h"
#include "TCPConnection.h"

#define CONNECTION_RETRY_SECONDS 10

namespace KernelHive {

class TCPClient : public NetworkEndpoint, public TCPConnectionListener, public LoopedThread {
private:
	TCPClientListener *listener;
	TCPConnection *connection;

	void executeLoopCycle();
	void connectToSocket();
	void disconnectFromSocket();
public:
	TCPClient(NetworkAddress *serverAddress, TCPClientListener *listener);

	void onDisconnected(int sockfd);
	void onMessage(int sockfd, TCPMessage *message);

	void sendMessage(char *message);

	void pleaseStop();

	virtual ~TCPClient();
};

}

#endif /* TCPCLIENT_H_ */

