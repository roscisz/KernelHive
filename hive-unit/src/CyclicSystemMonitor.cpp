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
#include "CyclicSystemMonitor.h"
#include <unistd.h>

namespace KernelHive {

CyclicSystemMonitor::CyclicSystemMonitor(UDPClient* udpClient, unsigned int interval) {
	this->interval = interval;
	systemMonitor = new SystemMonitor();
	serializer = new HostStatusSerializer();
	this->udpClient = udpClient;
}

CyclicSystemMonitor::~CyclicSystemMonitor() {
	// TODO Auto-generated destructor stub
}

void CyclicSystemMonitor::executeLoopCycle() {
	if(systemMonitor->getId() < 0) {
		printf("Waiting for ID...");
		usleep(1000 * interval);
	}

	if(systemMonitor->getClusterInitRequest()) {
		systemMonitor->createHostStatus();
		sendInitialMessage();
		systemMonitor->setClusterInitRequest(false);
	} else {
		systemMonitor->refreshStatus();
	}

	HostStatus* status = systemMonitor->getStatus();
	ByteBuffer* message = serializer->serializeToSequentialMessage(status);
	udpClient->sendMessage((char*)message->getBuffer(), message->getSize());

	/*printf("CPU:");
	for(int i = 0; i < status->getCpuCount(); i++) {
		printf(" %d.%d%%", status->getCpuUsage(i)/100, status->getCpuUsage(i)%100);
	}
	printf(" MEM: %dKB/%dKB", status->getMemoryUsed(), status->getMemoryTotal());
	list<GPUStatus*> gpuDevices = status->getGpuDevices();
	printf(" GPUS: %d", gpuDevices.size());
	for(list<GPUStatus*>::const_iterator it = gpuDevices.begin();
			it != gpuDevices.end(); it++) {
		GPUStatus* gpu = *it;
		printf(" GPU:%s %dMB %hd%% %hd%%", gpu->getName().c_str(), gpu->getUsedMemory(),
				gpu->getGpuUsage(), gpu->getFanSpeed());
	}
	printf("\n");
	message->printHex();*/

	usleep(1000 * interval);
}

SystemMonitor* CyclicSystemMonitor::getSystemMonitor() {
	return systemMonitor;
}

void CyclicSystemMonitor::sendInitialMessage() {
	HostStatus* hostStatus = systemMonitor->getStatus();
	HostStatusSerializer* serializer = new HostStatusSerializer();
	ByteBuffer* initialMessage = serializer->serializeToInitialMessage(hostStatus);
	initialMessage->printHex();
	initialMessage->printBinary();

	string initial2 = serializer->serializeToStringInitialMessage(hostStatus);

	udpClient->sendMessage((char*)initialMessage->getBuffer(), initialMessage->getSize());
}

} /* namespace KernelHive */
