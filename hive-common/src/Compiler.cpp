#include <iostream>

#include "commons/KernelCompiler.h"
#include "commons/KernelHiveException.h"

using namespace KernelHive;

int main(int argc, char** argv) {
	if (argc < 2) {
		std::cout << "Source file not provided" << std::endl;
	}

	KernelCompiler compiler;

	try {
		compiler.loadSource(argv[1]);
	} catch (KernelHiveException& e) {
		std::cout << e.getMessage() << std::endl;
	}
}

