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
#ifndef TCPCONNECTION_H_
#define TCPCONNECTION_H_

#include "../threading/LoopedThread.h"
#include "TCPConnectionListener.h"
#include "TCPMessage.h"
#include "../commons/byte.h"

#define MAX_MESSAGE_BYTES 1492

namespace KernelHive {

class TCPConnection : public LoopedThread {
public:
	TCPConnection(int sockfd, TCPConnectionListener *listener);
	void executeLoopCycle();
	//void sendMessage(byte *msg); // FIXME: Should be protected?
	void sendMessage(TCPMessage *message);
	void disconnect();
	virtual ~TCPConnection();
private:
	int sockfd;
	TCPMessage *readMessage();
	TCPConnectionListener *listener;
};

}

#endif /* TCPCONNECTION_H_ */
