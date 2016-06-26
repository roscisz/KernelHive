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


#ifndef KERNELHIVE_VIDEOENCODER_H
#define KERNELHIVE_VIDEOENCODER_H

#include "../BasicWorker.h"

namespace KernelHive {

class ImageMerger: public BasicWorker {
public:
    ImageMerger(char **argv);
    virtual ~ImageMerger();
    virtual void work(char *const argv[]);

protected:
    std::string merge();
    virtual void init(char* const argv[]);

private:
    std::string fileExtension;
    std::string internalExtension;
    int bufferHistory;
    std::string fourcc;
    int fps;
    int frameHeight;
    int frameWidth;
};

}

#endif //KERNELHIVE_VIDEOENCODER_H
