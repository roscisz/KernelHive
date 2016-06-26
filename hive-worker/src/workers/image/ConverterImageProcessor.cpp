/**
* Copyright (c) 2016 Gdansk University of Technology
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

#include <commons/KernelHiveException.h>
#include "ConverterImageProcessor.h"

namespace KernelHive {

ConverterImageProcessor::ConverterImageProcessor(char **argv): ImageProcessor(argv) {
}

ConverterImageProcessor::~ConverterImageProcessor() {

}

const char *ConverterImageProcessor::getKernelName() {
    return "image_conversion";
}

void ConverterImageProcessor::workOnImage(int bufferNumber) {
    context->write(INPUT_BUFFER, 0, frameSize * sizeof(byte), (void *) (buffers[dataIds[bufferNumber]]->getRawData()));
    context->executeKernel(numberOfDimensions, dimensionOffsets, globalSizes, localSizes);
    context->read(OUTPUT_BUFFER, 0, frameSize * sizeof(byte), (void *) (resultBuffers[bufferNumber]->getRawData()));
}

void ConverterImageProcessor::initSpecific(char *const *argv) {
    ImageProcessor::initSpecific(argv);
    if (datasCount != resultsCount) {
        throw new KernelHiveException("ConverterImageProcessor supports only the same number of input and output files");
    }
}

void ConverterImageProcessor::prepareKernel() {
    context->createBuffer(INPUT_BUFFER, frameSize * sizeof(byte), CL_MEM_READ_ONLY);
    context->createBuffer(OUTPUT_BUFFER, frameSize * sizeof(byte), CL_MEM_WRITE_ONLY);
    context->createBuffer(PREVIEW_BUFFER, sizeof(PreviewObject), CL_MEM_READ_WRITE);

    context->buildProgramFromSource((char *)buffers[kernelDataId]->getRawData(), buffers[kernelDataId]->getSize());
    context->prepareKernel(getKernelName());

    context->setBufferArg(0, INPUT_BUFFER);
    context->setBufferArg(1, OUTPUT_BUFFER);
    context->setValueArg(2, sizeof(unsigned int), (void*) &imageWidth);
    context->setValueArg(3, sizeof(unsigned int), (void*) &imageHeight);
    context->setBufferArg(4, PREVIEW_BUFFER);
}

}

