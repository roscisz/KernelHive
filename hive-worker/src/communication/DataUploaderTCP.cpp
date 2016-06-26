/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
#include <cstdio>
#include <cstring>
#include <sstream>

#include "DataUploaderTCP.h"
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

DataUploaderTCP::DataUploaderTCP(NetworkAddress* address,
		SynchronizedBuffer** buffers, int partsCount) :
		IDataUploader(address, buffers, partsCount) {
	this->tcpClient = new TCPClient(address, this);
	this->address = address;
	this->buffers = buffers;
	this->partsCount = partsCount;
	this->currentState = STATE_INITIAL;
	this->currentPart = 0;
	dataIdentifiers = new int[partsCount];

	prepareCommands();
//	Logger::log(DEBUG, ">>> UPLOADER %s:%d WILL LOG the amount of DATA HE GOT: %d\n", address->host, address->port, buffer->getSize());
//	buffer->logMyFloatData();
//	Logger::log(DEBUG, ">>> UPLOADER %s:%d FINISHED LOGGING DATA HE GOT\n", address->host, address->port);
}

DataUploaderTCP::~DataUploaderTCP() {
}

void DataUploaderTCP::onMessage(TCPMessage* message) {
	switch (currentState) {
	case STATE_INITIAL:
		if (acquireDataIdentifier(message) == true) {
			buffers[currentPart]->seek(0);
			currentState = STATE_IDENTIFIER_ACQUIRED;
			uploadData();
		}
		break;

	case STATE_IDENTIFIER_ACQUIRED:
		uploadData();
		break;
	}
}

void DataUploaderTCP::onConnected() {
	Logger::log(INFO, "Uploader connection established\n");
	switch (currentState) {
	case STATE_INITIAL:
		dataPublishData[1] = buffers[currentPart]->getSize();
		this->tcpClient->sendMessage(dataPublish);
		break;

	case STATE_IDENTIFIER_ACQUIRED:
		uploadData();
		break;
	}
}

/* SKLEIĆ WSZYSTKIE URLE tak jak jest sklejane w OpenCLWorker - GetAllUploadStrings */
void DataUploaderTCP::getDataURL(std::string *param) {
	std::stringstream ret;

	for (int i = 0; i != this->partsCount; i++) {
		ret << address->host;
		ret << " ";
		ret << KhUtils::itoa(address->port);
		ret << " ";
		ret << dataIdentifiers[i];
		ret << " ";
	}

	param->append(ret.str());
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void DataUploaderTCP::uploadData() {
	byte* uploadBuffer = NULL;
	SynchronizedBuffer* buffer = buffers[currentPart];

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
		byte *tmp = (byte *) uploadCmd;
		for (int i = 0; i < cmdSize; i++) {
			uploadBuffer[i] = tmp[i];
		}

		buffer->read(uploadBuffer + cmdSize, uploadPackageSize);

		TCPMessage message(uploadBuffer, msgSize);
		this->tcpClient->sendMessage(&message);
		Logger::log(DEBUG, ">>>>>> SENT %u BYTES\n", msgSize);

		// TODO Find out why this is necessary
		//sleep(1);

		delete[] uploadBuffer;
	}

	if (++currentPart == partsCount) {
		pleaseStop();
	} else {
		currentState = STATE_INITIAL;
		dataPublishData[1] = buffers[currentPart]->getSize();
		this->tcpClient->sendMessage(dataPublish);
	}
}

void DataUploaderTCP::prepareCommands() {
	dataPublishData = new int[2];
	dataPublishData[0] = PUBLISH_DATA;
	// TODO: add before each sending: dataPublishData[1] = buffer->getSize();
	dataPublish = new TCPMessage((byte *) dataPublishData, 2 * sizeof(int));
}

void DataUploaderTCP::prepareDataAppend(int *command, size_t packageSize) {
	command[0] = APPEND_DATA;
	command[1] = dataIdentifiers[currentPart];
	command[2] = packageSize;
}

void DataUploaderTCP::copyCommand(byte *buffer, int *command) {
	byte *tmp = (byte *) command;
	for (int i = 0; i < TCP_COMMAND_SIZE; i++) {
		buffer[i] = tmp[i];
	}
}

bool DataUploaderTCP::acquireDataIdentifier(TCPMessage* message) {
	bool outcome = false;
	//if (message->nBytes == sizeof(int)) {
	int* tmp = new int;
	tmp = (int *) message->data;
	dataIdentifiers[currentPart] = *tmp;
	outcome = true;
	delete tmp;
	//}
	return outcome;
}

void DataUploaderTCP::run() {
	tcpClient->run();
}

void DataUploaderTCP::pleaseStop() {
	tcpClient->pleaseStop();
}

} /* namespace KernelHive */
