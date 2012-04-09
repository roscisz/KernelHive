/*
 * ClusterProxy.h
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#ifndef CLUSTERPROXY_H_
#define CLUSTERPROXY_H_

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#include "IClusterListener.h"

#define MAX_MESSAGE_BYTES 32
#define CONNECTION_RETRY_SECONDS 10

class ClusterProxy {
private:
	IClusterListener *listener;
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
	ClusterProxy(const char *host, int port);
	virtual ~ClusterProxy();
	void registerListener(IClusterListener *listener);
	void sendMessage(char *msg);
	void listenOnSocket();
};

#endif /* CLUSTERPROXY_H_ */
