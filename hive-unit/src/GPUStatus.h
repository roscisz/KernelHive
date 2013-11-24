/*
 * GPUStatus.h
 *
 *  Created on: 17-04-2013
 *      Author: szymon
 */

#ifndef GPUDEVICE_H_
#define GPUDEVICE_H_

#include <string>

using namespace std;

namespace KernelHive {

class GPUStatus {
public:
	GPUStatus();
	virtual ~GPUStatus();

	short int getId();
	short int getGpuUsage();
	int getTotalMemory();
	int getUsedMemory();
	string getName();
	string getVendorName();
	string getIdentifier();
	short int getFanSpeed();
	bool isAvailable();

	void setId(short int id);
	void setGpuUsage(short int gpuUsage);
	void setTotalMemory(int totalMemory);
	void setUsedMemory(int usedMemory);
	void setName(string name);
	void setVendorName(string vendorName);
	void setIdentifier(string identifier);
	void setFanSpeed(short int fanSpeed);
	void setAvailable(bool isAvailable);

private:
	short int id;
	string vendorName;
	string name;
	string identifier;
	int totalMemory;
	int usedMemory;
	short int gpuUsage;
	short int fanSpeed;
	bool available;
};

} /* namespace KernelHive */
#endif /* GPUDEVICE_H_ */
