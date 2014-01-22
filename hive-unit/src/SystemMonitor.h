/*
 * SystemMonitor.h
 *
 *  Created on: 16-04-2013
 *      Author: Szymon Bultrowicz
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
