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
#include "ByteBuffer.h"
#include <cstdio>

namespace KernelHive {

ByteBuffer::ByteBuffer() {
	size = 0;
	maxSize = BYTEBUFFER_SIZE_STEP;
	buffer = new byte[maxSize];
	buffer[0] = '\0';
}

ByteBuffer::~ByteBuffer() {
	delete[] buffer;
}

byte* ByteBuffer::getBuffer() {
	return buffer;
}

size_t ByteBuffer::getSize() {
	return size;
}

void ByteBuffer::extend() {
	byte* backup = buffer;
	maxSize += BYTEBUFFER_SIZE_STEP;
	buffer = new byte[maxSize];

	memcpy(buffer, backup, size+1);
	delete[] backup;
}

void ByteBuffer::append(byte* value, size_t size) {
	while(this->size + size >= maxSize) {
		extend();
	}
	memcpy(buffer + this->size, value, size);
	this->size += size;
	buffer[this->size] = '\0';
}

void ByteBuffer::append(byte value) {
	size_t size = sizeof(value);
	byte* b = new byte[size];

	memcpy(b, &value, size);
	append(b, size);

	delete[] b;
}

void ByteBuffer::append(int value) {
	size_t size = sizeof(value);
	byte* b = new byte[size];

	memcpy(b, &value, size);
	append(b, size);

	delete[] b;
}

void ByteBuffer::append(short int value) {
	size_t size = sizeof(value);
	byte* b = new byte[size];

	memcpy(b, &value, size);
	append(b, size);

	delete[] b;
}

void ByteBuffer::printBinary() {
	for(int i = 0; i < size; i++) {
		byte b = buffer[i];
		for(int j = 7; j >= 0; j--) {
			byte mask = 1;
			for(int k = 0; k < j; k++) {
				mask <<= 1;
			}
			printf("%x", (b & mask) >> j);
		}

	}
	printf("\n");
}

void ByteBuffer::printHex() {
	for(int i = 0; i < size; i++) {
		byte b = buffer[i];
		printf("%02x", buffer[i]);
	}
	printf("\n");
}

}
