#ifndef KERNEL_HIVE_SYNCHRONIZED_BUFFER_H
#define KERNEL_HIVE_SYNCHRONIZED_BUFFER_H

#include <cstdlib>
#include <pthread.h>

namespace KernelHive {

/** A data type alias. */
typedef char byte;

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
	 * Returns the amount of bytes allocated in this buffer.
	 *
	 * @return the amount of bytes allocated in this buffer
	 */
	size_t getSize();

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
