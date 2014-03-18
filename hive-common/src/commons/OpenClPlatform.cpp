/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
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
#include <iostream>
#include <sstream>

#include "OpenClPlatform.h"
#include "OpenClDevice.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	OpenClPlatform::OpenClPlatform() {
		clPlatformId = NULL;
		gpuDevices = NULL;
		gpuDevicesCount = 0;

		cl_int retVal = clGetPlatformIDs(1, &clPlatformId, NULL);
		if (retVal == CL_SUCCESS) {
			initializeDevices(CL_DEVICE_TYPE_GPU, &gpuDevices, &gpuDevicesCount);
			initializeDevices(CL_DEVICE_TYPE_CPU, &cpuDevices, &cpuDevicesCount);
		}
	}

	OpenClPlatform::OpenClPlatform(cl_platform_id platformId) {
		clPlatformId = platformId;
		initializeDevices(CL_DEVICE_TYPE_GPU, &gpuDevices, &gpuDevicesCount);
		initializeDevices(CL_DEVICE_TYPE_CPU, &cpuDevices, &cpuDevicesCount);
	}

	OpenClPlatform::OpenClPlatform(OpenClPlatform& platform) {
		this->clPlatformId = platform.getClPlatformId();
	}

	OpenClPlatform::~OpenClPlatform() {
		cleanupDevices(&gpuDevices, gpuDevicesCount);
	}


	cl_platform_id OpenClPlatform::getClPlatformId() {
		return this->clPlatformId;
	}

	OpenClDevice** OpenClPlatform::getGpuDevices() {
		return this->gpuDevices;
	}

	uint32_t OpenClPlatform::getGpuDevicesCount() {
		return this->gpuDevicesCount;
	}

	OpenClDevice** OpenClPlatform::getCpuDevices() {
		return this->cpuDevices;
	}

	uint32_t OpenClPlatform::getCpuDevicesCount() {
		return this->cpuDevicesCount;
	}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

	/*static*/ std::string OpenClPlatform::getDevicesInfo(OpenClDevice** devices,
			uint32_t devicesCount) {
		std::stringstream stream;

		stream << devicesCount;
		if (devicesCount > 0) {
			stream << DEVICES_INFO_SEPARATOR;
			for (uint32_t i = 0; i < devicesCount; i++) {
				stream << devices[i]->getDeviceInfo();
				if (i+1 < devicesCount) {
					stream << DEVICES_INFO_SEPARATOR;
				}
			}
		}

		return stream.str();
	}

	void OpenClPlatform::initializeDevices(cl_device_type deviceType,
			OpenClDevice*** devices, uint32_t* numDevices) {
		cl_device_id* deviceIds = NULL;
		cl_uint devicesCount;

		cl_int errorCode = getDeviceIds(deviceType, &deviceIds, &devicesCount);
		if (errorCode == CL_SUCCESS) {
			*numDevices = devicesCount;
			*devices = new OpenClDevice*[*numDevices];
			for (unsigned int i = 0; i < *numDevices; i++) {
				(*devices)[i] = new OpenClDevice(clPlatformId, deviceIds[i]);
			}
			if (deviceIds != NULL) {
				delete[] deviceIds;
			}
		} else {
			*devices = NULL;
			*numDevices = 0;
		}
	}

	void OpenClPlatform::cleanupDevices(OpenClDevice*** devices, uint32_t devicesCount) {
		// FIXME:
		/*
		for (unsigned int i = 0; i < devicesCount; i++) {
			delete *devices[i];
		}
		*/
		delete[] *devices;
	}

	cl_int OpenClPlatform::getDeviceIds(cl_device_type deviceType,
					cl_device_id** deviceIds, cl_uint* numDevices) {
		cl_int returnCode;

		returnCode = clGetDeviceIDs(clPlatformId, deviceType,
				NULL, NULL, numDevices);
		if (returnCode == CL_SUCCESS) {
			*deviceIds = new cl_device_id[*numDevices];
			returnCode = clGetDeviceIDs(clPlatformId, deviceType,
					*numDevices, *deviceIds, numDevices);
		}

		return returnCode;
	}

} /* namespace KernelHive */
