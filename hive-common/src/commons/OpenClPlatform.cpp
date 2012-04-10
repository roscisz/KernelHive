#include <CL/cl.h>
#include <iostream>
#include <sstream>

#include "OpenClPlatform.h"
#include "OpenClDevice.h"

namespace KernelHive {

	/*static*/ std::string OpenClPlatform::getGpuDevicesInfo() {
		OpenClPlatform platform;
		return getDevicesInfo(platform.getGpuDevices(), platform.getGpuDevicesCount());
	}

	/*static*/ std::string OpenClPlatform::getCpuDevicesInfo() {
		OpenClPlatform platform;
		return getDevicesInfo(platform.getCpuDevices(), platform.getCpuDevicesCount());
	}

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
				*devices[i] = new OpenClDevice(deviceIds[i]);
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
		for (unsigned int i = 0; i < devicesCount; i++) {
			delete *devices[i];
		}
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
