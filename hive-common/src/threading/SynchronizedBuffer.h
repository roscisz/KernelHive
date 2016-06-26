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
#ifndef KERNEL_HIVE_SYNCHRONIZED_BUFFER_H
#define KERNEL_HIVE_SYNCHRONIZED_BUFFER_H

#include <cstdlib>
#include <pthread.h>
#include "../commons/byte.h"

namespace KernelHive {

/**
 * A thread-safe buffer.
 */
class SynchronizedBuffer {

public:
	/**
	 * Initializes this synchronized buffer.
	 */
	SynchronizedBuffer();

	/**
	 * Initializes this buffer with provided size.
	 *
	 * @param size the number of bytes this buffer should allocate
	 */
	SynchronizedBuffer(size_t size);

	/**
	 * Deallocates resources used by this buffer.
	 */
	virtual ~SynchronizedBuffer();

	/**
	 * Allocates this buffer with provided size. Any data previously
	 * allocated and written will be released.
	 *
	 * @param size the size of the new buffer
	 */
	void allocate(size_t size);

	/**
	 * Deallocates resources used by this buffer.
	 */
	void deallocate();

	/**
	 * Returns the amount of bytes allocated in this buffer.
	 *
	 * @return the amount of bytes allocated in this buffer
	 */
	size_t getSize();

	/**
	 * Returns the current offset in this buffer.
	 *
	 * @return the current offset in this buffer
	 */
	size_t getOffset();

	/**
	 * Set the position in the buffer.
	 *
	 * @param offset the position to set
	 */
	void seek(size_t offset);

	/**
	 * Reads an amount of data from this buffer into the provided target
	 * buffer. The data is read from the buffers current offset. The offset
	 * will be incremented.
	 *
	 * @param target the placeholder to which the data should be copied
	 * @param amount the number of bytes to copy
	 */
	void read(byte* target, size_t amount);

	/**
	 * Tells whether the buffer's pointer reached the end or not.
	 *
	 * @return true if the pointer reached the end, false otherwise
	 */
	bool isAtEnd();

	/**
	 * Appends provided data to the end of this buffer.
	 *
	 * @param data the data to append
	 * @param amount the number of bytes to append
	 */
	void append(byte* data, size_t amount);

	/**
	 * Returns a pointer to data allocated in this buffer. This is the pointer
	 * to the internal buffer, so it's contents may be subject to overwriting
	 * by any other threads using it!
	 *
	 * @return the pointer to buffer's data
	 */
	byte* getRawData();

	/**
	 * Logs float data to STDOUT.
	 */
	void logMyFloatData();

	/**
	 * Logs raw data as hex to STDOUT.
	 */
	void logRawData();

private:
	/** The number of bytes allocated by this buffer. */
	size_t size;

	/** The position pointer of this buffer. */
	unsigned int position;

	/** The data stored in this buffer. */
	byte* data;

	/** The lock object used to synchronize this buffer. */
	pthread_mutex_t bufferLock;

	/**
	 * Initializes this buffer.
	 */
	void init();

	/**
	 * Performs the actual allocation.
	 *
	 * @param size the number of bytes to allocate
	 */
	void allocateInternal(size_t size);

	/**
	 * Releases any resources stored in this buffer
	 */
	void releaseResources();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_SYNCHRONIZED_BUFFER_H */
