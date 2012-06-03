#ifndef KERNEL_HIVE_DATA_UPLOADER_H
#define KERNEL_HIVE_DATA_UPLOADER_H

#include <string>

#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "threading/SynchronizedBuffer.h"

namespace KernelHive {

class DataUploader
	: public TCPClient, public TCPClientListener {

public:
	/**
	 * Initializes this data uploader.
	 *
	 * @param address the data repository address
	 * @param buffer the buffer with data to upload
	 */
	DataUploader(NetworkAddress* address, SynchronizedBuffer* buffer);

	/**
	 * The destructor
	 */
	virtual ~DataUploader();

	/**
	 * Called upon receiving data via the socket.
	 *
	 * @param message the received message
	 */
	void onMessage(TCPMessage* message);

	/**
	 * Called when a connection is established.
	 */
	void onConnected();

private:
	/** The size to use for the upload buffer. */
	static const size_t UPLOAD_BATCH = 1024;

	/** The command which allows to publish data in the repository. */
	static const char* PUBLISH_DATA;

	/** A pointer to the buffer with data to be uploaded. */
	SynchronizedBuffer* buffer;

	/** The command which tells the repository that data will be uploaded to it. */
	std::string dataPublish;

	/**
	 * Pre-compiles the commands which will be sent to the data repository.
	 */
	void prepareCommands();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_UPLOADER_H */
