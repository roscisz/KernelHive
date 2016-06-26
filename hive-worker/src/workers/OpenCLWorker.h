/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
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
#ifndef KERNEL_HIVE_BASIC_WORKER_H
#define KERNEL_HIVE_BASIC_WORKER_H

#include <map>
#include <vector>

#include "commons/OpenClDevice.h"
#include "commons/ExecutionContext.h"
#include "threading/SynchronizedBuffer.h"
#include "BasicWorker.h"

#define PreviewObject struct PreviewObjectStruct

namespace KernelHive {

struct PreviewObjectStruct {
	float f1;
	float f2;
	float f3;
};
/**
 * A a base class for open cl worker types.
 */
class OpenCLWorker: public BasicWorker {

public:
	/**
	 * Initializes this OpenCLWorker instance.
	 *
	 * @param clusterAddress the address of the KernelHive cluster.
	 */
	OpenCLWorker(char **argv);

	/**
	 * The destructor.
	 */
	virtual ~OpenCLWorker();

	/**
	 * The processing logic should be implemented in this method.
	 *
	 * @param argv the parameters passed to the worker by the KernelHive
	 * 		system
	 */
	void work(char *const argv[]);

protected:
	/** The execution context name of the input data buffer. */
	static const char* INPUT_BUFFER;

	/** The execution context name of the output data buffer. */
	static const char* OUTPUT_BUFFER;

	/** The execution context name of the preview data buffer. */
	static const char* PREVIEW_BUFFER;

	/** The address from which the kernel can be downloaded. */
	NetworkAddress* kernelAddress;

	/** The kernel identifier. */
	std::string deviceId;

	/** The number of dimensions to use for kernel execution. */
	unsigned int numberOfDimensions;

	/** An array of offsets in each dimensions. */
	size_t* dimensionOffsets;

	/** Global workgroup sizes in each dimension. */
	size_t* globalSizes;

	/** Local workgroup sizes in each dimension. */
	size_t* localSizes;

	/** Defines the size of the output that will be produced by this worker. */
	size_t outputSize;

	/** The OpenCL device which will be used for execution. */
	OpenClDevice* device;

	/** The execution context in which the data will be processed. */
	ExecutionContext* context;

	/** The identifier which can be used to download the kernel. */
	std::string kernelDataId;

	/**
	 * Performs initialization specific to a worker subclassing this.
	 *
	 * @param argv the parameters specific for a given OpenCLWorker subclass
	 */
	virtual void initSpecific(char *const argv[]) = 0;

	/**
	 * The method in which a concrete OpenCLWorker implementation should perform
	 * it's processing logic.
	 */
	virtual void workSpecific() = 0;

	/**
	 * Returns the name of the kernel to use for calculations.
	 *
	 * @return the name of the kernel to use.
	 */
	virtual const char* getKernelName() = 0;

private:
	/**
	 * A generic method initializing a OpenCLWorker.
	 *
	 * @param argv the parameters passed to the worker
	 */
	void init(char *const argv[]);

	/**
	 * Deallocates resources used by this worker.
	 */
	void deallocateResources();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_BASIC_WORKER_H */
