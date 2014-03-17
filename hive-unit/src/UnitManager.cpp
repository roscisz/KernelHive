/*
 * UnitManager.cpp
 *
 *  Created on: 12-03-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
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
