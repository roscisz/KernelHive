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

#ifndef KERNELHIVE_IMAGEPROCESSOR_H
#define KERNELHIVE_IMAGEPROCESSOR_H

#include "../OpenCLWorker.h"

namespace KernelHive {

class ImageProcessor: public OpenCLWorker {

public:
    ImageProcessor(char **argv);
    virtual ~ImageProcessor();

protected:
    static const char* MATRIX_BUFFER;
    SynchronizedBuffer* previewBuffer;
    int imageWidth;
    int imageHeight;
    int internalChannels;
    size_t frameSize;
    size_t frames;

    virtual void initSpecific(char *const *argv);
    virtual void workSpecific();
    virtual void workOnImage(int bufferNumber) = 0;
    virtual void prepareKernel() = 0;

private:
    int bufferHistory;
    int internalFcc;
    int internalFps;
    std::string internalExtension;
    void cleanupResources();

};

}

#endif //KERNELHIVE_IMAGEPROCESSOR_H
