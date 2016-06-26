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

#include "ConvolutionImageProcessor.h"
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>
#include <cmath>
#include <commons/KernelHiveException.h>
#include <commons/KhUtils.h>

namespace KernelHive {

ConvolutionImageProcessor::ConvolutionImageProcessor(char **argv): ImageProcessor(argv) {
    matrix = NULL;
    divider = 0.0;
    matrixSize = 0;
}

ConvolutionImageProcessor::~ConvolutionImageProcessor() {
    cleanupResources();
}

void ConvolutionImageProcessor::cleanupResources() {
    if (matrix != NULL) {
        delete[] matrix;
    }
}

void ConvolutionImageProcessor::initSpecific(char *const *argv) {
    ImageProcessor::initSpecific(argv);
    std::string m = nextParam(argv);
    std::vector<std::string> result;
    boost::split(result, m, boost::is_any_of(","));
    double s = sqrt(result.size());

    if (round(s) != s || ((int) s) % 2 == 0) {
        throw new KernelHiveException("Weight matrix is not square or is not even rank");
    }

    matrix = new float[result.size()];
    matrixSize = result.size();
    for (int i = 0; i < matrixSize; ++i) {
        matrix[i] = KhUtils::atof(result[i]);
        divider += matrix[i];
    }

    //divider cannot be 0
    if (fabs(divider) == 0.0) {
        divider = 1.0;
    }

    if (datasCount != resultsCount) {
        throw new KernelHiveException("ConvolutionImageProcessor supports only the same number of input and output files");
    }
}

const char *ConvolutionImageProcessor::getKernelName() {
    return "image_convolution";
}

void ConvolutionImageProcessor::workOnImage(int bufferNumber) {
    context->writeRect(INPUT_BUFFER, bufferOrigin, hostOrigin, region, (size_t) (imageWidth + matrixRank - 1) * internalChannels * sizeof(byte),
                       (size_t) imageWidth * internalChannels * sizeof(byte), (void *) (buffers[dataIds[bufferNumber]]->getRawData()));
    context->executeKernel(numberOfDimensions, dimensionOffsets, globalSizes, localSizes);
    context->read(OUTPUT_BUFFER, 0, frameSize * sizeof(byte), (void *) (resultBuffers[bufferNumber]->getRawData()));
}

void ConvolutionImageProcessor::prepareKernel() {
    matrixRank = (int) sqrt(matrixSize);

    size_t extendedFrameSize = (size_t) ((imageWidth + matrixRank - 1) * (imageHeight + matrixRank - 1) * internalChannels);
    context->createBuffer(INPUT_BUFFER, extendedFrameSize * sizeof(byte), CL_MEM_READ_ONLY);
    context->createBuffer(OUTPUT_BUFFER, frameSize * sizeof(byte), CL_MEM_WRITE_ONLY);
    context->createBuffer(PREVIEW_BUFFER, sizeof(PreviewObject), CL_MEM_READ_WRITE);
    context->createBuffer(MATRIX_BUFFER, matrixSize * sizeof(float), CL_MEM_READ_ONLY);
    context->write(MATRIX_BUFFER, 0, matrixSize * sizeof(float), (void*) matrix);

    context->buildProgramFromSource((char *)buffers[kernelDataId]->getRawData(), buffers[kernelDataId]->getSize());
    context->prepareKernel(getKernelName());

    context->setBufferArg(0, INPUT_BUFFER);
    context->setBufferArg(1, OUTPUT_BUFFER);
    context->setValueArg(2, sizeof(unsigned int), (void*) &imageWidth);
    context->setValueArg(3, sizeof(unsigned int), (void*) &imageHeight);
    context->setBufferArg(4, MATRIX_BUFFER);
    context->setValueArg(5, matrixSize * sizeof(float), NULL);
    context->setValueArg(6, sizeof(unsigned int), (void *) &matrixRank);
    context->setValueArg(7, sizeof(float), (void *) &divider);
    context->setBufferArg(8, PREVIEW_BUFFER);

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

