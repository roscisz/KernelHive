#ifndef KERNEL_HIVE_DATA_MERGER_H
#define KERNEL_HIVE_DATA_MERGER_H

#include <string>

#include "BasicWorker.h"
#include "network/NetworkAddress.h"
#include "threading/SynchronizedBuffer.h"

namespace KernelHive {

/**
 * A worker which implements the dataMerger kernel logic.
 * Parameters currently expected as input:
 * <ul>
 *   <li>n - number of data packages</li>
 *   <li>n data identifiers, one for each package</li>
 * </ul>
 */
class DataMerger : public BasicWorker {

public:
	/**
	 * Initializes a new instance of DataMerger.
	 *
	 * @param clusterAddress the address of the KernelHive cluster
	 */
	DataMerger(char **argv);

	/**
	 * The destructor.
	 */
	virtual ~DataMerger();

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

	/** The number of data sources to merge from. */
	int datasCount;

	/** The identifier which can be used to download data for this worker. */
	std::string** dataIds;

	/** The data identifier in the integer number form. */
	int* dataIdsInt;

	/** A buffer for storing the result of calculations. */
	SynchronizedBuffer* resultBuffer;

	void cleanupResources();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_MERGER_H */
