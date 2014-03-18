/**
 * Copyright (c) 2014 Gdansk University of Technology
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
