#ifndef KERNEL_HIVE_MASTER_H
#define KERNEL_HIVE_MASTER_H

namespace KernelHive {

/**
 * A worker which acts as a master of the Master/Slave paradigm.
 */
class Master {

public:
	/**
	 * The constructor.
	 */
	Master();

	/**
	 * The destructor.
	 */
	virtual ~Master();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_MASTER_H */
