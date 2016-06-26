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

#include "SumImageProcessor.h"
#include <commons/KernelHiveException.h>
#include <commons/KhUtils.h>

namespace KernelHive {

const char* SumImageProcessor::INPUT_BUFFER_2 = "inputBuffer2";

SumImageProcessor::SumImageProcessor(char **argv): ImageProcessor(argv) {
    weight1 = 0.0f;
    weight2 = 0.0f;
}

SumImageProcessor::~SumImageProcessor() {

}

void SumImageProcessor::initSpecific(char *const *argv) {
    ImageProcessor::initSpecific(argv);
    weight1 = KhUtils::atof(nextParam(argv));
    weight2 = KhUtils::atof(nextParam(argv));

    if (datasCount != 2 * resultsCount) {
        throw new KernelHiveException("SumImageProcessor supports only two times more input than output files");
    }
}

void SumImageProcessor::workOnImage(int bufferNumber) {
    if (buffers[dataIds[bufferNumber]]->getSize() != buffers[dataIds[bufferNumber + resultsCount]]->getSize()) {
        throw new KernelHiveException("Different number of bytes in buffers");
    }
    context->write(INPUT_BUFFER, 0, frameSize * sizeof(byte),
                   (void *) (buffers[dataIds[bufferNumber]]->getRawData()));
    context->write(INPUT_BUFFER_2, 0, frameSize * sizeof(byte),
                   (void *) (buffers[dataIds[bufferNumber + resultsCount]]->getRawData()));
    context->executeKernel(numberOfDimensions, dimensionOffsets, globalSizes, localSizes);
    context->read(OUTPUT_BUFFER, 0, frameSize * sizeof(byte),
                  (void *) (resultBuffers[bufferNumber]->getRawData()));
}

const char *SumImageProcessor::getKernelName() {
    return "image_sum";
}

void SumImageProcessor::prepareKernel() {

    context->createBuffer(INPUT_BUFFER, frameSize * sizeof(byte), CL_MEM_READ_ONLY);
    context->createBuffer(INPUT_BUFFER_2, frameSize * sizeof(byte), CL_MEM_READ_ONLY);
    context->createBuffer(OUTPUT_BUFFER, frameSize * sizeof(byte), CL_MEM_WRITE_ONLY);
    context->createBuffer(PREVIEW_BUFFER, sizeof(PreviewObject), CL_MEM_READ_WRITE);

    context->buildProgramFromSource((char *) buffers[kernelDataId]->getRawData(), buffers[kernelDataId]->getSize());
    context->prepareKernel(getKernelName());

    context->setBufferArg(0, INPUT_BUFFER);
    context->setValueArg(1, sizeof(float), (void *) &weight1);
    context->setBufferArg(2, INPUT_BUFFER_2);
    context->setValueArg(3, sizeof(float), (void *) &weight2);
    context->setBufferArg(4, OUTPUT_BUFFER);
    context->setValueArg(5, sizeof(unsigned int), (void *) &imageWidth);
    context->setValueArg(6, sizeof(unsigned int), (void *) &imageHeight);
    context->setBufferArg(7, PREVIEW_BUFFER);
}

}

