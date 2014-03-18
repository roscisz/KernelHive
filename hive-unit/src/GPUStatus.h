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
