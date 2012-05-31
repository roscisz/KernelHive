#include <cstring>

#include "DataUploader.h"
#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "commons/Logger.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataUploader::DataUploader(NetworkAddress* address, SynchronizedBuffer* buffer)
	: TCPClient(address, this) {
	this->buffer = buffer;
}

DataUploader::~DataUploader() {
}

void DataUploader::onMessage(TCPMessage* message) {
	// TODO Remove development logging
	if (checkAck(message->data)) {
		byte uploadBuffer[UPLOAD_BATCH];
		buffer->seek(0);
		while (!buffer->isAtEnd()) {
			buffer->read(uploadBuffer, UPLOAD_BATCH);
			sendMessage(uploadBuffer);
		}
		pleaseStop();
	} else {
		sendMessage("PUT");
	}
}

void DataUploader::onConnected() {
	Logger::log(INFO, "Uploader connection established\n");
	sendMessage("PUT");
}

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

const char* DataUploader::ACK_MESSAGE = "OK";

bool DataUploader::checkAck(const char* message) {
	if (strcmp(ACK_MESSAGE, message) == 0) {
		return true;
	} else {
		return false;
	}
}

} /* namespace KernelHive */
