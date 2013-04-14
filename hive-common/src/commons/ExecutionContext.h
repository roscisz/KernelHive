#ifndef KERNEL_HIVE_EXECUTION_CONTEXT_H
#define KERNEL_HIVE_EXECUTION_CONTEXT_H

#include <CL/cl.h>
#include <map>

#include "OpenClDevice.h"
#include "OpenClEvent.h"

namespace KernelHive {

	/** The type to use for the meomry buffers. */
	typedef std::map<std::string, cl_mem> BufferMap;

	/** The type to use for the kernels cache. */
	typedef std::map<std::string, cl_kernel> KernelMap;

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
		 * Gets the raw OpenCL buffer allocated in this execution context.
		 *
		 * @param name the name of the buffer to fetch
		 * @return the buffer of provided name or NULL if none was found
		 */
		cl_mem getRawBuffer(const std::string& name);

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
		 * Waits for provided OpenCL events to finish, blocking.
		 *
		 * @param eventsCount the number of events to wait for
		 * @param events the events to wait for
		 */
		void waitForEvents(size_t eventsCount, OpenClEvent** events);

		/**
		 * Waits for provided OpenCL event to finish, blocking.
		 *
		 * @param event the event to wait for
		 */
		void waitForEvent(OpenClEvent* event);

		void waitForEvent(OpenClEvent** event);

		/**
		 * Build a new program to use by this execution context.
		 * Calling this function will cause any previous kernels and/or
		 * programs to be released.
		 *
		 * @param source the source code string of the program to build
		 */
		void buildProgramFromSource(std::string source);

		/**
		 * Build a new program to use by this execution context.
		 * Calling this function will cause any previous kernels and/or
		 * programs to be released.
		 *
		 * @param source the source code string of the program to build
		 * @param sourceLength the length of the source to build
		 */
		void buildProgramFromSource(const char* source, size_t sourceLength);

		/**
		 * Prepares a kernel which should be executed next.
		 *
		 * @param kernelName the name of the kernel which should be prepared
		 */
		void prepareKernel(std::string kernelName);

		/**
		 * Prepares a kernel which should be executed next.
		 *
		 * @param kernelName the name of the kernel which should be prepared
		 */
		void prepareKernel(const char* kernelName);

		/**
		 * Sets the argument for the current kernel.
		 *
		 * @param index the index of the argument. Arguments are numbered
		 * 		from 0 to n-1, from left to right
		 * @param size the size of the argument being set
		 * @param value the value of the argument
		 */
		void setValueArg(cl_uint index, size_t size, const void* value);

		/**
		 * Sets an OpenCL buffer allocated in this context as a kernel argument.
		 *
		 * @param index the kernel argument index to use
		 * @param bufferName the name of the buffer to use
		 */
		void setBufferArg(cl_uint index, const char* bufferName);

		/**
		 * Executes a currently set-up kernel in this execution context. The method
		 * blocks until the execution has finished.
		 *
		 * @param numDimensions the number of dimensions to use for this kernel
		 * @param globalWorkOffset specifies the numDimensions offsets of the
		 * 		global work item IDs in each dimension
		 * @param globalWorkSize specifies the numDimensions sizes of the total
		 * 		numbers of work-items which will execute in each dimension
		 * @param localWorkSize defines the number of work-items in each
		 * 		dimension in a single work group
		 */
		void executeKernel(cl_uint numDimensions, const size_t* globalWorkOffset,
				const size_t* globalWorkSize, const size_t* localWorkSize);

		/**
		 * Executes a currently set-up kernel in this execution context. The method
		 * returns an OpenClEvent object which can be used to wait for it to finish
		 * or check it's status.
		 *
		 * @param numDimensions the number of dimensions to use for this kernel
		 * @param globalWorkOffset specifies the numDimensions offsets of the
		 * 		global work item IDs in each dimension
		 * @param globalWorkSize specifies the numDimensions sizes of the total
		 * 		numbers of work-items which will execute in each dimension
		 * @param localWorkSize defines the number of work-items in each
		 * 		dimension in a single work group
		 * @return an OpenClEvent object which can be used to wait for the execution
		 * 		to finish
		 */
		OpenClEvent enqueueKernelExecution(cl_uint numDimensions, const size_t* globalWorkOffset,
				const size_t* globalWorkSize, const size_t* localWorkSize);

		/**
		 * Waits for commands from the previous to finish.
		 */
		void finishPreviousExecution();

		static void pfn_notify(cl_program a, void *user_data);


	private:
		/** The OpenCL device to use for this context. */
		OpenClDevice device;

		/** The OpenCL context. */
		cl_context clContext;

		/** The OpenCL command queue. */
		cl_command_queue clCommandQueue;

		/** A map which holds all buffers used in this execution context. */
		BufferMap buffers;

		/** The OpenCL program to use by this execution context. */
		cl_program clProgram;

		/** A cache used to store already created kernels. */
		KernelMap kernels;

		/** The OpenCL kernel to be executed next in this execution context. */
		cl_kernel clKernel;

		/**
		 * Initializes the OpenCL context.
		 */
		void initOpenClContext();

		/**
		 * Initializes an OpenCL command queue.
		 */
		void initOpenClCommandQueue();

		/**
		 * Performs actual building of the program executable.
		 */
		void buildProgramFromSourceInternal(const char* source, size_t sourceLength);

		/**
		 * Releases the OpenCL context, if any is created.
		 */
		void releaseContext();

		/**
		 * Releases the OpenCL command queue, if any is created.
		 */
		void releaseCommandQueue();

		/**
		 * Releases any buffers which are left in this execution context.
		 */
		void releaseBuffers();

		/**
		 * Releases the current kernel, if any is built.
		 */
		void releaseKernels();

		/**
		 * Releases the current program and any kernel associated with it.
		 */
		void releaseProgram();


	};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_EXECUTION_CONTEXT_H */
