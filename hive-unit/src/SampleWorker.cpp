/*
 * SampleWorker.cpp
 *
 *  Created on: 19-04-2012
 *      Author: Paweł Rościszewski (roscisz@gmail.com)
 */

#include <unistd.h>
#include "SampleWorker.h"

namespace KernelHive {

SampleWorker::SampleWorker(char *clusterHostname, int clusterPort) : Worker(clusterHostname, clusterPort){
	// TODO Auto-generated constructor stub

}

void SampleWorker::work() {
	int i;
	for(i = 0; i <= 25; i++) {
		this->setPercentDone(4*i);
		sleep(1);
	}
}

SampleWorker::~SampleWorker() {
	// TODO Auto-generated destructor stub
}

}
