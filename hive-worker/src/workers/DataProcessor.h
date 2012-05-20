#ifndef KERNEL_HIVE_DATA_PROCESSOR_H
#define KERNEL_HIVE_DATA_PROCESSOR_H

#include "commons/Worker.h"

namespace KernelHive {

/**
 * A worker which implements the dataProcessor kernel logic.
 */
class DataProcessor : public Worker {

public:
	/**
	 * Instantiates this worker with the cluster's network address.
	 *
	 * @param clusterAddress the cluster address
	 */
	DataProcessor(NetworkAddress * clusterAddress);

	/**
	 * The processing logic should be implemented in this method.
	 *
	 * @param argv the parameters passed to the worker by the KernelHive
	 * 		system
	 */
	void work(char *const argv[]);

	/**
	 * Deallocates resources used by this data processor.
	 */
	virtual ~DataProcessor();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_PROCESSOR_H */
