/*
 * HostStatus.h
 *
 *  Created on: 16-04-2013
 *      Author: szymon
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
