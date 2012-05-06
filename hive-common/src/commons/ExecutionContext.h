#ifndef KERNEL_HIVE_EXECUTION_CONTEXT_H
#define KERNEL_HIVE_EXECUTION_CONTEXT_H

#include <CL/cl.h>
#include <map>

#include "OpenClDevice.h"
#include "OpenClEvent.h"

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

		/**
		 * Performs a blocking write to a buffer previously created in this
		 * execution context.
		 *
		 * @param bufferName the name of the buffer to write to
		 * @param offset the offset from the beginning of the buffer, in bytes
		 * @param size the number of bytes to write to, in bytes
		 * @param ptr the pointer to data which should be written to the buffer
		 */
		void write(std::string bufferName, size_t offset, size_t size, const void* ptr);

		/**
		 * Enqueues a non-blocking write to a buffer previously created in this
		 * execution context.
		 *
		 * @param bufferName the name of the buffer to write to
		 * @param offset the offset from the beginning of the buffer, in bytes
		 * @param size the number of bytes to write to, in bytes
		 * @param ptr the pointer to data which should be written to the buffer
		 * @return an OpenClEvent object which represents this operation in the
		 * 		command queue
		 */
		OpenClEvent enqueueWrite(std::string bufferName, size_t offset, size_t size, const void* ptr);

		/**
		 * Performs a blocking read from a buffer previously created in this
		 * execution context.
		 *
		 * @param bufferName the name of the buffer to read from
		 * @param offset the offset from the beginning of the buffer, in bytes
		 * @param size the number of bytes to read, in bytes
		 * @param ptr the pointer to data to which buffer's data should be read
		 */
		void read(std::string bufferName, size_t offset, size_t size, void* ptr);

		/**
		 * Enqueues a non-blocking read from a buffer previously created in this
		 * execution context.
		 *
		 * @param bufferName the name of the buffer to read from
		 * @param offset the offset from the beginning of the buffer, in bytes
		 * @param size the number of bytes to read, in bytes
		 * @param ptr the pointer to data to which buffer's data should be read
		 * @return an OpenClEvent object which represents this operation in the
		 * 		command queue
		 */
		OpenClEvent enqueueRead(std::string bufferName, size_t offset, size_t size, void* ptr);

		/**
		 * Waits for provided OpenCL events to finish.
		 *
		 * @param eventsCount the number of events to wait for
		 * @param events the events to wait for
		 */
		void waitForEvents(size_t eventsCount, OpenClEvent* events);

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
