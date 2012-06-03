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
// 							Constants Init									 //
// ========================================================================= //

const char* DataDownloader::GET_SIZE = "1";

const char* DataDownloader::GET_DATA = "2";

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataDownloader::DataDownloader(NetworkAddress* address, char* dataId, SynchronizedBuffer* buffer)
	: TCPClient(address, this)
{
	this->dataId = dataId;
	this->buffer = buffer;
	currentState = STATE_INITIAL;
	prepareQueries();
}

void DataDownloader::onMessage(TCPMessage* message) {
	// TODO Remove development logging
	switch (currentState) {
	case STATE_INITIAL:
		if (acquireDataSize(message->data)) {
			Logger::log(INFO, "Data size has been acquired: %u\n", totalDataSize);
			buffer->allocate(totalDataSize);
			currentState = STATE_SIZE_ACQUIRED;
			sendMessage(dataQuery.c_str());
		} else {
			sendMessage(sizeQuery.c_str());
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
		pleaseStop();
		break;
	}
}

void DataDownloader::onConnected() {
	Logger::log(INFO, "Connection established\n");
	sendMessage(sizeQuery.c_str());
}

DataDownloader::~DataDownloader() {
	// TODO Auto-generated destructor stub
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

bool DataDownloader::acquireDataSize(const char* sizeString) {
	bool outcome = false;
	try {
		totalDataSize = KhUtils::atoi(sizeString);
		outcome =  true;
	} catch (KernelHiveException& e) {
		Logger::log(ERROR, e.getMessage().data());
	}
	return outcome;
}

void DataDownloader::prepareQueries() {
	std::stringstream ss;
	ss << GET_SIZE << ' ' << dataId;
	sizeQuery = ss.str();
	ss.str("");
	ss.clear();
	ss << GET_DATA << ' ' << dataId;
	dataQuery = ss.str();
}

} /* namespace KernelHive */
