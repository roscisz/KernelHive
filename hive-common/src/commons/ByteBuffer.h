/*
 * ByteBuffer.h
 *
 *  Created on: 08-05-2013
 *      Author: szymon
 */

#ifndef BYTEBUFFER_H_
#define BYTEBUFFER_H_

#define BYTEBUFFER_SIZE_STEP 1024

#include <cstring>

#include "byte.h"

namespace KernelHive {

class ByteBuffer {
public:
	ByteBuffer();
	virtual ~ByteBuffer();

	void append(byte* value, size_t size);
	void append(byte value);
	void append(short int value);
	void append(int value);

	byte* getBuffer();
	size_t getSize();

	void printBinary();
	void printHex();

protected:
	byte* buffer;
	size_t size;
	size_t maxSize;

	void extend();
};

}
#endif /* BYTEBUFFER_H_ */
