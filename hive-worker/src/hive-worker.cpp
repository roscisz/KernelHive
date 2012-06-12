#include <cstdio>
#include "network/NetworkAddress.h"
#include "commons/SampleWorker.h"

int main(int argc, char *argv[]) {

	if(argc < 2) {
		printf("Arguments missing. Worker binary exits...\n");
		return 0;
	}

	try {
		KernelHive::SampleWorker *worker = new KernelHive::SampleWorker(argv);
		worker->work(argv + WORKER_STD_ARGS);
		delete worker;
	}
	catch(const char *str) {
		printf("Error: %s\n", str);
	}

	//while(true);
}
