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
	for (int i = 0; i < size; i += sizeof(float)) {
		if (i == 40) {
			break;
		}
		tmp = (float *)(data+i);
		printf("%d: [ %d, %d, %d, %d ] %f\n", i/sizeof(float), data[i], data[i+1], data[i+2], data[i+3], *tmp);
	}
	Logger::log(DEBUG, "(buffer) >>> FINISHED LOGGING BUFFER DATA AS FLOATS\n");
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
