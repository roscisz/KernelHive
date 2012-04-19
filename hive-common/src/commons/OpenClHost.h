#ifndef KERNEL_HIVE_OPEN_CL_HOST_H
#define KERNEL_HIVE_OPEN_CL_HOST_H

#include <iostream>

#include "OpenClPlatform.h"

namespace KernelHive {

	/**
	 * Represents an OpenCL host. A host can have
	 * many OpenCL platforms.
	 */
	class OpenClHost {

	public:
		/**
		 * Instantiates a new OpenCL host.
		 */
		OpenClHost();

		/**
		 * Cleans up resources allocated by this
		 * OpenCL host.
		 */
		virtual ~OpenClHost();

		/**
		 * Gets the singleton instance of this OpenCL host.
		 *
		 * @return the OpenCL host instance
		 */
		static OpenClHost* getInstance();

		/**
		 * Gets the devices information.
		 *
		 * @return a string containing the GPU devices
		 * 		information
		 */
		static std::string getDevicesInfo();

		/**
		 * Gets the platforms available on this host.
		 *
		 * @return platforms available on this host
		 */
		OpenClPlatform** getPlatforms();

		/**
		 * Gets the number of platforms available on this host.
		 *
		 * @return the number of platforms available on this host
		 */
		cl_uint getPlatformsCount();

	private:
		static OpenClHost instance;

		/** The OpenCL platforms on this host. */
		OpenClPlatform** platforms;

		/** The number of platforms on this host. */
		cl_uint platformsCount;

		/**
		 * Gets the number of platforms available on this host.
		 *
		 * @return the count of platforms available
		 */
		cl_uint getAvailablePlatformsCount();

		/**
		 * Gets the IDs of available OpenCL platforms.
		 *
		 * @param ids the initialized IDs array
		 * @param count the number of IDs to fetch
		 */
		void getAvailablePlatformIds(cl_platform_id* ids, cl_uint count);

	};

}

#endif /* KERNEL_HIVE_OPEN_CL_HOST_H */
