#include "ThreadManager.h"
#include "../commons/Logger.h"

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
}

void ThreadManager::waitForThreads() {
    std::vector<pthread_t>::iterator it;
    for(it = threadInfos->begin(); it != threadInfos->end(); it++) {
        if(pthread_join(*it, NULL) < 0)
        	Logger::log(FATAL, "pthread_join failed");
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


ThreadManager::~ThreadManager() {
    std::vector<Thread*>::iterator it;
    for(it = this->threadObjects->begin(); it != this->threadObjects->end(); it++)
        delete (*it);

    this->threadInfos->clear();

    delete threadInfos;
    delete threadObjects;
}

}
