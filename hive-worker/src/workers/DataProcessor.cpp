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
#include <commons/KernelHiveException.h>

#include "commons/Logger.h"
#include "commons/OpenClEvent.h"
#include "threading/ThreadManager.h"
#include "../communication/DataUploaderTCP.h"
#include "../communication/DataUploaderGridFs.h"
#include "DataProcessor.h"

//#define ITERATIVE_EXECUTION
#define ITERATION_LIMIT 10000

namespace KernelHive {


// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataProcessor::KERNEL = "processData";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataProcessor::DataProcessor(char **argv) : OpenCLWorker(argv) {
	outputDataAddress = NULL;
}

DataProcessor::~DataProcessor() {
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataProcessor::getKernelName() {
	return KERNEL;
}

void DataProcessor::initSpecific(char *const argv[]) {
	if (datasCount != 1 || resultsCount != 1) {
		throw new KernelHiveException("DataProcessor supports only one input and one output file");
	}
}

void DataProcessor::workSpecific() {
	runAllDownloads(); // Download data and the kernel

	// Wait for the data to be ready
	Logger::log(DEBUG, "(processor) >>> PROCESSOR BEFORE WAIT FOR DOWNLOADS\n");
	waitForAllDownloads();
	Logger::log(DEBUG, "(processor) >>> PROCESSOR AFTER WAIT FOR DOWNLOADS\n");
	setPercentDone(40);

	size_t size = buffers[dataIds[0]]->getSize();

	Logger::log(DEBUG, "(processor) >>> PROCESSOR BEFORE COMPUTE\n");
	buffers[dataIds[0]]->logMyFloatData();

	SynchronizedBuffer* previewBuffer = new SynchronizedBuffer(outputSize * sizeof(PreviewObject));

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, size*sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(OUTPUT_BUFFER, outputSize*sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(PREVIEW_BUFFER, outputSize*sizeof(PreviewObject), CL_MEM_READ_WRITE);

	// Begin copying data to the device
	OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, 0,
			size*sizeof(byte), (void*)buffers[dataIds[0]]->getRawData());

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource((char *)buffers[kernelDataId]->getRawData(),
			buffers[kernelDataId]->getSize());
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

	resultBuffers[0]->allocate(outputSize);
	// Copy the result:
	context->read(OUTPUT_BUFFER, 0, outputSize*sizeof(byte), (void*)resultBuffers[0]->getRawData());
	context->read(PREVIEW_BUFFER, 0, outputSize*sizeof(PreviewObject), (void*)previewBuffer->getRawData());

	setPercentDone(90);

	Logger::log(INFO, "(processor) >>> PROCESSOR AFTER COMPUTE");
	resultBuffers[0]->logMyFloatData();

	reportPreview(previewBuffer);

	Logger::log(INFO, "(processor) >>> UPLOADING DATA TO ");
	// Upload data to repository
	uploaders.push_back(new DataUploaderGridFs(outputDataAddress, resultBuffers, 1));
	runAllUploads();
	waitForAllUploads();
	setPercentDone(100);
}

} /* namespace KernelHive */
