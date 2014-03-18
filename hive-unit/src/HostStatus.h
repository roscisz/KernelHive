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
#ifndef HostStatus_H_
#define HostStatus_H_

#include <list>
#include <string>
#include "GPUStatus.h"

using namespace std;

namespace KernelHive {

class HostStatus {
public:
	HostStatus(short int id, short int cpuCount, int memoryTotal, list<GPUStatus*> gpuDevices);
	virtual ~HostStatus();

	short int getId();

	/**
	 * Returns total memory size [KB]
	 */
	int getMemoryTotal();

	/**
	 * Returns used memory size [KB]
	 */
	int getMemoryUsed();

	/**
	 * Returns percentage memory usage multiplied by 100 (max 100000, 57.3% - 5730)
	 */
	short int getCpuUsage(short int cpuId);

	/**
	 * Returns number of cores
	 */
	short int getCpuCount();

	/**
	 * Returns current speed of processor in MHz/s
	 */
	short int getCpuSpeed();

	list<GPUStatus*> getGpuDevices();
	void clearGpuDevices();

	void setMemoryTotal(int memoryTotal);
	void setMemoryFree(int memoryFree);
	void setCpuIdle(short int cpuId, short int cpuIdle);
	void setCpuSpeed(short int cpuSpeed);

private:
	short int id;

	int memoryTotal;
	int memoryFree;

	short int cpuCount;
	short int* cpuIdles;
	short int cpuSpeed;

	std::list<GPUStatus*> gpuDevices;
};

} /* namespace KernelHive */
#endif /* HostStatus_H_ */
