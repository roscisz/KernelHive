#include <CL/cl.h>
#include <sstream>

#include "OpenClHost.h"

namespace KernelHive {

// ========================================================================= //
// 							Static Members									 //
// ========================================================================= //

	OpenClHost OpenClHost::instance;

	/*static*/ OpenClHost* OpenClHost::getInstance() {
		return &instance;
	}

	/*static*/ std::string OpenClHost::getDevicesInfo() {
		std::stringstream stream;

		cl_uint counts = 0;
		cl_uint count;
		OpenClDevice** devices;
		OpenClPlatform** platforms = instance.getPlatforms();
		for (cl_uint i = 0; i < instance.getPlatformsCount(); i++) {
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
			if (i + 1 < instance.getPlatformsCount()) {
				stream << OpenClPlatform::DEVICES_INFO_SEPARATOR;
			}
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

		platforms = new OpenClPlatform*[count];
		platformsCount = count;
		for (cl_uint i = 0; i < count; i++) {
			platforms[i] = new OpenClPlatform(ids[i]);
		}
		delete [] ids;
	}

	OpenClHost::~OpenClHost() {
		for (cl_uint i = 0; i < platformsCount; i++) {
			delete platforms[i];
		}
		delete [] platforms;
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
