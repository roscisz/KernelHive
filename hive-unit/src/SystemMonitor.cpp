/*
 * SystemMonitor.cpp
 *
 *  Created on: 16-04-2013
 *      Author: Szymon Bultrowicz
 */

#include "SystemMonitor.h"

#include <cmath>
#include <fstream>
#include <sstream>
#include <stdio.h>
#include <unistd.h>

#include "commons/OpenClHost.h"
#include "commons/OpenClPlatform.h"
#include "Helpers.h"

using namespace std;

namespace KernelHive {

SystemMonitor::SystemMonitor() {
	id = -1;
	keepRunning = false;
	clusterInitRequest = false;
	hostStatus = NULL;
}

SystemMonitor::~SystemMonitor() {
	delete hostStatus;
}

HostStatus* SystemMonitor::createHostStatus() {
	if(hostStatus != NULL) {
		delete hostStatus;
	}

	list<GPUStatus*> gpus = list<GPUStatus*>();

	int cpuCores = strtol((new BashRunner(BashCommands::CPU_CORES_COMMAND))->execute(), NULL, 0);
	if(cpuCores == 0)
		throw std::string("Cannot detect any CPU core");

	short int openclDevicesCount = 0;
	short int gpuId = 0;
	OpenClHost* openClHost = OpenClHost::getInstance();
	OpenClPlatform** openClPlatforms = openClHost->getPlatforms();
	cl_uint platformsCount = openClHost->getPlatformsCount();
	for(int i = 0; i < platformsCount; i++) {
		printf("cpus: %d / gpus: %d\n", openClPlatforms[i]->getCpuDevicesCount(), openClPlatforms[i]->getGpuDevicesCount());
		for (int j = 0; j < openClPlatforms[i]->getCpuDevicesCount(); j++) {
			gpus.push_back(fillWithOpenCLDevices(openClPlatforms[i]->getCpuDevices()[j], gpuId++));
		}
		for (int j = 0; j < openClPlatforms[i]->getGpuDevicesCount(); j++) {
			gpus.push_back(fillWithOpenCLDevices(openClPlatforms[i]->getGpuDevices()[j], gpuId++));
		}
	}

	int memoryTotal = strtol((new BashRunner(BashCommands::TOTAL_MEMORY_STATS_COMMAND))->execute(), NULL, 0);
	printf("total devices: %d\n", gpus.size());
	hostStatus = new HostStatus(id, cpuCores, memoryTotal, gpus);
	return hostStatus;
}

GPUStatus* SystemMonitor::fillWithOpenCLDevices(OpenClDevice* openClDevice, short int id) {
		cl_device_id clId = openClDevice->getClDeviceId();
		cl_uint vendorId = openClDevice->getDeviceVendorId();
		GPUStatus* gpu = new GPUStatus();
		gpu->setId(id);
		gpu->setTotalMemory(openClDevice->getGlobalMemSize());
		printf("device: %s\n", openClDevice->getDeviceName().c_str());
		gpu->setName(openClDevice->getDeviceName());
		gpu->setVendorName(openClDevice->getDeviceVendor());
		gpu->setIdentifier(openClDevice->getIdentifier());
		gpu->setAvailable(openClDevice->getDeviceAvailability());
		return gpu;
}

HostStatus* SystemMonitor::getStatus() {
	return hostStatus;
}

void SystemMonitor::refreshStatus() {
	getCpuStats();
	getMemoryStats();
	getGpuStats();
}

void SystemMonitor::getCpuStats() {
	vector<unsigned int> workJiffies1(hostStatus->getCpuCount());
	vector<unsigned int> totalJiffies1(hostStatus->getCpuCount());
	getCurrentJiffies(&workJiffies1, &totalJiffies1);

	sleep(1);

	vector<unsigned int> workJiffies2(hostStatus->getCpuCount());
	vector<unsigned int> totalJiffies2(hostStatus->getCpuCount());
	getCurrentJiffies(&workJiffies2, &totalJiffies2);

	for(int cpuId = 0; cpuId < hostStatus->getCpuCount(); cpuId++) {
		double workJiffies = workJiffies2.at(cpuId) - workJiffies1.at(cpuId);
		double totalJiffies = totalJiffies2.at(cpuId) - totalJiffies1.at(cpuId);
		unsigned int cpuLoad = (unsigned int)round(10000.0 * workJiffies / totalJiffies);
		hostStatus->setCpuIdle(cpuId, 10000 - cpuLoad);
	}

	/*const char* buff = (new BashRunner(BashCommands::CPU_STATS_COMMAND))->execute();
	if(buff != NULL) {
		std::stringstream ss(buff);
		std::string line;
		for(int cpuId = 0; std::getline(ss, line) && cpuId < hostStatus->getCpuCount(); cpuId++) {
			unsigned int cpu = strtoul(line.c_str(), NULL, 0);
			hostStatus->setCpuIdle(cpuId, cpu);
		}
	}*/

	const char* buff = (new BashRunner(BashCommands::CPU_SPEED_COMMAND))->execute();
	unsigned int cpuSpeed;
	if(Helpers::parseString(buff, &cpuSpeed)) {
		hostStatus->setCpuSpeed(cpuSpeed);
	} else {
		printf("No CPU speed\n");
	}
}

void SystemMonitor::getCurrentJiffies(vector<unsigned int> *workJiffies, vector<unsigned int> *totalJiffies) {
	ifstream statFile ("/proc/stat");
	if(statFile.is_open()) {
		string line;
		string word;

		// omit first line - the global usage
		getline(statFile, line);

		// get jiffies for each core
		for(int cpuId = 0; cpuId < hostStatus->getCpuCount(); cpuId++) {
			getline(statFile, line);
			stringstream ss(line);
			int i = 0;
			unsigned int work = 0;
			unsigned int total = 0;
			while(getline(ss, word, ' ')) {
				if(i > 0) {
					int jiff = atoi(word.c_str());
					total += jiff;
					if(i < 4) {
						work += jiff;
					}
				}
				i++;
			}
			(*workJiffies)[cpuId] = work;
			(*totalJiffies)[cpuId] = total;
		}
	}
}

void SystemMonitor::getMemoryStats() {
	const char* buff;
	unsigned int unsignedValue;

	buff = (new BashRunner(BashCommands::TOTAL_MEMORY_STATS_COMMAND))->execute();
	if(Helpers::parseString(buff, &unsignedValue)) {
		hostStatus->setMemoryTotal(unsignedValue);
	}

	buff = (new BashRunner(BashCommands::FREE_MEMORY_STATS_COMMAND))->execute();
	if(Helpers::parseString(buff, &unsignedValue)) {
		hostStatus->setMemoryFree(unsignedValue);
	}
}

short int SystemMonitor::getId() {
	return id;
}

void SystemMonitor::setId(short int id) {
	this->id = id;
}

bool SystemMonitor::getClusterInitRequest() {
	return clusterInitRequest;
}

void SystemMonitor::setClusterInitRequest(bool clusterInitRequest) {
	this->clusterInitRequest = clusterInitRequest;
}

void SystemMonitor::getGpuStats() {
	list<GPUStatus*> gpuDevices = hostStatus->getGpuDevices();
	char command[256];
	for(list<GPUStatus*>::const_iterator it = gpuDevices.begin(); it != gpuDevices.end(); it++) {
		GPUStatus* gpu = *it;
		sprintf(command, BashCommands::NVIDIA_ID_COMMAND, gpu->getName().c_str());
		const char* buff = (new BashRunner(command))->execute();
		unsigned int id;
		if(Helpers::parseString(buff, &id)) {
			int usedMemory;
			sprintf(command, BashCommands::NVIDIA_MEMORY_USED_COMMAND, id);
			buff = (new BashRunner(command))->execute();
			if(Helpers::parseString(buff, &usedMemory)) {
				gpu->setUsedMemory(usedMemory);
			}

			short int gpuUsage;
			sprintf(command, BashCommands::NVIDIA_GPU_COMMAND, id);
			buff = (new BashRunner(command))->execute();
			if(Helpers::parseString(buff, &gpuUsage)) {
				gpu->setGpuUsage(gpuUsage);
			} else {
				gpu->setGpuUsage(-1);
			}

			short int fanSpeed;
			sprintf(command, BashCommands::NVIDIA_FAN_COMMAND, id);
			buff = (new BashRunner(command))->execute();
			if(Helpers::parseString(buff, &fanSpeed)) {
				gpu->setFanSpeed(fanSpeed);
			} else {
				gpu->setFanSpeed(-1);
			}
		}
	}
}

}
