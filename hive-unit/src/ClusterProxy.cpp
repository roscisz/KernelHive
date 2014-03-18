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
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include "ClusterProxy.h"
#include "commons/Logger.h"
#include "commons/OpenClHost.h"
#include "HostStatusSerializer.h"

namespace KernelHive {

ClusterProxy::ClusterProxy(NetworkAddress *clusterAddress, TCPClientListener *listener)
		: TCPClient(clusterAddress, listener) {

}

void ClusterProxy::sendUpdate(HostStatus* hoststatus) {
	HostStatusSerializer *serializer = new HostStatusSerializer();
	std::string data = "UPDATE " + serializer->serializeToStringInitialMessage(hoststatus);

	TCPMessage *message = new TCPMessage((byte *) data.c_str(), data.size());
	sendMessage(message);
}

ClusterProxy::~ClusterProxy() {
}

}
