/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Rafal Lewandowski
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

#define CONNECTION_RETRY_SECONDS 1

namespace KernelHive {

class TCPClient : public NetworkEndpoint, public TCPConnectionListener, public LoopedThread {
private:
	TCPClientListener *listener;
	TCPConnection *connection;
	int noDelayFlag;

	void executeLoopCycle();
	void connectToSocket();
	void disconnectFromSocket();
	void setNoDelay();
public:
	TCPClient(NetworkAddress *serverAddress, TCPClientListener *listener);

	void onDisconnected(int sockfd);
	void onMessage(int sockfd, TCPMessage *message);

	//void sendMessage(byte *message);
	void sendMessage(TCPMessage *message);

	void pleaseStop();

	virtual ~TCPClient();
};

}

#endif /* TCPCLIENT_H_ */

