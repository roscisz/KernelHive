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
#include "commons/OpenClPlatform.h"

namespace KernelHive {

ClusterProxy::ClusterProxy(const char *host, int port) : TCPClient(host, port) {

}

/* FIXME: this method should be rewritten
 * after OpenClPlatrorm changes.
 */
void ClusterProxy::sendUpdate() {
	//const char *cpus = OpenClPlatform::getCpuDevicesInfo().c_str();
	char message[MAX_MESSAGE_BYTES];
	//sprintf(message, "UPDATE %s", cpus);
	//sendMessage(message);


	const char *gpus = OpenClPlatform::getGpuDevicesInfo().c_str();
	sprintf(message, "UPDATE %s", gpus);
	sendMessage(message);
}

ClusterProxy::~ClusterProxy() {
}

}
