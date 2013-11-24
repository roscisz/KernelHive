/*
 * HostStatus.cpp
 *
 *  Created on: 16-04-2013
 *      Author: szymon
 */

#include "HostStatus.h"
#include <cstdlib>
#include <cstdio>

using namespace std;

namespace KernelHive {

HostStatus::HostStatus(short int id, short int cpuCount, int memoryTotal, list<GPUStatus*> gpuDevices) {
	this->id = id;
	printf("HostStatus : %hd\n", id);
	this->cpuCount = cpuCount;
	cpuIdles = new short int[cpuCount];
	this->gpuDevices = gpuDevices;
	this->memoryTotal = memoryTotal;
	memoryFree = -1;
	cpuSpeed = -1;
}

HostStatus::~HostStatus() {
	delete[] cpuIdles;
	clearGpuDevices();
}

void HostStatus::clearGpuDevices() {
	for(list<GPUStatus*>::const_iterator it = gpuDevices.begin(); it != gpuDevices.end(); it++) {
		delete *it;
	}
	gpuDevices.clear();
}

short int HostStatus::getId() {
	return id;
}

short int HostStatus::getCpuCount() {
	return cpuCount;
}

short int HostStatus::getCpuUsage(short int cpuId) {
	short int cpuIdle = cpuIdles[cpuId];
	if(cpuIdle > 10000)
		return 0;
	return 10000 - cpuIdle;
}

int HostStatus::getMemoryTotal() {
	return memoryTotal;
}

int HostStatus::getMemoryUsed() {
	if(memoryFree > memoryTotal)
		return 0;
	return memoryTotal - memoryFree;
}

short int HostStatus::getCpuSpeed() {
	return cpuSpeed;
}

list<GPUStatus*> HostStatus::getGpuDevices() {
	return gpuDevices;
}

void HostStatus::setCpuIdle(short int cpuId, short int cpuIdle) {
	cpuIdles[cpuId] = cpuIdle;
}

void HostStatus::setMemoryTotal(int memoryTotal) {
	this->memoryTotal = memoryTotal;
}

void HostStatus::setMemoryFree(int memoryFree) {
	this->memoryFree = memoryFree;
}

void HostStatus::setCpuSpeed(short int cpuSpeed) {
	this->cpuSpeed = cpuSpeed;
}

} /* namespace KernelHive */
