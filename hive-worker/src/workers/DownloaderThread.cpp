#include <iostream>
#include <unistd.h>

#include "DownloaderThread.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

void DownloaderThread::run() {
	// TODO Implement data downloading
}

void DownloaderThread::pleaseStop() {
	pthread_mutex_lock(&stopFlagMutex);
	shouldStop = true;
	pthread_mutex_unlock(&stopFlagMutex);
}

DownloaderThread::DownloaderThread() {
	pthread_mutex_init(&stopFlagMutex, NULL);
	shouldStop = false;
}

DownloaderThread::~DownloaderThread() {
	// TODO Auto-generated destructor stub
}

} /* namespace KernelHive */
