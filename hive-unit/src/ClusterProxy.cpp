/*
 * ClusterProxy.cpp
 *
 *  Created on: 05-04-2012
 *      Author: roy
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
