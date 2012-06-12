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
#include "UnitManager.h"
#include "commons/Logger.h"
#include "commons/OpenClPlatform.h"
#include "commons/WorkerProxy.h"

#include "network/NetworkAddress.h"
#include "network/UDPClient.h"
#include "network/DataPublisher.h"
#include "threading/ThreadManager.h"
#include "commons/SampleWorker.h"

namespace KernelHive {

UnitManager::UnitManager() {

	// Testing worker
	WorkerProxy *proxy = WorkerProxy::create(/* type ,*/
			"bin 1 localhost 31338 localhost 31340 localhost 31340 ION 1 0 4096 64 456 123");

	try {
		this->clusterProxy = new ClusterProxy(new NetworkAddress("localhost", 31338), this);
	}
	catch(const char *msg) {
		Logger::log(FATAL, "Couldn't open Cluster Proxy: %s\n", msg);
		exit(EXIT_FAILURE);
	}
	ThreadManager::Get()->runThread(clusterProxy);

	DataPublisher *dataPublisher = new DataPublisher(new NetworkAddress("localhost", 31350));
	dataPublisher->publish(123, "DANE PRZYKLADOWE C++");
}

UnitManager::~UnitManager() {
	printf("UnitManager destroyed.\n");
}

void UnitManager::listen() {
	while(true);
}

void UnitManager::onMessage(TCPMessage *message) {
	printf("Echo from server: %s", message->data);

	// TODO: When given a task to do, run a worker
}

void UnitManager::onConnected() {
	clusterProxy->sendUpdate();
}

}
