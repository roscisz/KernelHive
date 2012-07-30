#include <iostream>
#include <fstream>
#include <CL/cl.h>

#include "commons/OpenClHost.h"
#include "commons/WorkerProxy.h"

using namespace KernelHive;

int main(int argc, char** argv) {

	std::cout << OpenClHost::getInstance()->getDevicesInfo() << std::endl;
	WorkerProxy::create("bin 666 localhost 31338 localhost 31338 GeForce9400MG 1 0 4096 64 localhost 31341 456 localhost 31340 123 localhost 31340");

	return 0;
}
