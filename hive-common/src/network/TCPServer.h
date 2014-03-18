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
