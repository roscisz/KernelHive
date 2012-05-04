#ifndef KERNEL_HIVE_EXECUTION_CONTEXT_H
#define KERNEL_HIVE_EXECUTION_CONTEXT_H

#include <CL/cl.h>

#include "OpenClDevice.h"

namespace KernelHive {

	/**
	 * Represents an execution context in which a kernel runs.
	 * Must be initialized with a device to execute on, allows to
	 * create and use memory buffers on the device and compile and
	 * run an OpenCL kernel.
	 */
	class ExecutionContext {

	public:
		/**
		 * Initializes this execution context.
		 *
		 * @param device an OpenCL device to use for executing
		 * 		kernels
		 */
		ExecutionContext(const OpenClDevice& device);

		/**
		 * The destructor.
		 */
		virtual ~ExecutionContext();

	private:
		/** The OpenCL device to use for this context. */
		OpenClDevice device;

		/** The OpenCL context. */
		cl_context clContext;

		/** The OpenCL command queue. */
		cl_command_queue clCommandQueue;

		/** Initializes the OpenCL context. */
		void initOpenClContext();

		/** Initializes an OpenCL command queue. */
		void initOpenClCommandQueue();

	};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_EXECUTION_CONTEXT_H */
