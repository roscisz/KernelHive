#include <iostream>
#include <fstream>
#include <CL/cl.h>

#include "commons/OpenClHost.h"
#include "commons/WorkerProxy.h"

using namespace KernelHive;

int main(int argc, char** argv) {

	std::cout << OpenClHost::getInstance()->getDevicesInfo() << std::endl;
	WorkerProxy::create("localhost 31338 localhost 31340 123 localhost 9012 456 GeForce9400MG 1 0 4096 64");

	return 0;
}
