/*
 * CyclicSystemMonitor.h
 *
 *  Created on: 17-04-2013
 *      Author: szymon
 */

#ifndef CYCLICSYSTEMMONITOR_H_
#define CYCLICSYSTEMMONITOR_H_

#include "network/UDPClient.h"
#include "threading/LoopedThread.h"
#include "HostStatusSerializer.h"
#include "SystemMonitor.h"

namespace KernelHive {

class CyclicSystemMonitor : public LoopedThread {
public:
	CyclicSystemMonitor(UDPClient* udpClient, unsigned int interval);
	virtual ~CyclicSystemMonitor();

	SystemMonitor* getSystemMonitor();

private:
	unsigned int interval;
	SystemMonitor* systemMonitor;
	HostStatusSerializer* serializer;
	UDPClient* udpClient;

	void sendInitialMessage();
	virtual void executeLoopCycle();
};

} /* namespace KernelHive */
#endif /* CYCLICSYSTEMMONITOR_H_ */
