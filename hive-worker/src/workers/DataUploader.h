#ifndef KERNEL_HIVE_DATA_UPLOADER_H
#define KERNEL_HIVE_DATA_UPLOADER_H

#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "threading/SynchronizedBuffer.h"

namespace KernelHive {

class DataUploader
	: public TCPClient, public TCPClientListener {

public:
	/** The size to use for the upload buffer. */
	static const size_t UPLOAD_BATCH = 1024;

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
	static const char* ACK_MESSAGE;

	/** A pointer to the buffer with data to be uploaded. */
	SynchronizedBuffer* buffer;

	/**
	 * Checks if the message is an ack from the server.
	 */
	bool checkAck(const char* message);

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_UPLOADER_H */
