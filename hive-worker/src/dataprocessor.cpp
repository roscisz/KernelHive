#include <cstdio>
#include <iostream>

#include "commons/OpenClException.h"
#include "commons/KernelHiveException.h"
#include "network/NetworkAddress.h"
#include "workers/DataProcessor.h"

int main(int argc, char** argv) {
	if(argc < 2) {
		printf("Arguments missing. Worker binary exits...\n");
		return 0;
	}

	printf("Hello worker: %s %s\n", argv[1], argv[2]);

	NetworkAddress *address = new NetworkAddress(argv[1], argv[2]);

	printf("%s %d\n", address->host, address->port);

	try {
		KernelHive::DataProcessor *worker = new KernelHive::DataProcessor(address);
		worker->work(argv + 3);
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



