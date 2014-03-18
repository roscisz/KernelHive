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

/*
 * Singleton object that manages all threads created in program.
 * Every new thread should be created through this.
 * The thread objects must be run in order of dependencies
 */
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
        void joinThread(pthread_t threadInfo);

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
