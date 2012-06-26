/*
 * UDPReporter.cpp
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#include <unistd.h>
#include <cstdio>
#include "UDPReporter.h"

namespace KernelHive {

UDPReporter::UDPReporter(int jobID, NetworkAddress *serverAddress, IReportable *reportable) {
	this->jobID = jobID;
	this->reportable = reportable;
	this->udpClient = new UDPClient(serverAddress);

}

void UDPReporter::executeLoopCycle() {
	sendReport(reportable->getPercentDone());
	sleep(UDP_REPORT_SECONDS);
}

void UDPReporter::sendReport(int percentDone) {
	// FIXME: message size
	char message[60];
	sprintf(message, "%d %d\n", jobID, percentDone);
	udpClient->sendMessage(message);
}

void UDPReporter::reportOver(char *status) {
	// FIXME:
	char *msg = new char[100];
	sprintf(msg, "%d OVER %s\n", jobID, status);
	udpClient->sendMessage(msg);
}

UDPReporter::~UDPReporter() {

}

}
