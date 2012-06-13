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

const char* DataUploader::APPEND_DATA = "4";

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
		buffer->seek(0);
		currentState = STATE_IDENTIFIER_ACQUIRED;
		uploadData();
		pleaseStop();
		break;

	case STATE_IDENTIFIER_ACQUIRED:
		uploadData();
		pleaseStop();
		break;
	}
}

void DataUploader::onConnected() {
	Logger::log(INFO, "Uploader connection established\n" );
	switch(currentState) {
	case STATE_INITIAL:
		sendMessage(dataPublish.c_str());
		break;

	case STATE_IDENTIFIER_ACQUIRED:
		uploadData();
		pleaseStop();
		break;
	}
}

std::string* DataUploader::getDataIdentifier() {
	return &dataIdentifier;
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataUploader::uploadData() {
	byte* uploadBuffer = NULL;
	while (!buffer->isAtEnd()) {
		size_t amount = buffer->getSize() - buffer->getOffset();
		size_t uploadPackageSize = amount < UPLOAD_BATCH ? amount : UPLOAD_BATCH;
		std::string uploadCmd = formatDataAppend(dataIdentifier, uploadPackageSize);
		size_t cmdSize = uploadCmd.size();
		size_t msgSize = cmdSize + uploadPackageSize;

		uploadBuffer = new byte[msgSize];
		strcpy(uploadBuffer, uploadCmd.c_str());
		buffer->read(uploadBuffer + cmdSize, uploadPackageSize);
		TCPMessage message(uploadBuffer, msgSize);
		sendMessage(&message);
		Logger::log(DEBUG, ">>>>>> SENT %u BYTES\n", msgSize);

		// TODO Find out why this is necessary
		sleep(1);

		delete [] uploadBuffer;
	}
}

void DataUploader::prepareCommands() {
	std::stringstream ss;
	ss << PUBLISH_DATA << ' ' << buffer->getSize();
	dataPublish = ss.str();
}

std::string DataUploader::formatDataAppend(std::string dataId, size_t packageSize) {
	std::stringstream dataAppend;

	dataAppend << APPEND_DATA << ' ';
	dataAppend << dataId << ' ';
	dataAppend << packageSize << ' ';

	return dataAppend.str();
}

} /* namespace KernelHive */
