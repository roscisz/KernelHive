/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Rafal Lewandowski
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
#include <fstream>
#include <CL/cl.h>
#include <cstdlib>

#include "commons/OpenClHost.h"
#include "commons/WorkerProxy.h"
#include "network/TCPClient.h"
#include "threading/ThreadManager.h"

using namespace KernelHive;

int main(int argc, char** argv) {

	std::cout << OpenClHost::getInstance()->getDevicesInfo() << std::endl;

	//WorkerProxy::create("DataPartitioner", "bin 666 localhost 31338 31338 GeForce9400MG 3 0 0 0 512 1 1 64 1 1 4096 localhost 31341 456 localhost 31340 123 2 localhost 31342");
	//WorkerProxy::create("DataProcessor", "bin 666 localhost 31338 31338 GeForce9400MG 3 0 0 0 512 1 1 64 1 1 2048 localhost 31341 456 localhost 31340 123 localhost 31343");
	//WorkerProxy::create("DataMerger", "bin 666 localhost 31338 31338 GeForce9400MG 3 0 0 0 128 1 1 16 1 1 4 localhost 31341 456 2 localhost 31342 123 localhost 31340 111 localhost 31343");

}
