#include <cstdio>

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
		worker->work(argv);
		delete worker;
	}
	catch(const char *str) {
		printf("Error: %s\n", str);
	}
}



