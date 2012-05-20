#include "ThreadManager.h"
#include "../commons/Logger.h"
#include <signal.h>

namespace KernelHive {

ThreadManager::ThreadManager() {
    threadObjects = new std::vector<Thread*>();
    threadInfos = new std::vector<pthread_t>();
}

/*
void ThreadManager::addThread(Thread* threadObject) {
    threadObjects->push_back(threadObject);
}
*/

void ThreadManager::pleaseStopAllThreads() {
    std::vector<Thread *>::iterator it;
    for(it = threadObjects->begin(); it != threadObjects->end(); it++)
        (*it)->pleaseStop();
    ThreadMap::iterator threadIter;
    for (threadIter = threadsMap.begin(); threadIter != threadsMap.end(); threadIter++) {
		Thread* thread = threadIter->second;
    	thread->pleaseStop();
	}
}

void ThreadManager::waitForThreads() {
    std::vector<pthread_t>::iterator it;
    for(it = threadInfos->begin(); it != threadInfos->end(); it++) {
        if(pthread_join(*it, NULL) < 0)
        	Logger::log(FATAL, "pthread_join failed");
    }
    ThreadInfoMap::iterator threadInfoIter;
	for (threadInfoIter = threadInfoMap.begin(); threadInfoIter != threadInfoMap.end(); threadInfoIter++) {
		pthread_t threadInfo = threadInfoIter->second;
		if (pthread_join(threadInfo, NULL) < 0) {
			Logger::log(FATAL, "pthread_join for named thread failed");
		}
	}
}

/*
void ThreadManager::runThreads() {
	Logger::log(DEBUG, "Ok dude im here");
    std::vector<Thread *>::iterator it;
    for(it = threadObjects->begin(); it != threadObjects->end(); it++)
        runThread(*it);
}*/


void *ThreadManager::runThread(void* arg) {
    Thread* threadObject = (Thread*) arg;
    threadObject->run();
}

void ThreadManager::runThread(Thread* thread) {
    pthread_t threadInfo;
    pthread_create(&threadInfo, NULL, &ThreadManager::runThread, (void*) (thread));
    Logger::log(INFO, "Pthread created [%u]", threadInfo);
    threadInfos->push_back(threadInfo);
    threadObjects->push_back(thread);
}

/*
void ThreadManager::abortHandler(int sig) {
	Logger::log(INFO, "Recieved signal [%u], stopping all threads", sig);
    //ThreadManager::GetPtr()->pleaseStopAllThreads();
}*/

void ThreadManager::runThread(std::string& threadName, Thread* threadObject) {
	pthread_t threadInfo;
	pthread_create(&threadInfo, NULL, &ThreadManager::runThread, (void*) (threadObject));
	threadsMap[threadName] = threadObject;
	threadInfoMap[threadName] = threadInfo;
	Logger::log(INFO, "Named pthread '%s' created [%u]", threadName.data(), threadInfo);
}

void ThreadManager::pleaseStopThread(std::string& threadName) {
	ThreadMap::iterator iterator = threadsMap.find(threadName);
	if (iterator != threadsMap.end()) {
		Thread* thread = threadsMap[threadName];
		thread->pleaseStop();
	}
}

void ThreadManager::waitForThread(std::string& threadName) {
	ThreadInfoMap::iterator iterator = threadInfoMap.find(threadName);
	if (iterator != threadInfoMap.end()) {
		pthread_t threadInfo = threadInfoMap[threadName];
		if (pthread_join(threadInfo, NULL) < 0) {
			Logger::log(FATAL, "pthread_join for named thread '%s' failed",
					threadName.data());
		}
	}
}

void ThreadManager::cleanUpMaps() {
	ThreadMap::iterator threadIter;
	for (threadIter = threadsMap.begin(); threadIter != threadsMap.end(); threadIter++) {
		delete threadIter->second;
	}
	threadsMap.clear();
	threadInfoMap.clear();
}

void ThreadManager::connectSignals() {
	//signal(SIGINT, ThreadManager::abortHandler);
	//signal(SIGTERM, ThreadManager::abortHandler);
	//signal(SIGHUP, restartHandler);
}

ThreadManager::~ThreadManager() {
    std::vector<Thread*>::iterator it;
    for(it = this->threadObjects->begin(); it != this->threadObjects->end(); it++)
        delete (*it);

    this->threadInfos->clear();

    delete threadInfos;
    delete threadObjects;

    cleanUpMaps();
}

}
