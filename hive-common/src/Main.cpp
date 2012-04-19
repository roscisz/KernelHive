#include <iostream>
#include <fstream>

#include "commons/OpenClPlatform.h"

using namespace KernelHive;

int main(int argc, char** argv) {

	std::cout << OpenClPlatform::getAvailablePlatformsCount() << std::endl;

	return 0;
}
