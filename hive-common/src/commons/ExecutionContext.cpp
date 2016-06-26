/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2016 Adrian Boguszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
#include "ExecutionContext.h"
#include "KernelHiveException.h"

#include <cstdio>
#include <unistd.h>
#include "Logger.h"

#define ITERATIVE_EXECUTION

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

	void ExecutionContext::writeRect(std::string bufferName, const size_t bufferOrigin[], const size_t hostOrigin[],
									 const size_t region[], size_t bufferRowPitch, size_t hostRowPitch, const void *ptr)
	{
		cl_int errorCode = clEnqueueWriteBufferRect(clCommandQueue, buffers[bufferName], CL_TRUE, bufferOrigin, hostOrigin,
													region, bufferRowPitch, 0, hostRowPitch, 0, ptr, 0, NULL, NULL);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error writing rectangle to a buffer", errorCode);
		}
	}

	OpenClEvent ExecutionContext::enqueueWriteRect(std::string bufferName, const size_t *bufferOrigin, const size_t *hostOrigin,
											const size_t *region, size_t bufferRowPitch, size_t hostRowPitch, const void *ptr) {
		cl_event event;
		cl_int errorCode = clEnqueueWriteBufferRect(clCommandQueue, buffers[bufferName], CL_FALSE, bufferOrigin, hostOrigin,
													region, bufferRowPitch, 0, hostRowPitch, 0, ptr, 0, NULL, &event);
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error writing rectangle to a buffer", errorCode);
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

	void ExecutionContext::waitForEvents(cl_uint eventsCount, OpenClEvent** events) {
		cl_event* clEvents = new cl_event[eventsCount];
		for (size_t i = 0; i < eventsCount; i++) {
			clEvents[i] = events[i]->getOpenClEvent();
		}
		cl_int errorCode = clWaitForEvents(eventsCount, clEvents);
		delete [] clEvents;
		if (errorCode != CL_SUCCESS) {
			throw OpenClException("Error waiting for events to finish", errorCode);
		}
	}

	void ExecutionContext::waitForEvent(OpenClEvent *event) {
		cl_event clEvent = event->getOpenClEvent();
		cl_int errorCode = clWaitForEvents(1, &clEvent);
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
		clReleaseEvent(event);
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
		clCommandQueue = clCreateCommandQueue(clContext, device.getClDeviceId(), 0, &errorCode);
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

 	void ExecutionContext::pfn_notify(cl_program a, void *user_data)
	{
		fprintf(stderr, "OpenCL Error (via pfn_notify): %s\n", user_data);
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
				/*"-cl-nv-verbose"*/NULL, /*&pfn_notify*/NULL, NULL);
		if (errorCode != CL_SUCCESS) {
			if (errorCode == CL_BUILD_PROGRAM_FAILURE) {

				cl_int retCode;
				size_t logSize = 0;
				char *buildLog = NULL;
				retCode = clGetProgramBuildInfo(clProgram, openClDeviceId,
						CL_PROGRAM_BUILD_LOG, 0, NULL, &logSize);
				if (retCode != CL_SUCCESS) {
					Logger::log(ERROR, "Error acquiring build log size\n");
				} else {
					buildLog = new char[logSize];
					retCode = clGetProgramBuildInfo(clProgram, openClDeviceId,
							CL_PROGRAM_BUILD_LOG, logSize, buildLog, NULL);
					if (retCode != CL_SUCCESS) {
						Logger::log(ERROR, "Error acquiring build log\n");
					} else {
						Logger::log(ERROR, "Kernel build failure\n");
						printf("%s\n", buildLog);
					}
					delete [] buildLog;
				}

			}
			throw OpenClException("Error building the program", errorCode);
			clUnloadCompiler();
		}
	}

	void ExecutionContext::finishPreviousExecution() {
		if (clCommandQueue != NULL) {
			cl_int errCode = clFlush(clCommandQueue);
			if (errCode != CL_SUCCESS) {
				throw OpenClException("Error flushing a command queue", errCode);
			}
			errCode = clFinish(clCommandQueue);
			if (errCode != CL_SUCCESS) {
				throw OpenClException("Error finishing command queue events", errCode);
			}
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
