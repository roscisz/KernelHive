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

private:
	/**	Holds the kernel source. */
	std::string sourceCode;

	/** Maps maps number to device identifier. */
	IdDevicesMap idMappings;

	/** An execution context, used to compile the kernel source. */
	ExecutionContext* executionContext;

	/**
	 * Initializes the device mapping for the compiler.
	 */
	void initDeviceMappings();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_KERNEL_COMPILER_H */
