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
typedef std::map<std::string, Thread*> ThreadMap;

/** A type wrapper for std::map of pthread_t variables. */
typedef std::map<std::string, pthread_t> ThreadInfoMap;

class ThreadManager : public Singleton<ThreadManager> {
    public:
        ThreadManager();
        ~ThreadManager();

        //void addThread(Thread* threadObject);
        //void runThreads();
        void pleaseStopAllThreads();
        void waitForThreads();
        void runThread(Thread* threadObject);
        
        /**
         * Runs the given thread in this thread manager, associating it
         * with a provided name.
         *
         * @param threadName the name of the thread to run
         * @param threadObject the thread to run
         */
        void runThread(std::string& threadName, Thread* threadObject);

        /**
         * Asks the thread identified by the provided name to stop.
         *
         * @param threadName the name of the thread to ask for finishing
         */
        void pleaseStopThread(std::string& threadName);

        /**
         * Waits for the thread identified by the provided name to finish.
         *
         * @param threadName the name of a thread to wait for
         */
        void waitForThread(std::string& threadNname);

        // Utility methods:
        void forkAndExitParent();

    private:
        static void *runThread(void *arg);
        void connectSignals();
        //void abortHandler(int sig);
        
        /**
         * Deallocates all resources used by the thread and thread info
         * maps.
         */
        void cleanUpMaps();

        /** Holds the mappings for Thread object pointers. */
        ThreadMap threadsMap;

        /** Holds the mappings for pthread_t variables of threads. */
        ThreadInfoMap threadInfoMap;

        std::vector<Thread *>* threadObjects;
        std::vector<pthread_t>* threadInfos;
};

}

#endif /* THREADMANAGER_H_ */
