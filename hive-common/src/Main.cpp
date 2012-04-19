#include <iostream>
#include <fstream>
#include <CL/cl.h>

#include "commons/OpenClHost.h"

using namespace KernelHive;

int main(int argc, char** argv) {

	std::cout << OpenClHost::getDevicesInfo() << std::endl;

	return 0;
}
