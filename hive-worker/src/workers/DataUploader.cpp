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
	this->currentState = STATE_INITIAL;
	prepareCommands();
}

DataUploader::~DataUploader() {
}

void DataUploader::onMessage(TCPMessage* message) {
	switch(currentState) {
	case STATE_INITIAL:
		dataIdentifier = message->data;
		currentState = STATE_IDENTIFIER_ACQUIRED;
		break;

	case STATE_IDENTIFIER_ACQUIRED:
		break;
	}
}

void DataUploader::onConnected() {
	Logger::log(INFO, "Uploader connection established (%s)\n", dataPublish.c_str());
	sendMessage(dataPublish.c_str());
	byte* uploadBuffer = NULL;//[UPLOAD_BATCH];
	buffer->seek(0);
	while (!buffer->isAtEnd()) {
		size_t amount = buffer->getSize() - buffer->getOffset();
		size_t readSize = amount < UPLOAD_BATCH ? amount : UPLOAD_BATCH;
		Logger::log(INFO, "WILL SEND %u\n", readSize);
		uploadBuffer = new byte[readSize];
		buffer->read(uploadBuffer, readSize);
		TCPMessage message(uploadBuffer, readSize);
		sendMessage(&message);
		delete [] uploadBuffer;
	}
	pleaseStop();
}

std::string* DataUploader::getDataIdentifier() {
	return &dataIdentifier;
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataUploader::prepareCommands() {
	std::stringstream ss;
	ss << PUBLISH_DATA << ' ' << buffer->getSize() << ' ';
	dataPublish = ss.str();
}

} /* namespace KernelHive */
