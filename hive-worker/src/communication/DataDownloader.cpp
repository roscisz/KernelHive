/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
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
#include <iostream>
#include <unistd.h>
#include <cstdlib>
#include <sstream>
#include <string>

#include "DataDownloader.h"
#include "commons/Logger.h"
#include "commons/KhUtils.h"
#include "commons/KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataDownloader::DataDownloader(NetworkAddress* address, const char* dataId, SynchronizedBuffer* buffer)
	: TCPClient(address, this)
{
	this->dataId = KhUtils::atoi(dataId);
	this->buffer = buffer;
	progressSize = 0;
	currentState = STATE_INITIAL;
	sizeQuery = NULL;
	dataQuery = NULL;
	sizeQueryData = NULL;
	dataQueryData = NULL;
	prepareQueries();
}

void DataDownloader::onMessage(TCPMessage* message) {
	// TODO Remove development logging
	switch (currentState) {
	case STATE_INITIAL:
		if (acquireDataSize(message)) {
			Logger::log(INFO, "Data size of package %d has been acquired: %u\n", dataId, totalDataSize);
			buffer->allocate(totalDataSize);
			currentState = STATE_SIZE_ACQUIRED;
			sendMessage(dataQuery);
		} else {
			sendMessage(sizeQuery);
		}
		break;

	case STATE_SIZE_ACQUIRED:
		buffer->append(message->data, message->nBytes);
		progressSize += message->nBytes;
		Logger::log(INFO, "%d/%d bytes available of package %d\n", progressSize, totalDataSize, dataId);
		if (!(progressSize < totalDataSize)) {
			currentState = STATE_DATA_ACQUIRED;
			Logger::log(DEBUG, ">>> DOWNLOADER (dataId: %d) WILL LOG the amount of DATA HE GOT: %d\n", dataId, buffer->getSize());
			//buffer->logMyFloatData();
			Logger::log(DEBUG, ">>> DOWNLOADER (dataId: %d) FINISHED LOGGING DATA HE GOT\n", dataId);
			pleaseStop();
		}
		break;

	case STATE_DATA_ACQUIRED:
		Logger::log(INFO, "All data has been acquired..");
		pleaseStop();
		break;
	}
}

void DataDownloader::onConnected() {
	Logger::log(INFO, "Connection established\n");
	sendMessage(sizeQuery);
}

DataDownloader::~DataDownloader() {
	if (sizeQuery != NULL) {
		delete sizeQuery;
	}
	if (dataQuery != NULL) {
		delete dataQuery;
	}
	if (sizeQueryData != NULL) {
		delete [] sizeQueryData;
	}
	if (dataQueryData != NULL) {
		delete [] dataQueryData;
	}
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

bool DataDownloader::acquireDataSize(TCPMessage *sizeMessage) {
	bool outcome = false;
	Logger::log(INFO, "DATA_SIZE: %d/\n", sizeMessage->nBytes );
	//if (sizeMessage->nBytes == sizeof(int)) {
		int* tmp = new int;
		tmp = (int *)sizeMessage->data;
		totalDataSize = *tmp;
		outcome = true;
		delete tmp;
	//}
	return outcome;
}

void DataDownloader::prepareQueries() {
	sizeQueryData = new int[2];
	sizeQueryData[0] = GET_SIZE;
	sizeQueryData[1] = dataId;
	sizeQuery = new TCPMessage((byte *)sizeQueryData, TCP_COMMAND_SIZE);
	dataQueryData = new int[2];
	dataQueryData[0] = GET_DATA;
	dataQueryData[1] = dataId;
	dataQuery = new TCPMessage((byte *)dataQueryData, TCP_COMMAND_SIZE);
}

} /* namespace KernelHive */
