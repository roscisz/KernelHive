/*
 * LoopedThread.cpp
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#include <cstdio>
#include <unistd.h>
#include "LoopedThread.h"

namespace KernelHive {

LoopedThread::LoopedThread() {
	pthread_mutex_init(&(this->stopMutex), NULL);
	this->stopFlag = false;
}

void LoopedThread::run() {
	while(!(this->stopFlag)) {
		pthread_mutex_lock(&(this->stopMutex));
		this->executeLoopCycle();
		pthread_mutex_unlock(&(this->stopMutex));
		// FIXME: without this sleep, the mutex can't be locked by pleaseStop...
		sleep(1);
	}
}

void LoopedThread::pleaseStop() {
	pthread_mutex_lock(&(this->stopMutex));
	this->stopFlag = true;
	pthread_mutex_unlock(&(this->stopMutex));
}

LoopedThread::~LoopedThread() {
	// TODO Auto-generated destructor stub
}

}
