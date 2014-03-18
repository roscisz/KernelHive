/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Rafal Lewandowski
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
#include "../network/TCPClient.h"
#include "../network/TCPReporter.h"
#include "../threading/ThreadManager.h"
#include "KhUtils.h"
#include "Worker.h"

using namespace std;

namespace KernelHive {

Worker::Worker(char **argv) {
	jobID = KhUtils::atoi(argv[1]);

	this->clusterTCPAddress = new NetworkAddress(argv[2], argv[3]);
	this->reporter = new UDPReporter(jobID, new NetworkAddress(argv[2], argv[4]), this);
	this->previewReporter = new ProgressReporter(jobID, new NetworkAddress(argv[2], argv[4]));

	this->percentDone = -1;
	this->paramOffset = 0;

	threadManager = ThreadManager::Get();

	ThreadManager::Get()->runThread(this->reporter);
}

int Worker::getPercentDone() {
	return this->percentDone;
}

void Worker::setPercentDone(int progress) {
	printf("done %d\n", progress);
	this->percentDone = progress;
}

char* Worker::nextParam(char *const argv[]) {
	return argv[paramOffset++];
}

void Worker::reportOver(const char* uploadIDs) {
	printf("Reporting over: %s\n", uploadIDs);
	std::string report = "OVER ";
	report.append(KhUtils::itoa(jobID));
	report.append(" ");
	report.append(uploadIDs);

	//TCPClient *client = new TCPClient(clusterTCPAddress, NULL);

	TCPMessage *message = new TCPMessage((byte *)report.c_str(), report.length());
	TCPReporter *tcpReporter = new TCPReporter(clusterTCPAddress, message);
	printf("Reporting start\n", uploadIDs);
	ThreadManager::Get()->runThread(tcpReporter);
	ThreadManager::Get()->waitForThread(tcpReporter);
	printf("Reported\n", uploadIDs);
}

void Worker::reportPreview(SynchronizedBuffer *buffer) {
	previewReporter->sendReport(buffer);
}

Worker::~Worker() {
	ThreadManager::Get()->pleaseStopAllThreads();
}

}
