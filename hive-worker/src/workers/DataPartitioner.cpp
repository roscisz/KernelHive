/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
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
#include <iostream>
#include <commons/KernelHiveException.h>

#include "DataPartitioner.h"

#include "commons/KhUtils.h"
#include "../communication/DataUploaderGridFs.h"
#include "../communication/DataDownloaderGridFs.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataPartitioner::KERNEL = "partitionData";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataPartitioner::DataPartitioner(char **argv) : OpenCLWorker(argv) {
	totalDataSize = 0;
	outputDataAddress = NULL;
	resultBuffers = NULL;
}

DataPartitioner::~DataPartitioner() {
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataPartitioner::getKernelName() {
	return KERNEL;
}

void DataPartitioner::workSpecific() {
	std::cout << ">>> DataPartitioner work BEGIN" << std::endl;
	runAllDownloads(); // Download data and the kernel

	// Wait for the data to be ready
	waitForAllDownloads();

//	std::cout << ">>> data size: " << buffers[dataIdInt]->getSize() << std::endl;
//	buffers[dataIdInt]->logMyFloatData();

	setPercentDone(40);

	totalDataSize = buffers[dataIds[0]]->getSize();
	size_t outputSizeInBytes = outputSize * sizeof(byte);

	// Allocate local result buffers
	for (int i = 0; i < resultsCount; i++) {
		resultBuffers[i]->allocate(outputSizeInBytes);
	}

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, totalDataSize * sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(OUTPUT_BUFFER, resultsCount * outputSizeInBytes, CL_MEM_READ_WRITE);

	// Begin copying data to the device
	OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, 0, totalDataSize, (void*)buffers[dataIds[0]]->getRawData());

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
	context->setValueArg(1, sizeof(unsigned int), (void*)&totalDataSize);
	context->setValueArg(2, sizeof(unsigned int), (void*)&resultsCount);
	context->setBufferArg(3, OUTPUT_BUFFER);
	context->setValueArg(4, sizeof(unsigned int), (void*)&outputSize);

	// Execute the kernel
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);
	setPercentDone(80);

	// Copy the result:
	OpenClEvent** copyEvents = new OpenClEvent*[resultsCount];
	for (int i = 0; i < resultsCount; i++) {
		dataCopy = context->enqueueRead(OUTPUT_BUFFER, i * outputSizeInBytes,
				outputSizeInBytes, (void*)resultBuffers[i]->getRawData());
		copyEvents[i] = &dataCopy;
	}
	context->waitForEvents((cl_uint) resultsCount, copyEvents);
	setPercentDone(90);

	uploaders.push_back(new DataUploaderGridFs(outputDataAddress, resultBuffers, resultsCount));

	runAllUploads();
	waitForAllUploads();

	setPercentDone(100);
}

void DataPartitioner::initSpecific(char *const argv[]) {
	if (datasCount != 1) {
		throw new KernelHiveException("DataPartitioner supports only one input file");
	}
}

} /* namespace KernelHive */
