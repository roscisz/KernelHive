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

#ifndef KERNELHIVE_SOBELIMAGEPROCESSOR_H
#define KERNELHIVE_SOBELIMAGEPROCESSOR_H

#include "ImageProcessor.h"

namespace KernelHive {

class SobelImageProcessor: public ImageProcessor {
public:
    SobelImageProcessor(char **argv);
    virtual ~SobelImageProcessor();

protected:
    static const char* MATRIX_BUFFER_X;
    static const char* MATRIX_BUFFER_Y;
    static const char* TMP_BUFFER_X;
    static const char* TMP_BUFFER_Y;
    float* matrixX;
    float* matrixY;
    float divider;
    float sumCoeff;
    unsigned long matrixSize;
    std::string convolutionKernel;
    std::string conversionKernel;
    std::string sumKernel;
    virtual void initSpecific(char *const *argv);
    virtual void workOnImage(int bufferNumber);
    void prepareKernel();
    virtual const char *getKernelName();

private:
    int matrixRank;
    size_t border;
    size_t bufferOrigin[3];
    size_t hostOrigin[3];
    size_t region[3];
    void cleanupResources();
};

}


#endif //KERNELHIVE_SOBELIMAGEPROCESSOR_H
