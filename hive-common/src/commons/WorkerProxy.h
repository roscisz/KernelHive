/*
 * WorkerProxy.h
 *
 *  Created on: 14-05-2012
 *      Author: roy
 */

#ifndef WORKERPROXY_H_
#define WORKERPROXY_H_

#include "../network/NetworkAddress.h"

namespace KernelHive {

class WorkerProxy {
public:
	virtual ~WorkerProxy();
	static WorkerProxy *create(char *type, char *params);
private:
	WorkerProxy(const char *binaryPath, char *params);
	void forkAndExec(const char *binaryPath, char *const argv[]);
};

}

#endif /* WORKERPROXY_H_ */
