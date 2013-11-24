/*
 * UnitManager.h
 *
 *  Created on: 12-03-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#ifndef UNITMANAGER_H_
#define UNITMANAGER_H_

#include "ClusterProxy.h"
#include "SystemMonitor.h"
#include "network/TCPClientListener.h"
#include "network/TCPMessage.h"

namespace KernelHive {

class UnitManager : public TCPClientListener {
private:
	ClusterProxy *clusterProxy;
	SystemMonitor *systemMonitor;
public:
	UnitManager(char *clusterHostname, SystemMonitor* systemMonitor);
	virtual ~UnitManager();

	void listen();
	void onMessage(TCPMessage *message);
	void onConnected();
	int getId();
};

}

#endif /* UNITMANAGER_H_ */
