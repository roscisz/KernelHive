/**
 * Copyright (c) 2014 Gdansk University of Technology
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
#include <cstdio>
#include <iostream>

#include "commons/OpenClException.h"
#include "commons/KernelHiveException.h"
#include "network/NetworkAddress.h"
#include "workers/image/ConverterImageProcessor.h"

int main(int argc, char** argv) {
    if(argc < WORKER_STD_ARGS) {
        printf("Arguments missing. Worker binary exits...\n");
        return 0;
    }

    try {
        KernelHive::ConverterImageProcessor *worker = new KernelHive::ConverterImageProcessor(argv);
        worker->work(argv + WORKER_STD_ARGS);
        delete worker;
    }
    catch(const char *str) {
        printf("Error: %s\n", str);
    }
    catch (KernelHive::OpenClException& e) {
        std::cout << e.getMessage() << "(" << e.getOpenClErrorCode() << ")" << std::endl;
    }
    catch (KernelHive::KernelHiveException& e) {
        std::cout << e.getMessage() << std::endl;
    }
}
