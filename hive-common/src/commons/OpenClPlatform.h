#ifndef KERNEL_HIVE_OPEN_CL_PLATFORM_H
#define KERNEL_HIVE_OPEN_CL_PLATFORM_H

#include <CL/cl.h>

#include "OpenClDevice.h"

namespace KernelHive {

	/**
	 * Represents an OpenCL platform.
	 */
	class OpenClPlatform {

	public:
		/** The separator character used in the information strings. */
		static const char DEVICES_INFO_SEPARATOR = '|';

		/**
		 * The default constructor.
		 */
		OpenClPlatform();

		/**
		 * The copy constructor.
		 *
		 * @param platform the platform
		 */
		OpenClPlatform(OpenClPlatform& platform);

		/**
		 * The destructor.
		 */
		virtual ~OpenClPlatform();

		/**
		 * Gets the GPU devices information.
		 *
		 * @return a string containing the GPU devices
		 * 		information
		 */
		static std::string getGpuDevicesInfo();

		/**
		 * Gets the CPU devices information.
		 *
		 * @return a string containing the CPU devices
		 * 		information
		 */
		static std::string getCpuDevicesInfo();

		/**
		 * Gets the OpenCL identifier of this platform.
		 *
		 * @return the cl_pltaform_id of this platform or NULL
		 * 		if the platform could not be initialized
		 */
		cl_platform_id getClPlatformId();

		/**
		 * Gets the GPU devices available on this platform.
		 *
		 * @return an array of GPU devices available on this platform
		 */
		OpenClDevice** getGpuDevices();

		/**
		 * Gets the number of GPU devices available on this platform.
		 *
		 * @return the number of GPU devices available on this platform
		 */
		uint32_t getGpuDevicesCount();

		/**
		 * Gets the CPU devices available on this platform.
		 *
		 * @return an array of CPU devices available on this platform
		 */
		OpenClDevice** getCpuDevices();

		/**
		 * Gets the number of CPU devices available on this platform.
		 *
		 * @return the number of CPU devices available on this platform
		 */
		uint32_t getCpuDevicesCount();

	private:
		/** The OpenCL ID of the platform. */
		cl_platform_id clPlatformId;

		/** The GPU devices available on this platform. */
		OpenClDevice** gpuDevices;

		/** The number of GPU devices available on this platform. */
		uint32_t gpuDevicesCount;

		/** The CPU devices available on this platform. */
		OpenClDevice** cpuDevices;

		/** The number of CPU devices available on this platform. */
		uint32_t cpuDevicesCount;

		/**
		 * Gets the information about provided OpenCL devices.
		 *
		 * @param devices an array of pointers to OpenCL devices
		 * @param count the number of given devices
		 * @return a string containing information about given devices
		 */
		static std::string getDevicesInfo(OpenClDevice** devices, uint32_t count);

		/**
		 * Initializes the devices on this platform.
		 */
		void initializeDevices(cl_device_type deviceType,
				OpenClDevice*** devices, uint32_t* numDevices);

		/**
		 * Deallocates the memory used to store devices of this
		 * platform.
		 */
		void cleanupDevices(OpenClDevice*** devices, uint32_t devicesCount);

		/**
		 * Gets the IDs of devices of the chosen type available
		 * on this platform. Will dynamically allocate the deviceIds
		 * array - deallocation should be handled by the caller.
		 *
		 * @param deviceType the type of devices to look for
		 * @param deviceIds the address of OpenCL device IDs array
		 * @param numDevices the address of the number of devices found
		 * 		variable
		 * @return the OpenCL error code of this operation
		 */
		cl_int getDeviceIds(cl_device_type deviceType,
				cl_device_id** deviceIds, cl_uint* numDevices);

	};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_OPEN_CL_PLATFORM_H */
