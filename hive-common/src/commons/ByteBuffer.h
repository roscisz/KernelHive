/**
 * Copyright (c) 2014 Gdansk University of Technology
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
