#include <cstdlib>
#include <iostream>
#include <fstream>

#include "KernelCompiler.h"
#include "KhUtils.h"

namespace KernelHive {

KernelCompiler::KernelCompiler() {
	executionContext = NULL;
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

} /* namespace KernelHive */
