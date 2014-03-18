/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
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
#include "SynchronizedBuffer.h"

#include "../commons/Logger.h"
#include <cstdio>

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

SynchronizedBuffer::SynchronizedBuffer() {
	init();
}

SynchronizedBuffer::SynchronizedBuffer(size_t size) {
	init();
	allocateInternal(size);
}

SynchronizedBuffer::~SynchronizedBuffer() {
	releaseResources();
}

void SynchronizedBuffer::allocate(size_t size) {
	pthread_mutex_lock(&bufferLock);
	allocateInternal(size);
	pthread_mutex_unlock(&bufferLock);
}

void SynchronizedBuffer::append(byte* data, size_t amount) {
	pthread_mutex_lock(&bufferLock);
	for (unsigned int i = 0; i < amount && position < size; i++) {
		this->data[position] = data[i];
		position++;
	}
	pthread_mutex_unlock(&bufferLock);
}

void SynchronizedBuffer::read(byte* target, size_t amount) {
	pthread_mutex_lock(&bufferLock);
	for (unsigned int i = 0; i < amount && position < size; i++) {
		target[i] = data[position];
		position++;
	}
	pthread_mutex_unlock(&bufferLock);
}

void SynchronizedBuffer::seek(size_t offset) {
	pthread_mutex_lock(&bufferLock);
	position = offset;
	pthread_mutex_unlock(&bufferLock);
}

bool SynchronizedBuffer::isAtEnd() {
	return !(position < size);
}

size_t SynchronizedBuffer::getSize() {
	return size;
}

size_t SynchronizedBuffer::getOffset() {
	return position;
}

byte* SynchronizedBuffer::getRawData() {
	return data;
}

void SynchronizedBuffer::logMyFloatData() {
	Logger::log(DEBUG, "(buffer) >>> WILL NOW LOG BUFFER DATA AS FLOATS\n");
	float *tmp;
	for (int i = 0; i < size && i < 400; i += sizeof(float)) {
		tmp = (float *)(data+i);
		printf("%lu: [ %.2X %.2X %.2X %.2X ] %f\n", (long unsigned int)(i/4), data[i], data[i+1], data[i+2], data[i+3], *tmp);
	}
	Logger::log(DEBUG, "(buffer) >>> FINISHED LOGGING BUFFER DATA AS FLOATS\n");
}

void SynchronizedBuffer::logRawData() {
	Logger::log(DEBUG, "(buffer) >>> WILL NOW LOG BUFFER DATA AS HEX\n");
	float *tmp;
	for (int i = 0; i < size && i < 400; i++) {
		printf("%.2X ", data[i]);
	}
	printf("\n");
	Logger::log(DEBUG, "(buffer) >>> FINISHED LOGGING BUFFER DATA AS HEX\n");
}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

void SynchronizedBuffer::init() {
	size = 0;
	position = 0;
	data = NULL;
	pthread_mutex_init(&bufferLock, NULL);
}

void SynchronizedBuffer::allocateInternal(size_t size) {
	releaseResources();
	data = new byte[size];
	this->size = size;
}

void SynchronizedBuffer::releaseResources() {
	if (data != NULL) {
		delete [] data;
	}
	size = 0;
	position = 0;
}

} /* namespace KernelHive */
