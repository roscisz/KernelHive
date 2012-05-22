#include <CL/cl.h>
#include <string>
#include <sstream>

#include "OpenClDevice.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	OpenClDevice::OpenClDevice(cl_platform_id clPlatformId, cl_device_id clDeviceId) {
		this->clPlatformId = clPlatformId;
		this->clDeviceId = clDeviceId;
	}

	OpenClDevice::OpenClDevice(const OpenClDevice& device) {
		this->clPlatformId = device.clPlatformId;
		this->clDeviceId = device.clDeviceId;
	}

	OpenClDevice::~OpenClDevice() {
		// TODO Auto-generated destructor stub
	}

	std::string OpenClDevice::getDeviceInfo() {
		std::stringstream stream;

		stream << getDeviceName() << DEVICE_INFO_SEPARATOR;
		stream << getDeviceVendor() << DEVICE_INFO_SEPARATOR;
		stream << getDeviceAvailability() << DEVICE_INFO_SEPARATOR;
		stream << getMaxComputeUnits() << DEVICE_INFO_SEPARATOR;
		stream << getMaxClockFrequency() << DEVICE_INFO_SEPARATOR;
		stream << getGlobalMemSize() << DEVICE_INFO_SEPARATOR;
		stream << getLocalMemSize() << DEVICE_INFO_SEPARATOR;
		stream << getMaxWorkGroupSize();

		return stream.str();
	}

	std::string OpenClDevice::getIdentifier() {
		// TODO Implement an identifier which will actually be used..
		return getDeviceName();
	}

	std::string OpenClDevice::getDeviceName() {
		std::string deviceName;
		char buffer[DEVICE_INFO_QUERY_BUFFER_SIZE];

		// TODO Error handling
		cl_int retVal = clGetDeviceInfo(clDeviceId, CL_DEVICE_NAME,
				DEVICE_INFO_QUERY_BUFFER_SIZE, buffer, NULL);
		if (retVal == CL_SUCCESS) {
			deviceName = buffer;
		}

		return deviceName;
	}

	std::string OpenClDevice::getDeviceVendor() {
		std::string deviceVendor;
		char buffer[DEVICE_INFO_QUERY_BUFFER_SIZE];

		// TODO Error handling
		cl_int retVal = clGetDeviceInfo(clDeviceId, CL_DEVICE_VENDOR,
				DEVICE_INFO_QUERY_BUFFER_SIZE, buffer, NULL);
		if (retVal == CL_SUCCESS) {
			deviceVendor = buffer;
		}

		return deviceVendor;
	}

	cl_bool OpenClDevice::getDeviceAvailability() {
		cl_bool val;
		// TODO Error handling
		/*cl_int retVal = */clGetDeviceInfo(clDeviceId, CL_DEVICE_AVAILABLE,
				sizeof(cl_bool), &val, NULL);
		return val;
	}

	cl_uint OpenClDevice::getMaxComputeUnits() {
		cl_uint val;
		// TODO Error handling
		/*cl_int retVal = */clGetDeviceInfo(clDeviceId, CL_DEVICE_MAX_COMPUTE_UNITS,
				sizeof(cl_uint), &val, NULL);
		return val;
	}

	cl_uint OpenClDevice::getMaxClockFrequency() {
		cl_uint val;
		// TODO Error handling
		/*cl_int retVal = */clGetDeviceInfo(clDeviceId, CL_DEVICE_MAX_CLOCK_FREQUENCY,
				sizeof(cl_uint), &val, NULL);
		return val;
	}

	cl_ulong OpenClDevice::getGlobalMemSize() {
		cl_ulong val;
		// TODO Error handling
		/*cl_int retVal = */clGetDeviceInfo(clDeviceId, CL_DEVICE_GLOBAL_MEM_SIZE,
				sizeof(cl_ulong), &val, NULL);
		return val;
	}

	cl_ulong OpenClDevice::getLocalMemSize() {
		cl_ulong val;
		// TODO Error handling
		/*cl_int retVal = */clGetDeviceInfo(clDeviceId, CL_DEVICE_LOCAL_MEM_SIZE,
				sizeof(cl_ulong), &val, NULL);
		return val;
	}

	size_t OpenClDevice::getMaxWorkGroupSize() {
		size_t val;
		// TODO Error handling
		/*cl_int retVal = */clGetDeviceInfo(clDeviceId, CL_DEVICE_MAX_WORK_GROUP_SIZE,
				sizeof(size_t), &val, NULL);
		return val;
	}

	cl_platform_id OpenClDevice::getClPlatformId() {
		return this->clPlatformId;
	}

	cl_device_id OpenClDevice::getClDeviceId() {
		return this->clDeviceId;
	}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

} /* namespace KernelHive */
