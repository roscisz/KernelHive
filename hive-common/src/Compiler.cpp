/**
 * Copyright (c) 2014 Gdansk University of Technology
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

#include "commons/KernelCompiler.h"
#include "commons/KernelHiveException.h"
#include "commons/KhUtils.h"
#include "commons/OpenClException.h"

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
		bool result = compiler.compileOnDevice(selection);
		if (result == true) {
			std::cout << "Build successful" << std::endl;
		}
	} catch (KernelHiveException& e) {
		std::cout << e.getMessage() << std::endl;
	} catch (OpenClException& e) {
		std::cout << e.getMessage() << std::endl;
	}
}

