#ifndef KERNEL_HIVE_EXECUTION_CONTEXT_H
#define KERNEL_HIVE_EXECUTION_CONTEXT_H

#include <CL/cl.h>
#include <map>

#include "OpenClDevice.h"

namespace KernelHive {

	/** The type to use for the meomry buffers. */
	typedef std::map<std::string, cl_mem> BufferMap;

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

		/**
		 * Creates a new device memory buffer. The buffer can be later
		 * referenced by the provided name.
		 *
		 * @param name the name ro use for the buffer
		 * @param size the size of the new buffer
		 * @param buffer OpenCL flags to use when creating this buffer
		 */
		void createBuffer(const std::string& name, size_t size, cl_mem_flags flags);

		/**
		 * Releases a buffer with the provided name.
		 *
		 * @param name the name of the buffer to release
		 */
		void releaseBuffer(const std::string& name);

	private:
		/** The OpenCL device to use for this context. */
		OpenClDevice device;

		/** The OpenCL context. */
		cl_context clContext;

		/** The OpenCL command queue. */
		cl_command_queue clCommandQueue;

		/** A map which holds all buffers used in this execution context. */
		BufferMap buffers;

		/** Initializes the OpenCL context. */
		void initOpenClContext();

		/** Initializes an OpenCL command queue. */
		void initOpenClCommandQueue();

		void releaseBuffers();
	};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_EXECUTION_CONTEXT_H */
