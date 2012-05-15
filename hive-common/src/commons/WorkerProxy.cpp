/*
 * WorkerProxy.cpp
 *
 *  Created on: 14-05-2012
 *      Author: roy
 */

#include <unistd.h>
#include <cstdio>
#include <string.h>
#include <errno.h>
#include <cstdlib>
#include "WorkerProxy.h"

namespace KernelHive {

WorkerProxy::WorkerProxy(char *binaryPath, NetworkAddress *clusterAddress) {
	char *params[4];

	params[1] = clusterAddress->host;
	params[2] = (char *) calloc(5, sizeof(char));
	sprintf(params[2], "%d", clusterAddress->port);
	// params[4] = kernel+data

	forkAndExec(binaryPath, params);
}

void WorkerProxy::forkAndExec(char *binaryPath, char *const argv[]) {
	if(fork()) return;

	if(execv(binaryPath, argv))
		printf("Execv error: %s", strerror(errno));
}

WorkerProxy *WorkerProxy::create(NetworkAddress *clusterAddress) {

	return new WorkerProxy("../hive-worker/Debug/hive-worker", clusterAddress);
}

WorkerProxy::~WorkerProxy() {
	// TODO Auto-generated destructor stub
}

}
