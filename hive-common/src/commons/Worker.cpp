/*
 * Worker.cpp
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#include <cstdio>
#include <cstdlib>
#include "../network/TCPClient.h"
#include "../network/TCPReporter.h"
#include "../threading/ThreadManager.h"
#include "KhUtils.h"
#include "Worker.h"

namespace KernelHive {

Worker::Worker(char **argv) {
	jobID = KhUtils::atoi(argv[1]);

	this->clusterTCPAddress = new NetworkAddress(argv[2], argv[3]);
	this->reporter = new UDPReporter(jobID, new NetworkAddress(argv[2], argv[4]), this);

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

void Worker::reportOver(const char* uploadIDs) {
	printf("Reporting over\n");
	std::string report = "OVER ";
	report.append(KhUtils::itoa(jobID));
	report.append(" ");
	report.append(uploadIDs);

	TCPClient *client = new TCPClient(clusterTCPAddress, NULL);

	TCPMessage *message = new TCPMessage((byte *)report.c_str(), report.length());
	TCPReporter *tcpReporter = new TCPReporter(clusterTCPAddress, message);
	ThreadManager::Get()->runThread(tcpReporter);
	ThreadManager::Get()->waitForThread(tcpReporter);
}

Worker::~Worker() {
	ThreadManager::Get()->pleaseStopAllThreads();
}

}
