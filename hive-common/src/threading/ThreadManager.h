/*
 * Singleton object that manages all threads created in program.
 * Every new thread should be created through this.
 * The thread objects must be run in order of dependencies
 */
#ifndef THREADMANAGER_H_
#define THREADMANAGER_H_

#include <pthread.h>
#include <vector>
#include <map>
#include <string>
#include "Thread.h"
#include "../commons/Singleton.h"

namespace KernelHive {

/** A type wrapper for std::map of Thread object pointers. */
typedef std::map<Thread*, pthread_t> ThreadMap;

class ThreadManager : public Singleton<ThreadManager> {
    public:
        ThreadManager();
        ~ThreadManager();

        //void addThread(Thread* threadObject);
        //void runThreads();
        void pleaseStopAllThreads();
        void waitForThreads();
        void runThread(Thread* threadObject);
        void pleaseStopThread(Thread *threadObject);
        void waitForThread(Thread *threadObject);

        // Utility methods:
        void forkAndExitParent();

    private:
        static void *runThread(void *arg);
        void connectSignals();
        //void abortHandler(int sig);
        
        /**
         * Deallocates all resources used by the thread and thread maps.
         */
        void cleanUpMaps();

        /** Holds the mappings for Thread object pointers. */
        ThreadMap threadMap;
};

}

#endif /* THREADMANAGER_H_ */
