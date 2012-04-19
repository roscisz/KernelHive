/*
 * UDPReporter.h
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#ifndef UDPREPORTER_H_
#define UDPREPORTER_H_

#define UDP_REPORT_SECONDS 4

#include "../threading/LoopedThread.h"
#include "IReportable.h"
#include "UDPClient.h"

namespace KernelHive {

class UDPReporter : public LoopedThread {
public:
	UDPReporter(char* host, int port, IReportable *reportable);
	virtual ~UDPReporter();
	void executeLoopCycle();
private:
	UDPClient *udpClient;
	IReportable *reportable;
	void sendReport(int percentDone);
};

}

#endif /* UDPREPORTER_H_ */
