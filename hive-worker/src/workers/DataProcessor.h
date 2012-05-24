#ifndef KERNEL_HIVE_DATA_PROCESSOR_H
#define KERNEL_HIVE_DATA_PROCESSOR_H

#include "commons/Worker.h"
#include "commons/ExecutionContext.h"
#include "threading/SynchronizedBuffer.h"
#include "DataDownloader.h"

namespace KernelHive {

/**
 * A worker which implements the dataProcessor kernel logic.
 * Parameters currently expected as input:
 * <ul>
 *   <li>data provider host</li>
 *   <li>data provider port</li>
 *   <li>kernel provider host</li>
 *   <li>kernel provider port</li>
 *   <li>number of dimensions to use - n</li>
 *   <li>n numbers specifying the offset in each dimension</li>
 *   <li>n numbers specifying the global workgroup size in each dimension</li>
 *   <li>n numners specifying the local workgroup size in each dimension</li>
 * </ul>
 */
class DataProcessor : public Worker {

public:
	/**
	 * Instantiates this worker with the cluster's network address.
	 *
	 * @param clusterAddress the cluster address
	 */
	DataProcessor(NetworkAddress* clusterAddress);

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

private:
	/** The address from which the data can be downloaded. */
	NetworkAddress* dataAddress;

	/** A network client used for downloading worker data. */
	DataDownloader* dataDownloader;

	/** The buffer for storing downloaded data. */
	SynchronizedBuffer* buffer;

	/** The execution context in which the data will be processed. */
	ExecutionContext* context;

	/**
	 * Initialize this data processor
	 */
	void init(char *const argv[]);

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_PROCESSOR_H */
