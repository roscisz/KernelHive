/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
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
#ifndef KERNEL_HIVE_KERNEL_COMPILER_H
#define KERNEL_HIVE_KERNEL_COMPILER_H

#include <map>
#include <string>

#include "ExecutionContext.h"
#include "OpenClDevice.h"

namespace KernelHive {

typedef std::map<int, OpenClDevice*> IdDevicesMap;

/**
 * A class used for test compilation of OpenCL kernels.
 */
class KernelCompiler {

public:
	/**
	 * Instantiates a new KernelComiler.
	 */
	KernelCompiler();

	/**
	 * The destructor - release resources used by this compiler.
	 */
	virtual ~KernelCompiler();

	/**
	 * Loads kernel source code from file.
	 *
	 * @param sourceFile a cstring containing the kernel source
	 */
	void loadSource(char *sourceFile);

	/**
	 * List device available on this host.
	 */
	void printDevices();

	/**
	 * Compile the source on provided device.
	 *
	 * @param the device to use
	 */
	bool compileOnDevice(int selection);

private:
	/**	Holds the kernel source. */
	std::string sourceCode;

	/** Maps maps number to device identifier. */
	IdDevicesMap idMappings;

	/** An execution context, used to compile the kernel source. */
	ExecutionContext* ctx;

	/**
	 * Initializes the device mapping for the compiler.
	 */
	void initDeviceMappings();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_KERNEL_COMPILER_H */
