/*
 * Worker.h
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#ifndef WORKER_H_
#define WORKER_H_

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
	void setPercentDone(int percentDone);
private:
	int percentDone;
	UDPReporter *reporter;
};

}

#endif /* WORKER_H_ */
