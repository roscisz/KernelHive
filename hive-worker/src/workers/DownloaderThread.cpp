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
	shouldStop = true;
}

DownloaderThread::DownloaderThread() {
	shouldStop = false;
}

DownloaderThread::~DownloaderThread() {
	// TODO Auto-generated destructor stub
}

} /* namespace KernelHive */
