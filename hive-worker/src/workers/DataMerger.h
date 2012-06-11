#ifndef KERNEL_HIVE_DATA_MERGER_H
#define KERNEL_HIVE_DATA_MERGER_H

#include "BasicWorker.h"
#include "network/NetworkAddress.h"

namespace KernelHive {

class DataMerger : public BasicWorker {

public:
	/**
	 * Initializes a new instance of DataMerger.
	 *
	 * @param clusterAddress the address of the KernelHive cluster
	 */
	DataMerger(NetworkAddress *clusterAddress);

	/**
	 * The destructor.
	 */
	virtual ~DataMerger();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_MERGER_H */
