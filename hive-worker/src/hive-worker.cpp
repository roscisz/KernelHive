#include <cstdio>
#include "network/NetworkAddress.h"
#include "commons/SampleWorker.h"

int main(int argc, char *argv[]) {

	if(argc < 2) {
		printf("Arguments missing. Worker binary exits...\n");
		return 0;
	}

	printf("Hello worker: %s %s\n", argv[1], argv[2]);

	NetworkAddress *address = new NetworkAddress(argv[1], argv[2]);

	printf("%s %d\n", address->host, address->port);

	try {
		KernelHive::SampleWorker *worker = new KernelHive::SampleWorker(address);
		worker->work();
		delete worker;
	}
	catch(const char *str) {
		printf("Error: %s\n", str);
	}

	//while(true);
}
