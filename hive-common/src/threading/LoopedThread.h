/*
 * LoopedThread.h
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#ifndef LOOPEDTHREAD_H_
#define LOOPEDTHREAD_H_

#include <pthread.h>

#include "Thread.h"

namespace KernelHive {

class LoopedThread : public Thread {
public:
	LoopedThread();
	virtual ~LoopedThread();
protected:
	virtual void executeLoopCycle() = 0;
	void pleaseStop();
private:
	void run();
	bool stopFlag;
	//pthread_mutex_t stopMutex;
};

}

#endif /* LOOPEDTHREAD_H_ */
