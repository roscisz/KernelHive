#ifndef KERNEL_HIVE_DATA_UPLOADER_H
#define KERNEL_HIVE_DATA_UPLOADER_H

#include <string>

#include "network/TCPClient.h"
#include "network/TCPMessage.h"
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

	int getDataIdentifier();

	void getDataURL(std::string *param);

private:
	/** The initial state. */
	static const int STATE_INITIAL = 0;

	/** The identifier of uploaded data has been acquired. */
	static const int STATE_IDENTIFIER_ACQUIRED = 1;

	/** The size to use for the upload buffer. */
	// MTU 1492 for PPPoE - 3 * 4 bytes for protocol overhead
	static const size_t UPLOAD_BATCH = 1480;

	/** The command which allows to publish data in the repository. */
	static const int PUBLISH_DATA = 0;

	/** A command which allows to append data to existing storage. */
	static const int APPEND_DATA = 4;

	/** The current state of this data uploader. */
	int currentState;

	/** Data server address */
	NetworkAddress* address;

	/** A pointer to the buffer with data to be uploaded. */
	SynchronizedBuffer* buffer;

	/** The command which tells the repository that data will be uploaded to it. */
	TCPMessage* dataPublish;

	int *dataPublishData;

	/** The identifier of uploaded data - returned by the repository after uploading. */
	int dataIdentifier;

	/**
	 * Pre-compiles the commands which will be sent to the data repository.
	 */
	void prepareCommands();

	/**
	 * Formats a data append command for sending.
	 *
	 * @param dataId the data identifier
	 * @param packageSize the size of the package to send
	 * @return the formatted prefix of the data append command
	 */
	void prepareDataAppend(int *command, size_t packageSize);

	void copyCommand(byte *buffer, int *command);

	bool acquireDataIdentifier(TCPMessage* message);

	/**
	 * Performs uploading of data.
	 */
	void uploadData();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_UPLOADER_H */
