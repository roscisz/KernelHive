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
#include "ClusterProxy.h"

UnitManager::UnitManager() {

	ClusterProxy *cluster = new ClusterProxy("localhost", 31338, this);

	while(true) {
		// FIXME: define MAX message size
#define BUFSIZE 32
		char buf[BUFSIZE];

	    printf("Please enter msg: ");
	    bzero(buf, BUFSIZE);
	    fgets(buf, BUFSIZE, stdin);

	    cluster->sendMessage((char*) buf);
	}


	// Connect to the engine using conf
	printf("Connecting unit to the egine.\n");
}

UnitManager::~UnitManager() {
	printf("UnitManager destroyed.\n");
}

void UnitManager::listen() {
	// Loop listening on daemon port

	// When given a task to do, run a worker


}
