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
	Logger::log(INFO, "%s\n", message->data);
	switch (currentState) {
	case STATE_INITIAL:
		if (acquireDataSize(message->data)) {
			Logger::log(DEBUG, "Data size has been acquired: %u", totalDataSize);
			buffer->allocate(totalDataSize);
			currentState = STATE_SIZE_ACQUIRED;
		}
		break;

	case STATE_SIZE_ACQUIRED:
		buffer->append(message->data, message->nBytes);
		progressSize += message->nBytes;
		if (!(progressSize < totalDataSize)) {
			currentState = STATE_DATA_ACQUIRED;
		}
		break;

	case STATE_DATA_ACQUIRED:
		Logger::log(DEBUG, "All data has been acquired..");
		break;
	}
}

void DataDownloader::onConnected() {
	// TODO Remove development logging
	Logger::log(INFO, "Connection established\n");
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
