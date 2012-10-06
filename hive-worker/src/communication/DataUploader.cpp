#include <cstdio>
#include <cstring>
#include <sstream>

#include "DataUploader.h"
#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "network/TCPMessage.h"
#include "commons/Logger.h"
#include "commons/KhUtils.h"
#include "commons/KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataUploader::DataUploader(NetworkAddress* address, SynchronizedBuffer* buffer) :
		TCPClient(address, this) {
	this->address = address;
	this->buffer = buffer;
	this->currentState = STATE_INITIAL;
	prepareCommands();
	Logger::log(DEBUG, ">>> UPLOADER s:d WILL LOG DATA HE GOT\n");
	buffer->logMyFloatData();
	Logger::log(DEBUG, ">>> UPLOADER s:d FINISHED LOGGING DATA HE GOT\n");
}

DataUploader::~DataUploader() {
}

void DataUploader::onMessage(TCPMessage* message) {
	switch (currentState) {
	case STATE_INITIAL:
		if (acquireDataIdentifier(message) == true) {
			buffer->seek(0);
			currentState = STATE_IDENTIFIER_ACQUIRED;
			uploadData();
			pleaseStop();
		}
		break;

	case STATE_IDENTIFIER_ACQUIRED:
		uploadData();
		pleaseStop();
		break;
	}
}

void DataUploader::onConnected() {
	Logger::log(INFO, "Uploader connection established\n");
	switch (currentState) {
	case STATE_INITIAL:
		sendMessage(dataPublish);
		break;

	case STATE_IDENTIFIER_ACQUIRED:
		uploadData();
		pleaseStop();
		break;
	}
}

int DataUploader::getDataIdentifier() {
	return dataIdentifier;
}

void DataUploader::getDataURL(std::string *param) {
	std::stringstream ret;

	ret << address->host;
	ret << " ";
	ret << KhUtils::itoa(address->port);
	ret << " ";
	ret << dataIdentifier;

	param->append(ret.str());
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataUploader::uploadData() {
	byte* uploadBuffer = NULL;
	while (!buffer->isAtEnd()) {
		size_t amount = buffer->getSize() - buffer->getOffset();
		size_t uploadPackageSize =
				amount < UPLOAD_BATCH ? amount : UPLOAD_BATCH;
		int *uploadCmd = new int[3];
		prepareDataAppend(uploadCmd, uploadPackageSize);
		size_t cmdSize = 3 * sizeof(int); // Pawel tu był (TCP_COMMAND_SIZE sie nie nadawał)
		size_t msgSize = cmdSize + uploadPackageSize;

		uploadBuffer = new byte[msgSize];
		//strcpy(uploadBuffer, uploadCmd.c_str());
		// FIXME:
		//copyCommand(uploadBuffer, uploadCmd);
		byte *tmp = (byte *)uploadCmd;
		for (int i = 0; i < cmdSize; i++) {
			uploadBuffer[i] = tmp[i];
		}

		buffer->read(uploadBuffer + cmdSize, uploadPackageSize);
		TCPMessage message(uploadBuffer, msgSize);
		sendMessage(&message);
		Logger::log(DEBUG, ">>>>>> SENT %u BYTES\n", msgSize);

		// TODO Find out why this is necessary
		sleep(1);

		delete[] uploadBuffer;
	}
}

void DataUploader::prepareCommands() {
	dataPublishData = new int[2];
	dataPublishData[0] = PUBLISH_DATA;
	dataPublishData[1] = buffer->getSize();
	dataPublish = new TCPMessage((byte *)dataPublishData, 2*sizeof(int));
}

void DataUploader::prepareDataAppend(int *command, size_t packageSize) {
	command[0] = APPEND_DATA;
	command[1] = dataIdentifier;
	command[2] = packageSize;
}

void DataUploader::copyCommand(byte *buffer, int *command) {
	byte *tmp = (byte *)command;
	for (int i = 0; i < TCP_COMMAND_SIZE; i++) {
		buffer[i] = tmp[i];
	}
}

bool DataUploader::acquireDataIdentifier(TCPMessage* message) {
	bool outcome = false;
	//if (message->nBytes == sizeof(int)) {
		int* tmp = new int;
		tmp = (int *)message->data;
		dataIdentifier = *tmp;
		outcome = true;
		delete tmp;
	//}
	return outcome;
}

} /* namespace KernelHive */
