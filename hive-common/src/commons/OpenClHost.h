/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
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
#ifndef KERNEL_HIVE_OPEN_CL_HOST_H
#define KERNEL_HIVE_OPEN_CL_HOST_H

#include <iostream>
#include <map>
#include <vector>
#include <string>

#include "OpenClPlatform.h"

namespace KernelHive {

	/** An alias for a list of devices. */
	typedef std::vector<OpenClDevice*> DevicesList;

	/** A type alias for the OpenCL devices map. */
	typedef std::map<std::string, DevicesList> DevicesMap;

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
		std::string getDevicesInfo();

		/**
		 * Attempts to lookup a device using the provided identifier.
		 *
		 * @param identifier the identifier of the device
		 * @return a pointer to the device if found, NULL otherwise
		 */
		OpenClDevice* lookupDevice(std::string identifier);

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
		static OpenClHost* instance;

		/** The OpenCL platforms on this host. */
		OpenClPlatform** platforms;

		/** The number of platforms on this host. */
		cl_uint platformsCount;

		/** A map which can be used to lookup OpenCL devices by their identifier. */
		DevicesMap devicesMap;

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
