#include <iostream>
#include <fstream>
#include <CL/cl.h>

#include "commons/OpenClHost.h"
#include "commons/WorkerProxy.h"

using namespace KernelHive;

int main(int argc, char** argv) {

	std::cout << OpenClHost::getInstance()->getDevicesInfo() << std::endl;
	WorkerProxy::create("DataPartitioner", "bin 666 localhost 31338 31338 GeForce9400MG 3 0 0 0 512 1 1 64 1 1 4096 localhost 31341 456 localhost 31340 123  2 localhost 31342 localhost 31343");
	//WorkerProxy::create("DataProcessor", "bin 666 localhost 31338 31338 GeForce9400MG 3 0 0 0 512 1 1 64 1 1 2048 localhost 31341 456 localhost 31340 123 localhost 31343");
	//WorkerProxy::create("DataMerger", "bin 666 localhost 31338 31338 GeForce9400MG 3 0 0 0 128 1 1 16 1 1 4 localhost 31341 456 2 localhost 31342 123 localhost 31340 111 localhost 31343");

	return 0;
}
