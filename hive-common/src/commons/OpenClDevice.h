#ifndef KERNEL_HIVE_OPEN_CL_DEVICE_H
#define KERNEL_HIVE_OPEN_CL_DEVICE_H

#include <CL/cl.h>
#include <string>

#include "OpenClException.h"

namespace KernelHive {

	/**
	 * Represents an OpenCL device.
	 */
	class OpenClDevice {

	public:
		/** The size of the buffer which is used to query devices. */
		static const size_t DEVICE_INFO_QUERY_BUFFER_SIZE = 1024;

		/** The separator used in the device information string. */
		static const char DEVICE_INFO_SEPARATOR = ':';

		/**
		 * The constructor.
		 *
		 * @param clPlatformId an OpenCL id of a platform if the
		 * 		device
		 * @param clDeviceId the OpenCL ID of this device
		 */
		OpenClDevice(cl_platform_id clPlatformId, cl_device_id clDeviceId);

		/**
		 * The copy constructor.
		 */
		OpenClDevice(const OpenClDevice& device);

		/**
		 * The destructor.
		 */
		virtual ~OpenClDevice();

		/**
		 * Returns the identifier which can be used to lookup this device.
		 *
		 * @return the lookup identifier
		 */
		std::string getIdentifier();

		/**
		 * Returns the information about this device.
		 *
		 * @return information about this device in a string
		 * 		representation
		 */
		std::string getDeviceInfo();

		/**
		 * Gets the device name.
		 *
		 * @return the device name
		 */
		std::string getDeviceName();

		/**
		 * Gets the device vendor.
		 *
		 * @return the device vendor
		 */
		std::string getDeviceVendor();

		/**
		 * Gets device vendor unique id
		 */
		cl_uint getDeviceVendorId();

		/**
		 * Gets device availability.
		 *
		 * @return CL_TRUE if the device is available,
		 * 		CL_FALSE otherwise
		 */
		cl_bool getDeviceAvailability();

		/**
		 * Gets max compute units.
		 *
		 * @return max compute units
		 */
		cl_uint getMaxComputeUnits();

		/**
		 * Gets device's compute unit's max clock frequency.
		 *
		 * @return the max clock frequency
		 */
		cl_uint getMaxClockFrequency();

		/**
		 * Gets the global memory size in bytes.
		 *
		 * @return max global memory size in bytes
		 */
		cl_ulong getGlobalMemSize();

		/**
		 * Gets the local memory size in bytes.
		 *
		 * @return max local memory size in bytes
		 */
		cl_ulong getLocalMemSize();

		/**
		 * Gets the max work group size for this device.
		 *
		 * @return the max work group size
		 */
		size_t getMaxWorkGroupSize();

		/**
		 * Returns the OpenCL ID of this device.
		 *
		 * @return the cl_device_id of this device
		 */
		cl_device_id getClDeviceId();

		/**
		 * Returns an OpenCL id of this device's platform.
		 *
		 * @return an OpenCL id of this device's platform
		 */
		cl_platform_id getClPlatformId();

	private:
		/** An OpenCL id of this device's platform. */
		cl_platform_id clPlatformId;

		/** The OpenCL ID of this device. */
		cl_device_id clDeviceId;

		/** The KernelHive identifier of the device. */
		std::string identifier;

		/**
		 * Constructs the KernelHive identifier for this device.
		 */
		void constructIdentifier();

	};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_OPEN_CL_DEVICE_H */
