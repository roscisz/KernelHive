/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
