#ifndef KERNEL_HIVE_KERNEL_COMPILER_H
#define KERNEL_HIVE_KERNEL_COMPILER_H

#include <map>
#include <string>

#include "ExecutionContext.h"

namespace KernelHive {

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

private:
	/**	Holds the kernel source. */
	std::string sourceCode;

	/** Maps maps number to device identifier. */
	std::map<int, std::string> idMappings;

	/** An execution context, used to compile the kernel source. */
	ExecutionContext* executionContext;

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_KERNEL_COMPILER_H */
