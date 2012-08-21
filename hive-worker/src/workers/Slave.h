#ifndef KERNEL_HIVE_SLAVE_H
#define KERNEL_HIVE_SLAVE_H

namespace KernelHive {

/**
 * A worker which acts as a slave of the Master/Slave template.
 */
class Slave {

public:
	/**
	 * The constructor.
	 */
	Slave();

	/**
	 * The destructor.
	 */
	virtual ~Slave();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_SLAVE_H */
