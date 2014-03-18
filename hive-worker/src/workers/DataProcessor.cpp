/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
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
#include <cstdio>
#include <string>
#include <iostream>

#include "commons/Logger.h"
#include "commons/OpenClEvent.h"
#include "threading/ThreadManager.h"
#include "../communication/DataUploader.h"
#include "DataProcessor.h"
#include "commons/KhUtils.h"

#define ITERATIVE_EXECUTION
#define ITERATION_LIMIT 10000
#define PreviewObject struct PreviewObjectStruct

namespace KernelHive {

struct PreviewObjectStruct {
	float f1;
	float f2;
	float f3;
};

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataProcessor::KERNEL = "processData";
const char* PREVIEW_BUFFER = "previewBuffer";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataProcessor::DataProcessor(char **argv) : BasicWorker(argv) {
	inputDataAddress = NULL;
	outputDataAddress = NULL;
	resultBuffer = NULL;
	dataIdInt = 0;
}

DataProcessor::~DataProcessor() {
	DataProcessor::cleanupResources();
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataProcessor::getKernelName() {
	return KERNEL;
}

void DataProcessor::initSpecific(char *const argv[]) {

	// TODO For processor only - skip the number of inputs:
	nextParam(argv);
	inputDataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	dataId = nextParam(argv);
	dataIdInt = KhUtils::atoi(dataId.c_str());
	
	// TODO For processor only - skip the number of outputs:
	nextParam(argv);
	outputDataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));

	buffers[dataIdInt] = new SynchronizedBuffer();
	resultBuffer = new SynchronizedBuffer(outputSize);

	downloaders[dataIdInt] = new DataDownloaderGridFs(inputDataAddress,
			dataId.c_str(), buffers[dataIdInt]);
	downloaders[kernelDataIdInt] = new DataDownloaderTCP(kernelAddress,
			kernelDataId.c_str(), buffers[kernelDataIdInt]);
	
	Logger::log(DEBUG, "(processor) >>> PROCESSOR INIT SPECIFIC END\n");
}

void DataProcessor::workSpecific() {
	runAllDownloads(); // Download data and the kernel

	// Wait for the data to be ready
	Logger::log(DEBUG, "(processor) >>> PROCESSOR BEFORE WAIT FOR DOWNLOADS\n");
	waitForAllDownloads();
	Logger::log(DEBUG, "(processor) >>> PROCESSOR AFTER WAIT FOR DOWNLOADS\n");
	setPercentDone(40);

	size_t size = buffers[dataIdInt]->getSize();

	Logger::log(DEBUG, "(processor) >>> PROCESSOR BEFORE COMPUTE\n");
	buffers[dataIdInt]->logMyFloatData();

	SynchronizedBuffer* previewBuffer = new SynchronizedBuffer(outputSize * sizeof(PreviewObject));

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, size*sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(OUTPUT_BUFFER, outputSize*sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(PREVIEW_BUFFER, outputSize*sizeof(PreviewObject), CL_MEM_READ_WRITE);

	// Begin copying data to the device
	OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, 0,
			size*sizeof(byte), (void*)buffers[dataIdInt]->getRawData());

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource((char *)buffers[kernelDataIdInt]->getRawData(),
			buffers[kernelDataIdInt]->getSize());
	context->prepareKernel(getKernelName());

	// Wait for data copy to finish
	OpenClEvent* tmp = &dataCopy;
	context->waitForEvents(1, &tmp);
	setPercentDone(60);

	// Set kernel agrguments
	context->setBufferArg(0, INPUT_BUFFER);
	context->setValueArg(1, sizeof(unsigned int), (void*)&size);
	context->setBufferArg(2, OUTPUT_BUFFER);
	context->setValueArg(3, sizeof(unsigned int), (void*)&outputSize);
	context->setBufferArg(4, PREVIEW_BUFFER);

	// Execute the kernel
#ifdef ITERATIVE_EXECUTION
	cl_int *result = new cl_int;
	*result = -1;
	int counter = 0;
	while ((*result) < 0 && counter < ITERATION_LIMIT) {
		counter++;
		//Logger::log(DEBUG, ">>> Iteration %d\n", counter);
		context->executeKernel(numberOfDimensions, dimensionOffsets,
				globalSizes, localSizes);
		context->finishPreviousExecution();
		// TODO Read the first byte of output and assign it to result
		context->read(OUTPUT_BUFFER, 0, sizeof(cl_int), (void*)result);
	}
	delete result;
#else
	printf("Kernel execution\n");
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);
#endif
	setPercentDone(80);


	// Copy the result:
	context->read(OUTPUT_BUFFER, 0, outputSize*sizeof(byte), (void*)resultBuffer->getRawData());
	context->read(PREVIEW_BUFFER, 0, outputSize*sizeof(PreviewObject), (void*)previewBuffer->getRawData());

	setPercentDone(90);

	Logger::log(INFO, "(processor) >>> PROCESSOR AFTER COMPUTE");
	//resultBuffer->logMyFloatData();

	reportPreview(previewBuffer);

	Logger::log(INFO, "(processor) >>> UPLOADING DATA TO ");
	// Upload data to repository
	uploaders.push_back(new DataUploader(outputDataAddress, resultBuffer));
	runAllUploads();
	waitForAllUploads();
	setPercentDone(100);
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

void DataProcessor::cleanupResources() {
	if (inputDataAddress != NULL) {
		delete inputDataAddress;
	}
	if (outputDataAddress != NULL) {
		delete outputDataAddress;
	}
	if (resultBuffer != NULL) {
		delete resultBuffer;
	}
}

} /* namespace KernelHive */
