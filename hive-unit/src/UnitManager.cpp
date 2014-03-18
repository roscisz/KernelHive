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
#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#include <strings.h>
#include <string.h>
#include "UnitManager.h"
#include "commons/Logger.h"
#include "commons/OpenClPlatform.h"
#include "commons/WorkerProxy.h"
#include "network/NetworkAddress.h"
#include "network/UDPClient.h"
#include "network/DataPublisher.h"
#include "threading/ThreadManager.h"
#include "commons/SampleWorker.h"
#include "HostStatus.h"

namespace KernelHive {

UnitManager::UnitManager(char *clusterHostname, SystemMonitor* systemMonitor) {
	this->systemMonitor = systemMonitor;
	try {
		this->clusterProxy = new ClusterProxy(new NetworkAddress(clusterHostname, 31338), this);
	}
	catch(const char *msg) {
		Logger::log(FATAL, "Couldn't open Cluster Proxy: %s\n", msg);
		exit(EXIT_FAILURE);
	}
	ThreadManager::Get()->runThread(clusterProxy);
}

UnitManager::~UnitManager() {
	printf("UnitManager destroyed.\n");
}

void UnitManager::listen() {
	while(true) {
		sleep(10000);
	}
}

void UnitManager::onMessage(TCPMessage *message) {
	short int id;
	if(sscanf((const char *)message->data, "%hd", &id) > 0) {
		printf("Got ID: %hd\n", id);
		systemMonitor->setId(id);
		HostStatus* hostStatus = systemMonitor->createHostStatus();
		clusterProxy->sendUpdate(hostStatus);
		return;
	}

	// FIXME: extract a constant
	char *type = (char *) calloc(30, sizeof(char));

	sscanf((const char *)message->data, "%s ", type);

	printf("Received job to run, type: %s\n", type);

	//char *args = strstr(message->data, " ") + 1; // + 1 ?

	printf("Args: %s\n", message->data);
	WorkerProxy *proxy = WorkerProxy::create(type, (char *)message->data);
}

void UnitManager::onConnected() {
	printf("Connected to cluster\n");
}

}
