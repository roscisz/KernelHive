#ifndef KERNEL_HIVE_OPEN_CL_EXCEPTION
#define KERNEL_HIVE_OPEN_CL_EXCEPTION

#include <string>
#include <CL/cl.h>

namespace KernelHive {

	/**
	 * An exception used for indicating OpenCL errors.
	 */
	class OpenClException {
	public:
		/**
		 * The constructor.
		 *
		 * @param message the error message of this exception
		 * @param openClErrorCode the OpenCL error code
		 */
		OpenClException(std::string& message, cl_int openClErrorCode);

		/**
		 * The destructor.
		 */
		virtual ~OpenClException();

		/**
		 * Gets the error message.
		 *
		 * @return the message
		 */
		std::string getMessage();

		/**
		 * Gets the OpenCL error code
		 *
		 * @return the OpenCL error code
		 */
		cl_int getOpenClErrorCode();

	private:
		/** The error message. */
		std::string message;

		/** The OpenCL error code. */
		cl_int openClErrorCode;
	};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_OPEN_CL_EXCEPTION */
