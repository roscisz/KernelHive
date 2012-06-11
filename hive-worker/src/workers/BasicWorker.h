#ifndef KERNEL_HIVE_BASIC_WORKER_H
#define KERNEL_HIVE_BASIC_WORKER_H

#include "commons/Worker.h"
#include "commons/OpenClDevice.h"
#include "commons/ExecutionContext.h"
#include "threading/SynchronizedBuffer.h"

namespace KernelHive {

/**
 * A a base class for basic worker types.
 *
 * Parameters currently expected as input:
 * <ul>
 *   <li>data provider host</li>
 *   <li>data provider port</li>
 *   <li>kernel provider host</li>
 *   <li>kernel provider port</li>
 *   <li>device identifier</li>
 *   <li>number of dimensions to use - n</li>
 *   <li>n numbers specifying the offset in each dimension</li>
 *   <li>n numbers specifying the global workgroup size in each dimension</li>
 *   <li>n numbers specifying the local workgroup size in each dimension</li>
 *   <li>kernel code identifier</li>
 * </ul>
 */
class BasicWorker : public Worker {

public:
	/**
	 * Initializes this BasicWorker instance.
	 *
	 * @param clusterAddress the address of the KernelHive cluster.
	 */
	BasicWorker(NetworkAddress* clusterAddress);

	/**
	 * The destructor.
	 */
	virtual ~BasicWorker();

	/**
	 * The processing logic should be implemented in this method.
	 *
	 * @param argv the parameters passed to the worker by the KernelHive
	 * 		system
	 */
	void work(char *const argv[]);

protected:
	/** The address from which the data can be downloaded. */
	NetworkAddress* dataAddress;

	/** The address from which the kernel can be downloaded. */
	NetworkAddress* kernelAddress;

	/** A buffer used for storing the downloaded kernel. */
	SynchronizedBuffer* kernelBuffer;

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

	/** The OpenCL device which will be used for execution. */
	OpenClDevice* device;

	/** The execution context in which the data will be processed. */
	ExecutionContext* context;

	/** The identifier which can be used to download the kernel. */
	std::string kernelDataId;

	/**
	 * Performs initialization specific to a worker subclassing this.
	 *
	 * @param argv the parameters specific for a given BasicWorker subclass
	 */
	virtual void initSpecific(char *const argv[]) = 0;

	/**
	 * The method in which a concrete BasicWorker implementation should perform
	 * it's processing logic.
	 */
	virtual void workSpecific() = 0;

private:
	/**
	 * A generic method initializing a BasicWorker.
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
