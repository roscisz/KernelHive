#include "ExecutionContext.h"
#include "KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	ExecutionContext::ExecutionContext(const OpenClDevice& device)
		: device(device)
	{
		clContext = NULL;
		clCommandQueue = NULL;
		clProgram = NULL;
		clKernel = NULL;
		initOpenClContext();
		initOpenClCommandQueue();
	}

	ExecutionContext::~ExecutionContext() {
		releaseProgram();
		releaseBuffers();
		releaseCommandQueue();
		releaseContext();
	}

	void ExecutionContext::createBuffer(const std::string& name, size_t size, cl_mem_flags flags) {
		cl_int errorCode;
		cl_mem buffer = clCreateBuffer(clContext, flags, size, NULL, &errorCode);
		if (errorCode == CL_SUCCESS) {
			buffers[name] = buffer;
		} else {
			throw OpenClException("Error creating an OpenCL buffer object", errorCode);
		}
	}

	void ExecutionContext::releaseBuffer(const std::string& name) {
		cl_int errorCode;
		BufferMap::iterator iterator = buffers.find(name);
		if (iterator != buffers.end()) {
			errorCode = clReleaseMemObject(buffers[name]);
			if (errorCode == CL_SUCCESS) {
				buffers.erase(name);
			} else {
				throw OpenClException("Error releasing an OpenCL buffer object", errorCode);
			}
		}
	}

	cl_mem ExecutionContext::getRawBuffer(const std::string& name) {
		cl_mem buffer = NULL;
		BufferMap::iterator iterator = buffers.find(name);
		if (iterator != buffers.end()) {
			buffer = buffers[name];
		} else {
			throw KernelHiveException("Buffer not found!");
		}
		return buffer;
	}

	void ExecutionContext::write(std::string bufferName, size_t offset,
			size_t size, const void* ptr)
	{
		// TODO Check for buffer existence, throw KernelHive exception
		cl_int errorCode = clEnqueueWriteBuffer(clCommandQueue, buffers[bufferName],
				CL_TRUE, offset, size, ptr, 0, NULL, NULL);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error writing to a buffer", errorCode);
		}
	}

	OpenClEvent ExecutionContext::enqueueWrite(std::string bufferName, size_t offset,
			size_t size, const void* ptr)
	{
		cl_event event;
		// TODO Check for buffer existence, throw KernelHive exception
		cl_int errorCode = clEnqueueWriteBuffer(clCommandQueue, buffers[bufferName],
				CL_FALSE, offset, size, ptr, 0, NULL, &event);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error enqueueing a write to a buffer", errorCode);
		}
		return OpenClEvent(event);
	}

	void ExecutionContext::read(std::string bufferName, size_t offset,
			size_t size, void* ptr)
	{
		// TODO Check for buffer existence, throw KernelHive exception
		cl_int errorCode = clEnqueueReadBuffer(clCommandQueue, buffers[bufferName],
				CL_TRUE, offset, size, ptr, 0, NULL, NULL);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error reading from a buffer", errorCode);
		}
	}

	OpenClEvent ExecutionContext::enqueueRead(std::string bufferName, size_t offset,
			size_t size, void* ptr)
	{
		cl_event event;
		// TODO Check for buffer existence, throw KernelHive exception
		cl_int errorCode = clEnqueueReadBuffer(clCommandQueue, buffers[bufferName],
				CL_FALSE, offset, size, ptr, 0, NULL, &event);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error enqueueing a read from a buffer", errorCode);
		}
		return OpenClEvent(event);
	}

	void ExecutionContext::waitForEvents(size_t eventsCount, OpenClEvent* events) {
		cl_event* clEvents = new cl_event[eventsCount];
		for (size_t i = 0; i < eventsCount; i++) {
			clEvents[i] = events[i].getOpenClEvent();
		}
		cl_int errorCode = clWaitForEvents(eventsCount, clEvents);
		delete [] clEvents;
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error waiting for events to finish", errorCode);
		}
	}

	void ExecutionContext::buildProgramFromSource(std::string source) {
		releaseProgram();
		buildProgramFromSourceInternal(source.data(), source.length());
	}

	void ExecutionContext::buildProgramFromSource(const char* source, size_t sourceLength) {
		releaseProgram();
		buildProgramFromSourceInternal(source, sourceLength);
	}

	void ExecutionContext::prepareKernel(const char* kernelName) {
		std::string name = kernelName;
		prepareKernel(name);
	}

	void ExecutionContext::prepareKernel(std::string kernelName) {
		if (kernels.find(kernelName) != kernels.end()) {
			clKernel = kernels[kernelName];
		} else {
			cl_int errorCode;
			cl_kernel kernel = clCreateKernel(clProgram, kernelName.data(), &errorCode);
			if (errorCode != CL_SUCCESS) {
				throw OpenClException("Error creating a kernel", errorCode);
			}
			kernels[kernelName] = kernel;
			clKernel = kernel;
		}
	}

	void ExecutionContext::setValueArg(cl_uint index, size_t size,
			const void* value)
	{
		cl_int errorCode = clSetKernelArg(clKernel, index, size, value);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error setting kernel argument.", errorCode);
		}
	}

	void ExecutionContext::setBufferArg(cl_uint index, const char* bufferName) {
		cl_mem buffer = getRawBuffer(bufferName);
		cl_int errorCode = clSetKernelArg(clKernel, index, sizeof(cl_mem), (void*)&buffer);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error setting kernel argument.", errorCode);
		}
	}

	void ExecutionContext::executeKernel(cl_uint numDimensions, const size_t* globalWorkOffset,
			const size_t* globalWorkSize, const size_t* localWorkSize)
	{
		cl_event event;
		cl_int errorCode = clEnqueueNDRangeKernel(clCommandQueue, clKernel,
				numDimensions, globalWorkOffset, globalWorkSize, localWorkSize,
				0, NULL, &event);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error executing a kernel", errorCode);
		}
		clWaitForEvents(1, &event);
	}

	OpenClEvent ExecutionContext::enqueueKernelExecution(cl_uint numDimensions,
			const size_t* globalWorkOffset, const size_t* globalWorkSize,
			const size_t* localWorkSize)
	{
		cl_event event;
		cl_int errorCode = clEnqueueNDRangeKernel(clCommandQueue, clKernel,
				numDimensions, globalWorkOffset, globalWorkSize, localWorkSize,
				0, NULL, &event);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error enqueuing kernel execution", errorCode);
		}
		return OpenClEvent(event);
	}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

	void ExecutionContext::initOpenClContext() {
		const cl_context_properties properties[] = {
				CL_CONTEXT_PLATFORM,
				(cl_context_properties) device.getClPlatformId(),
				0
		};
		cl_int errorCode;
		cl_device_id deviceId = device.getClDeviceId();
		// TODO think about adding that callback function to the context
		clContext = clCreateContext(properties, 1, &deviceId, NULL, NULL, &errorCode);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error initializing OpenCL context", errorCode);
		}
	}

	void ExecutionContext::initOpenClCommandQueue() {
		cl_int errorCode;
		clCommandQueue = clCreateCommandQueue(clContext, device.getClDeviceId(), NULL, &errorCode);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error initializing OpenCL command queue", errorCode);
		}
	}

	void ExecutionContext::releaseBuffers() {
		if (buffers.size() > 0) {
			BufferMap::iterator iterator;
			for (iterator = buffers.begin(); iterator != buffers.end(); iterator++) {
				clReleaseMemObject(iterator->second);
			}
			buffers.clear();
		}
	}

	void ExecutionContext::buildProgramFromSourceInternal(const char* source,
			size_t sourceLength)
	{
		cl_int errorCode;
		clProgram = clCreateProgramWithSource(clContext, 1,
				&source, &sourceLength, &errorCode);
		// TODO Think about handling out of memory/host error codes...
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error creating a program from source", errorCode);
		}
		// TODO Think about adding the callback function...
		cl_device_id openClDeviceId = device.getClDeviceId();
		errorCode = clBuildProgram(clProgram, 1, &openClDeviceId,
				NULL, NULL, NULL);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error building the program", errorCode);
		}
	}

	void ExecutionContext::releaseContext() {
		if (clContext != NULL) {
			clReleaseContext(clContext);
			clContext = NULL;
		}
	}

	void ExecutionContext::releaseCommandQueue() {
		if (clCommandQueue != NULL) {
			clFlush(clCommandQueue);
			clFinish(clCommandQueue);
			clReleaseCommandQueue(clCommandQueue);
			clCommandQueue = NULL;
		}
	}

	void ExecutionContext::releaseKernels() {
		if (kernels.size() > 0) {
			KernelMap::iterator iterator;
			for (iterator = kernels.begin(); iterator != kernels.end(); iterator++) {
				clReleaseKernel(iterator->second);
			}
			kernels.clear();
			clKernel = NULL;
		}
	}

	void ExecutionContext::releaseProgram() {
		releaseKernels();
		if (clProgram != NULL) {
			clReleaseProgram(clProgram);
			clProgram = NULL;
		}
	}

} /* namespace KernelHive */
