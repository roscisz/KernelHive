#include <iostream>
#include <unistd.h>
#include <cstdlib>

#include "DataDownloader.h"
#include "commons/Logger.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataDownloader::DataDownloader(NetworkAddress* address, SynchronizedBuffer* buffer)
	: TCPClient(address, this)
{
	this->buffer = buffer;
	currentState = STATE_INITIAL;
}

void DataDownloader::onMessage(TCPMessage* message) {
	// TODO Remove development logging
	switch (currentState) {
	case STATE_INITIAL:
		if (acquireDataSize(message->data)) {
			Logger::log(INFO, "Data size has been acquired: %u\n", totalDataSize);
			buffer->allocate(totalDataSize);
			currentState = STATE_SIZE_ACQUIRED;
			sendMessage("GET");
		}
		break;

	case STATE_SIZE_ACQUIRED:
		buffer->append(message->data, message->nBytes);
		progressSize += message->nBytes;
		Logger::log(INFO, "%d/%d bytes available\n", progressSize, totalDataSize);
		if (!(progressSize < totalDataSize)) {
			currentState = STATE_DATA_ACQUIRED;
			pleaseStop();
		}
		break;

	case STATE_DATA_ACQUIRED:
		Logger::log(DEBUG, "All data has been acquired..");
		break;
	}
}

void DataDownloader::onConnected() {
	Logger::log(INFO, "Connection established\n");
	sendMessage("SIZE");
}

DataDownloader::~DataDownloader() {
	// TODO Auto-generated destructor stub
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

bool DataDownloader::acquireDataSize(const char* sizeString) {
	int size = atoi(sizeString);
	if (size > 0) {
		totalDataSize = (size_t) size;
		return true;
	} else {
		return false;
	}
}

} /* namespace KernelHive */
