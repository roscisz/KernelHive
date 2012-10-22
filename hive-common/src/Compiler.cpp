#include <iostream>

#include "commons/KernelCompiler.h"
#include "commons/KernelHiveException.h"
#include "commons/KhUtils.h"

using namespace KernelHive;

int main(int argc, char** argv) {
	if (argc < 2) {
		std::cout << "Source file not provided" << std::endl;
	}

	KernelCompiler compiler;
	std::string rawInput;
	int selection = -1;

	try {
		compiler.loadSource(argv[1]);
		compiler.printDevices();
		std::cout << "Select a device: ";
		std::cin >> rawInput;
		selection = KhUtils::atoi(rawInput.c_str());
	} catch (KernelHiveException& e) {
		std::cout << e.getMessage() << std::endl;
	}
}

