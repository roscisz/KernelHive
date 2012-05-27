#include "ThreadManager.h"
#include "../commons/Logger.h"
#include <signal.h>

namespace KernelHive {

ThreadManager::ThreadManager() {
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
    Logger::log(INFO, "Pthread created [%u]", threadInfo);
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
		Logger::log(FATAL, "pthread_join for thread '%u' failed",
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
}

ThreadManager::~ThreadManager() {
    cleanUpMaps();
}

}
