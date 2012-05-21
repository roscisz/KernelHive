#include <iostream>
#include <unistd.h>

#include "DownloaderThread.h"
#include "commons/Logger.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DownloaderThread::DownloaderThread(NetworkAddress* address, SynchronizedBuffer* buffer)
	: TCPClient(address)
{
	registerListener(this);
	this->buffer = buffer;
	pthread_mutex_init(&stopFlagLock, NULL);
	shouldStop = false;
}

void DownloaderThread::run() {
	Logger::log(INFO, "DownloaderThread listening on socket\n");
	listenOnSocket();
}

void DownloaderThread::onMessage(char* message) {
	Logger::log(INFO, "Received message: %s\n", message);
}

void DownloaderThread::pleaseStop() {
	pthread_mutex_lock(&stopFlagLock);
	shouldStop = true;
	pthread_mutex_unlock(&stopFlagLock);
}

DownloaderThread::~DownloaderThread() {
	// TODO Auto-generated destructor stub
}

} /* namespace KernelHive */
