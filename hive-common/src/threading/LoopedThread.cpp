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
	while(true) {
		pthread_mutex_lock(&(this->stopMutex));
		if(this->stopFlag) break;
		pthread_mutex_unlock(&(this->stopMutex));
		this->executeLoopCycle();

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
