/*
 * Singleton object that manages all threads created in program.
 * Every new thread should be created through this.
 * The thread objects must be run in order of dependencies
 */
#ifndef THREADMANAGER_H_
#define THREADMANAGER_H_

#include <pthread.h>
#include <vector>
#include "Thread.h"
#include "../commons/Singleton.h"

namespace KernelHive {

class ThreadManager : public Singleton<ThreadManager> {
    public:
        ThreadManager();
        ~ThreadManager();

        //void addThread(Thread* threadObject);
        //void runThreads();
        void pleaseStopAllThreads();
        void waitForThreads();
        void runThread(Thread* threadObject);
        
        // Utility methods:
        void forkAndExitParent();

    private:
        static void *runThread(void *arg);
        void connectSignals();
        //void abortHandler(int sig);
        
        std::vector<Thread *>* threadObjects;
        std::vector<pthread_t>* threadInfos;
};

}

#endif /* THREADMANAGER_H_ */
