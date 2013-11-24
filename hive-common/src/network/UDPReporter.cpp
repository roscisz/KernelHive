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
	usleep(UDP_REPORT_MSECONDS*1000);
}

void UDPReporter::sendReport(int percentDone) {
	// FIXME: message size
	char message[60];
	sprintf(message, "1 %d %d\n", jobID, percentDone);
	udpClient->sendMessage(message);
}

UDPReporter::~UDPReporter() {

}

}
