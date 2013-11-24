/*
 * ByteBuffer.cpp
 *
 *  Created on: 08-05-2013
 *      Author: szymon
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
