#include <cstdlib>
#include <iostream>
#include <fstream>

#include "KernelCompiler.h"
#include "KhUtils.h"
#include "OpenClHost.h"

namespace KernelHive {

KernelCompiler::KernelCompiler() {
	executionContext = NULL;
	initDeviceMappings();
}

KernelCompiler::~KernelCompiler() {
	if (executionContext != NULL) {
		delete executionContext;
	}
}

void KernelCompiler::loadSource(char *sourceFile) {
	std::ifstream inputFile;
	inputFile.open(sourceFile);
	sourceCode = KhUtils::readStream(inputFile);
	inputFile.close();
}

void KernelCompiler::initDeviceMappings() {
	int mapping = 1;
	OpenClHost *host = OpenClHost::getInstance();
	OpenClPlatform **platforms = host->getPlatforms();
	for (int i = 0; i < host->getPlatformsCount(); i++) {
		OpenClPlatform *platform = platforms[i];
		OpenClDevice **devices = platform->getCpuDevices();
		for (int j = 0; j < platform->getCpuDevicesCount(); j++) {
			idMappings[mapping] = devices[j];
			mapping++;
		}
		devices = platform->getGpuDevices();
		for (int j = 0; j < platform->getGpuDevicesCount(); j++) {
			idMappings[mapping] = devices[j];
			mapping++;
		}
	}
}

void KernelCompiler::printDevices() {
	for (IdDevicesMap::iterator it = idMappings.begin(); it != idMappings.end(); it++ ) {
		std::cout << it->first << ". ";
		std::cout << it->second->getDeviceName();
		std::cout << " (" << it->second->getDeviceVendor() << ")" << std::endl;
	}
}

} /* namespace KernelHive */
