#include <cstdio>
#include <iostream>

#include "commons/OpenClException.h"
#include "commons/KernelHiveException.h"
#include "network/NetworkAddress.h"
#include "workers/DataPartitioner.h"

int main(int argc, char** argv) {
	if(argc < WORKER_STD_ARGS) {
		printf("Arguments missing. Worker binary exits...\n");
		return 0;
	}

	printf("Hello worker: %s %s\n", argv[1], argv[2]);

	try {
		KernelHive::DataPartitioner *worker = new KernelHive::DataPartitioner(argv);
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



