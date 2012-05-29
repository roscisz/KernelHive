#ifndef KERNEL_HIVE_KERNEL_HIVE_EXCEPTION_H
#define KERNEL_HIVE_KERNEL_HIVE_EXCEPTION_H

#include <string>

namespace KernelHive {

/**
 * A generic KernelHive exception class.
 */
class KernelHiveException {

public:
	/**
	 * Initialize this exception with an error message.
	 *
	 * @param message the message
	 */
	KernelHiveException(std::string& message);

	/**
	 * Initialize this exception with an error message.
	 *
	 * @param message the message
	 */
	KernelHiveException(const char* message);

	/**
	 * The desctructor.
	 */
	virtual ~KernelHiveException();

	/**
	 * Returns the error message of this exception.
	 */
	std::string getMessage();

private:
	/** The error message of this exception. */
	std::string message;

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_KERNEL_HIVE_EXCEPTION_H */
