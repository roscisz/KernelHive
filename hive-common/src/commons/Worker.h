/*
 * Worker.h
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#ifndef WORKER_H_
#define WORKER_H_

#include "../threading/ThreadManager.h"
#include "../network/NetworkAddress.h"
#include "../network/UDPReporter.h"
#include "../network/IReportable.h"

namespace KernelHive {

class Worker : public IReportable {
public:
	Worker(NetworkAddress *clusterAddress);
	int getPercentDone();
	virtual void work(char *const argv[]) = 0;
	virtual ~Worker();
protected:
	/** The thread manager - allows to run and control thread operations. */
	ThreadManager* threadManager;

	/** An index variable used to extract worker parameters from the parameters array. */
	int paramOffset;

	/**
	 * Returns the parameter pointed by the paramOffset and increments
	 * the paramOffset value.
	 *
	 * @param params the worker parameters array
	 * @return the parameter which was pointed by the paramOffset
	 */
	char* nextParam(char *const argv[]);

	void setPercentDone(int percentDone);
private:
	int percentDone;
	UDPReporter *reporter;
};

}

#endif /* WORKER_H_ */
