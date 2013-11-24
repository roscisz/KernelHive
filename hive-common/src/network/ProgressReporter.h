/*
 * UDPReporter.h
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#ifndef PROGRESSREPORTER_H_
#define PROGRESSREPORTER_H_

#include "../network/NetworkAddress.h"
#include "../threading/SynchronizedBuffer.h"
#include "IReportable.h"
#include "UDPClient.h"

namespace KernelHive {

class ProgressReporter {
public:
	ProgressReporter(int jobID, NetworkAddress *serverAddress);
	virtual ~ProgressReporter();
	void sendReport(SynchronizedBuffer *buffer);
private:
	int jobID;
	UDPClient *udpClient;
};

}

#endif /* PROGRESSREPORTER_H_ */
