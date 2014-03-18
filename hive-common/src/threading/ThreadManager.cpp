/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Rafal Lewandowski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
#include "ThreadManager.h"
#include "../commons/Logger.h"
#include <signal.h>
#include <cstdlib>

namespace KernelHive {

ThreadManager::ThreadManager() {
    this->connectSignals();
}

void ThreadManager::pleaseStopAllThreads() {
    ThreadMap::iterator threadIter;
    for (threadIter = threadMap.begin(); threadIter != threadMap.end(); threadIter++) {
		Thread* thread = threadIter->first;
    	thread->pleaseStop();
	}
}

void ThreadManager::waitForThreads() {
    ThreadMap::iterator threadIter;
	for (threadIter = threadMap.begin(); threadIter != threadMap.end(); threadIter++) {
		pthread_t threadInfo = threadIter->second;
		joinThread(threadInfo);
	}
	threadMap.clear();
}

void *ThreadManager::runThread(void* arg) {
    Thread* threadObject = (Thread*) arg;
    threadObject->run();
}

void ThreadManager::runThread(Thread* threadObject) {
    pthread_t threadInfo;
    pthread_create(&threadInfo, NULL, &ThreadManager::runThread, (void*) (threadObject));
    threadMap[threadObject] = threadInfo;
    Logger::log(INFO, "Pthread created [%u]\n", threadInfo);
}

void ThreadManager::pleaseStopThread(Thread *threadObject) {
	threadObject->pleaseStop();
}

void ThreadManager::waitForThread(Thread *threadObject) {
	ThreadMap::iterator iterator = threadMap.find(threadObject);
	if (iterator != threadMap.end()) {
		pthread_t threadInfo = threadMap[threadObject];
		joinThread(threadInfo);
		threadMap.erase(threadObject);
	}
}

void ThreadManager::joinThread(pthread_t threadInfo) {
	if (pthread_join(threadInfo, NULL) < 0) {
		Logger::log(FATAL, "pthread_join for thread '%u' failed\n",
				threadInfo);
	}
}

void ThreadManager::cleanUpMaps() {
	ThreadMap::iterator threadIter;
	for (threadIter = threadMap.begin(); threadIter != threadMap.end(); threadIter++) {
		delete threadIter->first;
	}
	threadMap.clear();
}

void ThreadManager::connectSignals() {
	//signal(SIGINT, ThreadManager::abortHandler);
	//signal(SIGTERM, ThreadManager::abortHandler);
	//signal(SIGHUP, restartHandler);
    struct sigaction sa;
    sa.sa_handler = SIG_IGN;
    sa.sa_flags = SA_NOCLDWAIT;
    if (sigaction(SIGCHLD, &sa, NULL) == -1) {
	    Logger::log(FATAL, "sigaction failed\n");
	    exit(1);
    }
}

ThreadManager::~ThreadManager() {
    cleanUpMaps();
}

}
