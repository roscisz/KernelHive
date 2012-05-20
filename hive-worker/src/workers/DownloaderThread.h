#ifndef KERNEL_HIVE_DATA_DOWNLOADER_H
#define KERNEL_HIVE_DATA_DOWNLOADER_H

#include "threading/Thread.h"

namespace KernelHive {

/**
 * A thread responsible for downloading data from from a given
 * network location.
 */
class DownloaderThread : public Thread {

public:
	/**
	 * Instantiates this downloader thread.
	 */
	DownloaderThread();

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

private:
	/** A variable which indicates that this thread should stop execution. */
	bool shouldStop;

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_DOWNLOADER_H */
