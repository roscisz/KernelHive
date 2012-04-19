/*
 * SampleWorker.h
 *
 *  Created on: 19-04-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#ifndef SAMPLEWORKER_H_
#define SAMPLEWORKER_H_

#include "commons/Worker.h"

namespace KernelHive {

class SampleWorker : public Worker {
public:
	SampleWorker(char *clusterHostname, int clusterPort);
	void work();
	virtual ~SampleWorker();
};

}

#endif /* SAMPLEWORKER_H_ */
