#include <cstdio>
#include "communication/DataDownloaderGridFs.h"
#include "network/NetworkAddress.h"

int main(int argc, char** argv) {
	KernelHive::NetworkAddress *address = new KernelHive::NetworkAddress("172.20.0.75", 27017);
	KernelHive::SynchronizedBuffer buffer;
	KernelHive::DataDownloaderGridFs sut = KernelHive::DataDownloaderGridFs(address, "12", &buffer);
	sut.run();
	buffer.logMyFloatData();
}
