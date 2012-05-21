#ifndef KERNEL_HIVE_DATA_DOWNLOADER_H
#define KERNEL_HIVE_DATA_DOWNLOADER_H

#include <pthread.h>

#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "threading/Thread.h"
#include "threading/SynchronizedBuffer.h"

namespace KernelHive {

/**
 * A thread responsible for downloading data from from a given
 * network location.
 */
class DownloaderThread
	: public Thread, public TCPClient, public TCPClientListener {

public:
	/**
	 * Instantiates this downloader thread.
	 */
	DownloaderThread(NetworkAddress* address, SynchronizedBuffer* buffer);

	/**
	 * Deallocates resources used by this downloader thread.
	 */
	virtual ~DownloaderThread();

	/**
	 * Implements this thread's processing logic.
	 */
	void run();

	/**
	 * Can be called by other threads to notify this thread that
	 * it should stop it's execution.
	 */
	void pleaseStop();

	/**
	 * Called upon receiving data via the socket.
	 *
	 * @param message the received message
	 */
	void onMessage(char* message);

private:
	/** A variable which indicates that this thread should stop execution. */
	bool shouldStop;

	/** A lock for the stop flag. */
	pthread_mutex_t stopFlagLock;

	/** A pointer to the buffer in which the downloaded data should be stored. */
	SynchronizedBuffer* buffer;

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_DOWNLOADER_H */
