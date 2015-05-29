/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
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
#include "DataMerger.h"

#include "commons/KhUtils.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataMerger::KERNEL = "mergeData";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataMerger::DataMerger(char **argv) : BasicWorker(argv) {
	datasCount = 0;
	totalDataSize = 0;
	inputDataAddresses = NULL;
	outputDataAddress = NULL;
	dataIds = NULL;
	resultBuffer = NULL;
}

DataMerger::~DataMerger() {
	DataMerger::cleanupResources();
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataMerger::getKernelName() {
	return KERNEL;
}

void DataMerger::workSpecific() {
	runAllDownloads(); // Download data and the kernel

	setPercentDone(20);

	// Wait for the data to be ready
	waitForAllDownloads();
	setPercentDone(40);

	for (int i = 0; i < datasCount; i++) {
		totalDataSize += buffers[dataIds[i]]->getSize();
	}

	// Allocate input and output buffers on the device
	context->createBuffer(INPUT_BUFFER, totalDataSize*sizeof(byte), CL_MEM_READ_WRITE);
	context->createBuffer(OUTPUT_BUFFER, outputSize*sizeof(byte), CL_MEM_READ_WRITE);

	// Begin copying data to the device
	size_t copyOffset = 0;
	OpenClEvent** copyEvents = new OpenClEvent*[datasCount];
	for (int i = 0; i < datasCount; i++) {
		OpenClEvent dataCopy = context->enqueueWrite(INPUT_BUFFER, copyOffset,
				buffers[dataIds[i]]->getSize()*sizeof(byte), (void*)buffers[dataIds[i]]->getRawData());
		copyEvents[i] = &dataCopy;
		copyOffset += buffers[dataIds[i]]->getSize();
	}

	// Compile and prepare the kernel for execution
	context->buildProgramFromSource((char *)buffers[kernelDataId]->getRawData(),
			buffers[kernelDataId]->getSize());
	context->prepareKernel(getKernelName());

	// Wait for copy to finish.
	context->waitForEvents(datasCount, copyEvents);
	setPercentDone(60);

	// Set kernel agrguments
	context->setBufferArg(0, INPUT_BUFFER);
	context->setValueArg(1, sizeof(unsigned int), (void*)&totalDataSize);
	context->setValueArg(2, sizeof(unsigned int), (void*)&datasCount);
	context->setBufferArg(3, OUTPUT_BUFFER);
	context->setValueArg(4, sizeof(unsigned int), (void*)&outputSize);

	// Execute the kernel
	context->executeKernel(numberOfDimensions, dimensionOffsets,
			globalSizes, localSizes);
	setPercentDone(80);

	// Copy the result:
	context->read(OUTPUT_BUFFER, 0, outputSize*sizeof(byte), (void*)resultBuffer->getRawData());
	setPercentDone(90);

	// Upload data to repository
	uploaders.push_back(new DataUploaderTCP(outputDataAddress, &resultBuffer, 1));
	runAllUploads();
	waitForAllUploads();
	setPercentDone(100);
}

void DataMerger::initSpecific(char *const argv[]) {
	datasCount = KhUtils::atoi(nextParam(argv));
	inputDataAddresses = new NetworkAddress*[datasCount];
	dataIds = new std::string[datasCount];
	for (int i = 0; i < datasCount; i++) {
		inputDataAddresses[i] = new NetworkAddress(nextParam(argv), nextParam(argv));
		dataIds[i] = std::string(nextParam(argv));
		buffers[dataIds[i]] = new SynchronizedBuffer();
		downloaders[dataIds[i]] = new DataDownloaderTCP(inputDataAddresses[i],
			dataIds[i].c_str(), buffers[dataIds[i]]);
	}
	downloaders[kernelDataId] = new DataDownloaderTCP(kernelAddress,
			kernelDataId.c_str(), buffers[kernelDataId]);

	// TODO For merger only - skip the number of outputs:
	nextParam(argv);
	outputDataAddress = new NetworkAddress(nextParam(argv), nextParam(argv));
	resultBuffer = new SynchronizedBuffer(outputSize);
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataMerger::cleanupResources() {
	if (dataIds != NULL) {
		delete [] dataIds;
	}
	if (inputDataAddresses != NULL) {
		for (int i = 0; i < datasCount; i++) {
			delete inputDataAddresses[i];
		}
		delete [] inputDataAddresses;
	}
	if (outputDataAddress != NULL) {
		delete outputDataAddress;
	}
	if (resultBuffer != NULL) {
		delete resultBuffer;
	}
}

} /* namespace KernelHive */
