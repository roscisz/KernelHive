#include <cstring>
#include <sstream>

#include "DataUploader.h"
#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "network/TCPMessage.h"
#include "commons/Logger.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //

const char* DataUploader::PUBLISH_DATA = "0";

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
	// TODO Handle incomming messages
}

void DataUploader::onConnected() {
	Logger::log(INFO, "Uploader connection established\n");
	sendMessage(dataPublish.c_str());
	byte uploadBuffer[UPLOAD_BATCH];
	buffer->seek(0);
	while (!buffer->isAtEnd()) {
		buffer->read(uploadBuffer, UPLOAD_BATCH);
		TCPMessage message(uploadBuffer, UPLOAD_BATCH);
		sendMessage(&message);
	}
	pleaseStop();
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataUploader::prepareCommands() {
	std::stringstream ss;
	ss << PUBLISH_DATA << ' ';
	dataPublish = ss.str();
}

} /* namespace KernelHive */
