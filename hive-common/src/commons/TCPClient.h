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
#include <netinet/in.h>
#include <netdb.h>

#include "TCPClientListener.h"

#define MAX_MESSAGE_BYTES 1024
#define CONNECTION_RETRY_SECONDS 10

namespace KernelHive {

class TCPClient {
private:
	TCPClientListener *listener;
	int sockfd;
	struct sockaddr_in clusterAddress;

	void tryConnectingUntilDone();
	void reconnectSocket();
	int openSocket();
	void connectToSocket();
	char* readMessage();
	void disconnectFromSocket();
	struct sockaddr_in prepareClusterAddress(const char *host, int port);
public:
	TCPClient(const char *host, int port);
	virtual ~TCPClient();
	void registerListener(TCPClientListener *listener);
	void sendMessage(char *msg); // FIXME: Should be protected?
	void listenOnSocket();
};

#endif /* TCPCLIENT_H_ */

}
