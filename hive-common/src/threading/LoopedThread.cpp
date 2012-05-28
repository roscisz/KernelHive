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
	//pthread_mutexattr_t attr;
	//pthread_mutexattr_settype(&attr, PTHREAD_MUTEX_RECURSIVE);
	//pthread_mutex_init(&(this->stopMutex), &attr);
	this->stopFlag = false;
}

void LoopedThread::run() {
	while(!(this->stopFlag)) {
		//pthread_mutex_lock(&(this->stopMutex));
		this->executeLoopCycle();
		//pthread_mutex_unlock(&(this->stopMutex));
	}
}

void LoopedThread::pleaseStop() {
	//pthread_mutex_lock(&(this->stopMutex));
	this->stopFlag = true;
	//pthread_mutex_unlock(&(this->stopMutex));
}

LoopedThread::~LoopedThread() {
	// TODO Auto-generated destructor stub
}

}
