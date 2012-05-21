#include "SynchronizedBuffer.h"

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
	for (unsigned int i = 0; i < amount; i++) {
		this->data[position] = data[i];
		position++;
	}
	pthread_mutex_unlock(&bufferLock);
}

size_t SynchronizedBuffer::getSize() {
	return size;
}

byte* SynchronizedBuffer::getRawData() {
	return data;
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
