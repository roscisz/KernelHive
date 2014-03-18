/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Rafal Lewandowski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
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
#include "Logger.h"

namespace KernelHive {

WorkerProxy::WorkerProxy(const char *binaryPath, char *params) {
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

void WorkerProxy::forkAndExec(const char *binaryPath, char *const argv[]) {
	if(fork()) return;

	if(execv(binaryPath, argv))
		Logger::log(FATAL, "Execv error: %s", strerror(errno));
}

WorkerProxy *WorkerProxy::create(char *type, char *params) {
	// FIXME: Worker binary path from configuration
	std::string path = "../hive-worker/";
	path.append(type);
	return new WorkerProxy(path.c_str(), params);
}

WorkerProxy::~WorkerProxy() {
	// TODO Auto-generated destructor stub
}

}
