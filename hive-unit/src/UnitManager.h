/*
 * UnitManager.h
 *
 *  Created on: 12-03-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#ifndef UNITMANAGER_H_
#define UNITMANAGER_H_

#include "ClusterProxy.h"

namespace KernelHive {

class UnitManager : IClusterListener {
private:
	ClusterProxy *clusterProxy;
public:
	UnitManager();
	virtual ~UnitManager();

	void listen();
	void onMessage(char *message);
};

}

#endif /* UNITMANAGER_H_ */
