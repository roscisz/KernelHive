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
#ifndef TCPCONNECTIONLISTENER_H_
#define TCPCONNECTIONLISTENER_H_

#include "TCPMessage.h"

namespace KernelHive {

class TCPConnectionListener {
public:
	TCPConnectionListener();

	virtual void onMessage(int sockfd, TCPMessage *message) {} ;
	virtual void onDisconnected(int sockfd) {};

	virtual ~TCPConnectionListener();
};

}

#endif /* TCPCONNECTIONLISTENER_H_ */
