#ifndef KERNEL_HIVE_DATA_PARTITIONER_H
#define KERNEL_HIVE_DATA_PARTITIONER_H

#include "BasicWorker.h"
#include "threading/SynchronizedBuffer.h"

namespace KernelHive {

/**
 * A worker which implements the dataPartitioner kernel logic.
 * Parameters currently expected as input:
 * <ul>
 *   <li>input data host</li>
 *   <li>input data port</li>
 *   <li>input data identifier</li>
 *   <li>n - number of parts to split to</li>
 *   <li>n pairs of output data host and port, one for each part after splitting</li>
 * </ul>
 */
class DataPartitioner : public BasicWorker {

public:
	DataPartitioner(char **argv);

	virtual ~DataPartitioner();

protected:
	/**
	 * Gets the name of the kernel to use for calculations.
	 *
	 * @return the kernel name
	 */
	const char* getKernelName();

	/**
	 * The processing logic should be implemented in this method.
	 */
	void workSpecific();

	/**
	 * Performs DataMerger specific initialization.
	 *
	 * @param argv the parameters passed to this worker, parameter offset
	 * 		is shifted to DataMerger specific parameters
	 */
	void initSpecific(char *const argv[]);

private:
	/** The name of the kernel to use for calcaulations. */
	static const char* KERNEL;

	/** The number of parts to split input data to. */
	int partsCount;

	/** The total size of data received from all sources. */
	size_t totalDataSize;

	/** The identifier which can be used to download data for this worker. */
	std::string dataId;

	/** The data identifier in the integer number form. */
	int dataIdInt;

	/** Buffers array for storing data parts. */
	SynchronizedBuffer** resultBuffers;

	/** Cleanup resources used by this Partitioner worker. */
	void cleanupResources();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_PARTITIONER_H */
