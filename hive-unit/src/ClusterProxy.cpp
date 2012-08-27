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

namespace KernelHive {

ClusterProxy::ClusterProxy(NetworkAddress *clusterAddress, TCPClientListener *listener) : TCPClient(clusterAddress, listener) {

}

void ClusterProxy::sendUpdate() {
	char data[MAX_MESSAGE_BYTES];
	const char *devices = OpenClHost::getDevicesInfo().c_str();
	sprintf(data, "UPDATE %s", devices);
	TCPMessage *message = new TCPMessage((byte *) data, strlen(data));
	sendMessage(message);
}

ClusterProxy::~ClusterProxy() {
}

}
