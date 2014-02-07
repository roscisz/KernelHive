#ifndef KERNEL_HIVE_DATA_PROCESSOR_H
#define KERNEL_HIVE_DATA_PROCESSOR_H

#include "BasicWorker.h"
#include "threading/SynchronizedBuffer.h"
#include "../communication/DataDownloaderTCP.h"
#include "../communication/DataDownloaderGridFs.h"

namespace KernelHive {

/**
 * A worker which implements the dataProcessor kernel logic.
 * Parameters currently expected as input:
 * <ul>
 *   <li>input data host</li>
 *   <li>input data port</li>
 *   <li>input data identifier</li>
 *   <li>output data host</li>
 *   <li>output data port</li>
 * </ul>
 */
class DataProcessor : public BasicWorker {

public:
	/**
	 * Instantiates this worker with the cluster's network address.
	 *
	 * @param clusterAddress the cluster address
	 */
	DataProcessor(char **argv);

	/**
	 * Deallocates resources used by this data processor.
	 */
	virtual ~DataProcessor();

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
	 * Performs DataProcessor specific initialization.
	 *
	 * @param argv the parameters passed to this worker, parameter offset
	 * 		is shifted to DataProcessor specific parameters
	 */
	void initSpecific(char *const argv[]);

private:
	/** The name of the Kernel to use by the DataProcessor worker. */
	static const char* KERNEL;

	/** The address from which the data can be downloaded. */
	NetworkAddress* inputDataAddress;

	/** The identifier which can be used to download data for this worker. */
	std::string dataId;

	/** The data identifier in the integer number form. */
	int dataIdInt;

	/** The address from which the data can be downloaded. */
	NetworkAddress* outputDataAddress;

	/** A buffer for storing the result of calculations. */
	SynchronizedBuffer* resultBuffer;

protected:
	/**
	 * Cleans up any resources allocated by this DataProcessor.
	 */
	void cleanupResources();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_PROCESSOR_H */
