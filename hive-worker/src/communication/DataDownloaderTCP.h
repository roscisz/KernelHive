/**
 * Copyright (c) 2014 Gdansk University of Technology
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
#ifndef KERNEL_HIVE_DATA_DOWNLOADER_H
#define KERNEL_HIVE_DATA_DOWNLOADER_H

#include <pthread.h>

#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "network/TCPMessage.h"
#include "threading/Thread.h"
#include "IDataDownloader.h"

namespace KernelHive {

/**
 * A thread responsible for downloading data from from a given
 * network location.
 */
class DataDownloaderTCP	: public IDataDownloader, public TCPClientListener {

public:
	/**
	 * Instantiates this downloader thread.
	 */
	DataDownloaderTCP(NetworkAddress* address, const char* dataId, SynchronizedBuffer* buffer);

	/**
	 * Deallocates resources used by this downloader thread.
	 */
	virtual ~DataDownloaderTCP();

	/**
	 * Called upon receiving data via the socket.
	 *
	 * @param message the received message
	 */
	void onMessage(TCPMessage* message);

	/**
	 * Called when a connection is established.
	 */
	void onConnected();

	void run();

	void pleaseStop();

private:
	TCPClient *tcpClient;

	/** The initial state. */
	static const int STATE_INITIAL = 0;

	/** Data size has been acquired. */
	static const int STATE_SIZE_ACQUIRED = 1;

	/** Data has been acquired. */
	static const int STATE_DATA_ACQUIRED = 2;

	/** The command which allows to query for data size. */
	static const int GET_SIZE = 1;

	/** The command which to query for data. */
	static const int GET_DATA = 2;

	/** Defines the current state of this data downloader. */
	int currentState;

	/** Stores the total amount of data to be received. */
	size_t totalDataSize;

	/** Defines the amount of data currently downloaded. */
	size_t progressSize;

	/** The identifier which can be used to query the data repository. */
	int dataId;

	/** The query which can be sent to data repository in order to acquire data size. */
	TCPMessage* sizeQuery;

	int *sizeQueryData;

	/** The query which can be sent to data repository in order to acquire data. */
	TCPMessage* dataQuery;

	int *dataQueryData;

	/**
	 * Parses and assigns the provided data size string.
	 *
	 * @return true if size has been parsed successfully, false otherwise
	 */
	bool acquireDataSize(TCPMessage *sizeMessage);

	/**
	 * Pre-compiles the queries which will be sent to the data repository.
	 */
	void prepareQueries();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_DATA_DOWNLOADER_H */
