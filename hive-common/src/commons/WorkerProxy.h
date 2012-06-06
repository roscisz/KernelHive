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
	static WorkerProxy *create(/* argument for factory method , */char *params);
private:
	WorkerProxy(char *binaryPath, char *params);
	void forkAndExec(char *binaryPath, char *const argv[]);
};

}

#endif /* WORKERPROXY_H_ */
