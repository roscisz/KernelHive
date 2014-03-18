/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
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
#ifndef CLUSTERPROXY_H_
#define CLUSTERPROXY_H_

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "network/NetworkAddress.h"
#include "HostStatus.h"

namespace KernelHive {

class ClusterProxy : public TCPClient {
public:
	ClusterProxy(NetworkAddress *clusterAddress, TCPClientListener *listener);
	virtual ~ClusterProxy();
	void sendUpdate(HostStatus* hoststatus);
};

#endif /* CLUSTERPROXY_H_ */

}
