/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
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
#include <CL/cl.h>
#include <cstdio>
#include <sstream>
#include <cstdio>

#include "OpenClHost.h"

#define USE_CPU
#define USE_GPU
#ifndef USE_CPU
	#define SINGLE_TYPE
#endif
#ifndef USE_GPU
	#define SINGLE_TYPE
#endif

namespace KernelHive {

// ========================================================================= //
// 							Static Members									 //
// ========================================================================= //

	OpenClHost* OpenClHost::instance;

	/*static*/ OpenClHost* OpenClHost::getInstance() {
		if(instance == NULL) {
			instance = new OpenClHost();
		}
		return instance;
	}

	/*static*/ std::string OpenClHost::getDevicesInfo() {
		std::stringstream stream;

		cl_uint counts = 0;
		cl_uint count = 0;
		OpenClDevice** devices;
		OpenClPlatform** platforms = getPlatforms();
		for (cl_uint i = 0; i < getPlatformsCount(); i++) {
#ifdef USE_GPU
			count = platforms[i]->getGpuDevicesCount();
			if (count > 0) {
				counts += count;
				devices = platforms[i]->getGpuDevices();
				for (cl_uint j = 0; j < count; j++) {
					stream << devices[j]->getDeviceInfo();
					if (j + 1 < count) {
						stream << OpenClPlatform::DEVICES_INFO_SEPARATOR;
					}
				}
			}
#endif
#ifdef USE_CPU
			count = platforms[i]->getCpuDevicesCount();
			if (count > 0) {
				counts += count;
				devices = platforms[i]->getCpuDevices();
				for (cl_uint j = 0; j < count; j++) {
					stream << devices[j]->getDeviceInfo();
					if (j + 1 < count) {
						stream << OpenClPlatform::DEVICES_INFO_SEPARATOR;
					}
				}
			}
#endif
#ifndef SINGLE_TYPE
			if (i + 1 < getPlatformsCount()) {
				stream << OpenClPlatform::DEVICES_INFO_SEPARATOR;
			}
#endif
		}

		std::stringstream outStream;
		outStream << counts;
		outStream << OpenClPlatform::DEVICES_INFO_SEPARATOR;
		outStream << stream.str();
		return outStream.str();
	}

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	OpenClHost::OpenClHost() {
		cl_uint count = getAvailablePlatformsCount();
		cl_platform_id* ids = new cl_platform_id[count];
		getAvailablePlatformIds(ids, count);
printf("OpenClHost init\n");
		platforms = new OpenClPlatform*[count];
		platformsCount = count;
		printf("platforms: %d\n", count);
		for (cl_uint i = 0; i < count; i++) {
			platforms[i] = new OpenClPlatform(ids[i]);
			OpenClDevice** devices = platforms[i]->getCpuDevices();
			printf("cpus: %d\n", platforms[i]->getCpuDevicesCount());
			for (cl_uint j = 0; j < platforms[i]->getCpuDevicesCount(); j++) {
				devicesMap[devices[j]->getIdentifier()].push_back(devices[j]);
			}
			devices = platforms[i]->getGpuDevices();
			for (cl_uint j = 0; j < platforms[i]->getGpuDevicesCount(); j++) {
				devicesMap[devices[j]->getIdentifier()].push_back(devices[j]);
			}
		}
		delete [] ids;
	}

	OpenClHost::~OpenClHost() {
		for (cl_uint i = 0; i < platformsCount; i++) {
			delete platforms[i];
		}
		delete [] platforms;
	}

	OpenClDevice* OpenClHost::lookupDevice(std::string identifier) {
		OpenClDevice* device = NULL;
		if (devicesMap.find(identifier) != devicesMap.end()) {
			DevicesList list = devicesMap[identifier];
			DevicesList::iterator iter;
			for (iter = list.begin(); iter != list.end(); iter++) {
				if ((*iter)->getDeviceAvailability() == CL_TRUE) {
					device = *iter;
					break;
				}
			}

		}
		return device;
	}

	OpenClPlatform** OpenClHost::getPlatforms() {
		return this->platforms;
	}

	cl_uint OpenClHost::getPlatformsCount() {
		return this->platformsCount;
	}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

	cl_uint OpenClHost::getAvailablePlatformsCount() {
		cl_uint count = 0;

		cl_int retVal = clGetPlatformIDs(0, NULL, &count);
		if (retVal != CL_SUCCESS) {
			// TODO throw exception
		}

		return count;
	}

	void OpenClHost::getAvailablePlatformIds(cl_platform_id* ids, cl_uint count) {
		cl_int retVal = clGetPlatformIDs(count, ids, &count);
		if (retVal != CL_SUCCESS) {
			// TODO throw exception
		}
	}

}
