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

namespace KernelHive {

UnitManager::UnitManager() {

	try {
		this->clusterProxy = new ClusterProxy("localhost", 31338);
	}
	catch(const char *msg) {
		Logger::log(FATAL, "Couldn't open Cluster Proxy: %s\n", msg);
		exit(EXIT_FAILURE);
	}

	this->clusterProxy->registerListener(this);
}

UnitManager::~UnitManager() {
	printf("UnitManager destroyed.\n");
}

void UnitManager::listen() {
	clusterProxy->listenOnSocket();
}

void UnitManager::onMessage(char *message) {
	printf("Echo from server: %s", message);

	// TODO: When given a task to do, run a worker
}

void UnitManager::onConnected() {
	clusterProxy->sendUpdate();
}

}
