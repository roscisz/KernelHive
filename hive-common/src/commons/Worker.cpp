/*
 * Worker.cpp
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#include <cstdio>
#include "../threading/ThreadManager.h"
#include "Worker.h"

namespace KernelHive {

Worker::Worker(NetworkAddress *clusterAddress) {

	this->reporter = new UDPReporter(clusterAddress, this);
	this->percentDone = -1;
	this->paramOffset = 0;

	threadManager = ThreadManager::Get();

	ThreadManager::Get()->runThread(this->reporter);
}

int Worker::getPercentDone() {
	return this->percentDone;
}

void Worker::setPercentDone(int progress) {
	this->percentDone = progress;
}

char* Worker::nextParam(char *const argv[]) {
	return argv[paramOffset++];
}

Worker::~Worker() {
	ThreadManager::Get()->pleaseStopAllThreads();
}

}
