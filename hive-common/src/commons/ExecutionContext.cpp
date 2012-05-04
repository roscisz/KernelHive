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
		if (clCommandQueue != NULL) {
			clReleaseCommandQueue(clCommandQueue);
		}
		if (clContext != NULL) {
			clReleaseContext(clContext);
		}
	}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

	void ExecutionContext::initOpenClContext() {
		const cl_context_properties properties[] = {
				CL_CONTEXT_PLATFORM,
				(cl_context_properties) device.getClDeviceId(),
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

} /* namespace KernelHive */
