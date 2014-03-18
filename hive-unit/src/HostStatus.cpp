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
