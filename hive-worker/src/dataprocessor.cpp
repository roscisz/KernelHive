#include <cstdio>
#include <iostream>

#include "commons/OpenClException.h"
#include "commons/KernelHiveException.h"
#include "network/NetworkAddress.h"
#include "workers/DataProcessor.h"

int main(int argc, char** argv) {
	if(argc < WORKER_STD_ARGS) {
		printf("Arguments missing. Worker binary exits...\n");
		return 0;
	}

	try {
		KernelHive::DataProcessor *worker = new KernelHive::DataProcessor(argv);
		worker->work(argv + WORKER_STD_ARGS);
		delete worker;
	}
	catch(const char *str) {
		printf("Error: %s\n", str);
	}
	catch (KernelHive::OpenClException& e) {
		std::cout << e.getMessage() << "(" << e.getOpenClErrorCode() << ")" << std::endl;
	}
	catch (KernelHive::KernelHiveException& e) {
		std::cout << e.getMessage() << std::endl;
	}
}



