#include "ExecutionContext.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	ExecutionContext::ExecutionContext(const OpenClDevice& device)
		: device(device)
	{
		initOpenClContext();
		initOpenClCommandQueue();
	}

	ExecutionContext::~ExecutionContext() {
		releaseBuffers();
		if (clCommandQueue != NULL) {
			clFlush(clCommandQueue);
			clFinish(clCommandQueue);
			clReleaseCommandQueue(clCommandQueue);
		}
		if (clContext != NULL) {
			clReleaseContext(clContext);
		}
	}

	void ExecutionContext::createBuffer(const std::string& name, size_t size, cl_mem_flags flags) {
		cl_int errorCode;
		cl_mem buffer = clCreateBuffer(clContext, flags, size, NULL, &errorCode);
		if (errorCode == CL_SUCCESS) {
			buffers[name] = buffer;
		} else {
			std::string message = "Error creating an OpenCL buffer object";
			throw OpenClException(message, errorCode);
		}
	}

	void ExecutionContext::releaseBuffer(const std::string& name) {
		cl_int errorCode;
		BufferMap::iterator iterator = buffers.find(name);
		if (iterator != buffers.end()) {
			errorCode = clReleaseMemObject(buffers[name]);
			if ( errorCode == CL_SUCCESS) {
				buffers.erase(name);
			} else {
				std::string message = "Error releasing an OpenCL buffer object";
				throw OpenClException(message, errorCode);
			}
		}
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
			std::string message = "Error initializing OpenCL context";
			throw OpenClException(message, errorCode);
		}
	}

	void ExecutionContext::initOpenClCommandQueue() {
		cl_int errorCode;
		clCommandQueue = clCreateCommandQueue(clContext, device.getClDeviceId(), NULL, &errorCode);
		if (errorCode != CL_SUCCESS) {
			std::string message = "Error initializing OpenCL command queue";
			throw OpenClException(message, errorCode);
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

} /* namespace KernelHive */
