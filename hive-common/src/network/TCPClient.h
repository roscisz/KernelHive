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

#include "NetworkClient.h"
#include "TCPClientListener.h"

#define MAX_MESSAGE_BYTES 1024
#define CONNECTION_RETRY_SECONDS 10

namespace KernelHive {

class TCPClient : public NetworkClient {
private:
	TCPClientListener *listener;

	void tryConnectingUntilDone();
	void reconnectSocket();
	void connectToSocket();
	char* readMessage();
	void disconnectFromSocket();
public:
	TCPClient(const char *host, int port);
	virtual ~TCPClient();
	void registerListener(TCPClientListener *listener);
	void sendMessage(char *msg); // FIXME: Should be protected?
	void listenOnSocket();
};

#endif /* TCPCLIENT_H_ */

}
