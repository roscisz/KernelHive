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
#include <vector>
#include <string>
#include <sstream>
#include "WorkerProxy.h"

namespace KernelHive {

WorkerProxy::WorkerProxy(char *binaryPath, char *params) {
	std::vector<char *> args;
	std::istringstream iss(params);

	std::string token;
	while(iss >> token) {
	  char *arg = new char[token.size() + 1];
	  copy(token.begin(), token.end(), arg);
	  arg[token.size()] = '\0';
	  args.push_back(arg);
	}
	args.push_back(0);

	forkAndExec(binaryPath, &args[0]);

	for(size_t i = 0; i < args.size(); i++)
	  delete[] args[i];
}

void WorkerProxy::forkAndExec(char *binaryPath, char *const argv[]) {
	if(fork()) return;

	if(execv(binaryPath, argv))
		printf("Execv error: %s", strerror(errno));
}

WorkerProxy *WorkerProxy::create(char *params) {
	// TODO: worker factory
	return new WorkerProxy("../build/hive-worker/DataProcessor", params);
}

WorkerProxy::~WorkerProxy() {
	// TODO Auto-generated destructor stub
}

}
