/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
#include <cstdio>
#include <unistd.h>
#include "LoopedThread.h"

namespace KernelHive {

LoopedThread::LoopedThread() {
	pthread_mutex_init(&(this->stopMutex), NULL);
	this->stopFlag = false;
}

void LoopedThread::run() {
	while(true) {
		pthread_mutex_lock(&(this->stopMutex));
		if(this->stopFlag) break;
		pthread_mutex_unlock(&(this->stopMutex));
		this->executeLoopCycle();
	}
	pthread_mutex_unlock(&(this->stopMutex));
}

void LoopedThread::pleaseStop() {
	pthread_mutex_lock(&(this->stopMutex));
	this->stopFlag = true;
	pthread_mutex_unlock(&(this->stopMutex));
}

LoopedThread::~LoopedThread() {
	// TODO Auto-generated destructor stub
}

}
