/*
 * UnitManager.h
 *
 *  Created on: 12-03-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#ifndef UNITMANAGER_H_
#define UNITMANAGER_H_

#include "ClusterProxy.h"
#include "network/TCPClientListener.h"

namespace KernelHive {

class UnitManager : public TCPClientListener {
private:
	ClusterProxy *clusterProxy;
public:
	UnitManager();
	virtual ~UnitManager();

	void listen();
	void onMessage(char *message);
	void onConnected();
};

}

#endif /* UNITMANAGER_H_ */
