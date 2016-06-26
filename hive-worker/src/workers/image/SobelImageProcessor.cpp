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

#include <cmath>
#include <commons/KernelHiveException.h>
#include "SobelImageProcessor.h"

namespace KernelHive {

const char* SobelImageProcessor::MATRIX_BUFFER_X = "matrixBufferX";
const char* SobelImageProcessor::MATRIX_BUFFER_Y = "matrixBufferY";
const char* SobelImageProcessor::TMP_BUFFER_X = "tmpBufferX";
const char* SobelImageProcessor::TMP_BUFFER_Y = "tmpBufferY";

SobelImageProcessor::SobelImageProcessor(char **argv): ImageProcessor(argv) {
    matrixSize = 9;
    matrixX = new float[9]{1.0f, 2.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0f, -1.0f};
    matrixY = new float[9]{1.0f, 0.0f, -1.0f, 2.0f, 0.0f, -2.0f, 1.0f, 0.0f, -1.0f};
    divider = 1.0f;
    sumCoeff = 0.5f;
    convolutionKernel = "image_convolution";
    conversionKernel = "image_conversion";
    sumKernel = "image_sum";
}

SobelImageProcessor::~SobelImageProcessor() {
    cleanupResources();
}

void SobelImageProcessor::workOnImage(int bufferNumber) {
    context->writeRect(INPUT_BUFFER, bufferOrigin, hostOrigin, region, (size_t) (imageWidth + matrixRank - 1) * internalChannels * sizeof(byte),
                       (size_t) imageWidth * internalChannels * sizeof(byte), (void *) (buffers[dataIds[bufferNumber]]->getRawData()));
    context->prepareKernel(convolutionKernel);
    // detect horizontal edges
    context->setBufferArg(1, TMP_BUFFER_X);
    context->setBufferArg(4, MATRIX_BUFFER_X);
    context->executeKernel(numberOfDimensions, dimensionOffsets, globalSizes, localSizes);
    // detect vertical edges
    context->setBufferArg(1, TMP_BUFFER_Y);
    context->setBufferArg(4, MATRIX_BUFFER_Y);
    context->executeKernel(numberOfDimensions, dimensionOffsets, globalSizes, localSizes);
    // sum two temporary images
    context->prepareKernel(sumKernel);
    context->executeKernel(numberOfDimensions, dimensionOffsets, globalSizes, localSizes);
    // convert to grey scale
    context->prepareKernel(conversionKernel);
    context->executeKernel(numberOfDimensions, dimensionOffsets, globalSizes, localSizes);

    context->read(TMP_BUFFER_X, 0, frameSize * sizeof(byte), (void *) (resultBuffers[bufferNumber]->getRawData()));
}

const char *SobelImageProcessor::getKernelName() {
    return NULL;
}

void SobelImageProcessor::cleanupResources() {
    delete[] matrixY;
    delete[] matrixX;
}

void SobelImageProcessor::initSpecific(char *const *argv) {
    ImageProcessor::initSpecific(argv);
    if (datasCount != resultsCount) {
        throw new KernelHiveException("SobelImageProcessor supports only the same number of input and output files");
    }
}

void SobelImageProcessor::prepareKernel() {
    matrixRank = (int) sqrt(matrixSize);

    size_t extendedFrameSize = (size_t) ((imageWidth + matrixRank - 1) * (imageHeight + matrixRank - 1) * internalChannels);
    context->createBuffer(INPUT_BUFFER, extendedFrameSize * sizeof(byte), CL_MEM_READ_ONLY);
    context->createBuffer(OUTPUT_BUFFER, frameSize * sizeof(byte), CL_MEM_READ_WRITE);
    context->createBuffer(TMP_BUFFER_X, frameSize * sizeof(byte), CL_MEM_READ_WRITE);
    context->createBuffer(TMP_BUFFER_Y, frameSize * sizeof(byte), CL_MEM_READ_WRITE);
    context->createBuffer(PREVIEW_BUFFER, sizeof(PreviewObject), CL_MEM_READ_WRITE);
    context->createBuffer(MATRIX_BUFFER_X, matrixSize * sizeof(float), CL_MEM_READ_ONLY);
    context->createBuffer(MATRIX_BUFFER_Y, matrixSize * sizeof(float), CL_MEM_READ_ONLY);
    context->write(MATRIX_BUFFER_X, 0, matrixSize * sizeof(float), (void*) matrixX);
    context->write(MATRIX_BUFFER_Y, 0, matrixSize * sizeof(float), (void*) matrixY);

    context->buildProgramFromSource((char *)buffers[kernelDataId]->getRawData(), buffers[kernelDataId]->getSize());

    // convolution
    context->prepareKernel(convolutionKernel);
    context->setBufferArg(0, INPUT_BUFFER);
    context->setBufferArg(1, TMP_BUFFER_X);
    context->setValueArg(2, sizeof(unsigned int), (void*) &imageWidth);
    context->setValueArg(3, sizeof(unsigned int), (void*) &imageHeight);
    context->setBufferArg(4, MATRIX_BUFFER_X);
    context->setValueArg(5, matrixSize * sizeof(float), NULL);
    context->setValueArg(6, sizeof(unsigned int), (void *) &matrixRank);
    context->setValueArg(7, sizeof(float), (void *) &divider);
    context->setBufferArg(8, PREVIEW_BUFFER);

    // sum
    context->prepareKernel(sumKernel);
    context->setBufferArg(0, TMP_BUFFER_X);
    context->setValueArg(1, sizeof(float), (void *) &sumCoeff);
    context->setBufferArg(2, TMP_BUFFER_Y);
    context->setValueArg(3, sizeof(float), (void *) &sumCoeff);
    context->setBufferArg(4, OUTPUT_BUFFER);
    context->setValueArg(5, sizeof(unsigned int), (void*) &imageWidth);
    context->setValueArg(6, sizeof(unsigned int), (void*) &imageHeight);
    context->setBufferArg(7, PREVIEW_BUFFER);

    // conversion
    context->prepareKernel(conversionKernel);
    context->setBufferArg(0, OUTPUT_BUFFER);
    context->setBufferArg(1, TMP_BUFFER_X);
    context->setValueArg(2, sizeof(unsigned int), (void*) &imageWidth);
    context->setValueArg(3, sizeof(unsigned int), (void*) &imageHeight);
    context->setBufferArg(4, PREVIEW_BUFFER);

    border = (size_t) (matrixRank / 2);
    bufferOrigin[0] = border * internalChannels * sizeof(byte);
    bufferOrigin[1] = border * sizeof(byte);
    bufferOrigin[2] = 0;
    hostOrigin[0] = 0;
    hostOrigin[1] = 0;
    hostOrigin[2] = 0;
    region[0] = (size_t) imageWidth * internalChannels * sizeof(byte);
    region[1] = (size_t) imageHeight * sizeof(byte);
    region[2] = 1;
}

}

