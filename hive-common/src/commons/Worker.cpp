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

Worker::Worker(char *clusterHostname, int clusterPort) {

	this->reporter = new UDPReporter(clusterHostname, clusterPort, this);
	this->percentDone = -1;

	ThreadManager::Get()->runThread(this->reporter);
}

int Worker::getPercentDone() {
	return this->percentDone;
}

void Worker::setPercentDone(int progress) {
	this->percentDone = progress;
}

Worker::~Worker() {
	ThreadManager::Get()->pleaseStopAllThreads();
}

}
