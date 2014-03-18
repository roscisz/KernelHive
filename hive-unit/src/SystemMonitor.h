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
#ifndef SYSTEMMONITOR_H_
#define SYSTEMMONITOR_H_

#include <list>
#include <vector>
#include "commons/BashRunner.h"
#include "HostStatus.h"
#include "BashCommands.h"
#include "commons/OpenClDevice.h"

using namespace std;

namespace KernelHive {

class SystemMonitor {
public:
	SystemMonitor();
	virtual ~SystemMonitor();

	HostStatus* createHostStatus();
	HostStatus* getStatus();
	void refreshStatus();

	short int getId();
	void setId(short int id);

	bool getClusterInitRequest();
	void setClusterInitRequest(bool clusterInitRequest);

private:

	bool keepRunning;
	bool clusterInitRequest;
	short int id;
	HostStatus* hostStatus;

	void getCpuStats();
	void getMemoryStats();
	void getGpuStats();

	GPUStatus* fillWithOpenCLDevices(OpenClDevice* openClDevice, short int id);

	void getCurrentJiffies(vector<unsigned int> *workJiffies, vector<unsigned int> *totalJiffies);
};

}

#endif /* SYSTEMMONITOR_H_ */
