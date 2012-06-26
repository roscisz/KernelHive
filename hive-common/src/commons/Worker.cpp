/*
 * Worker.cpp
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#include <cstdio>
#include "../commons/KhUtils.h"
#include "../threading/ThreadManager.h"
#include "Worker.h"

namespace KernelHive {

Worker::Worker(char **argv) {
	jobID = KhUtils::atoi(argv[1]);

	NetworkAddress *clusterAddress = new NetworkAddress(argv[2], argv[3]);
	this->reporter = new UDPReporter(jobID, clusterAddress, this);

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

void Worker::reportOver(char *status) {
	this->reporter->reportOver(status);
}

Worker::~Worker() {
	ThreadManager::Get()->pleaseStopAllThreads();
}

}
