#include <iostream>
#include <fstream>

#include "commons/OpenClPlatform.h"

using namespace KernelHive;

int main(int argc, char** argv) {

	std::cout << OpenClPlatform::getGpuDevicesInfo() << std::endl;
	std::cout << OpenClPlatform::getCpuDevicesInfo() << std::endl;

	return 0;
}
