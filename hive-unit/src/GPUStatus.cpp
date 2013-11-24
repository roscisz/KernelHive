/*
 * GPUStatus.cpp
 *
 *  Created on: 17-04-2013
 *      Author: szymon
 */

#include "GPUStatus.h"

namespace KernelHive {

GPUStatus::GPUStatus() {
	id = -1;
	gpuUsage = -1;
	totalMemory = -1;
	usedMemory = -1;
	fanSpeed = -1;
	available = true;
}

GPUStatus::~GPUStatus() {
	// TODO Auto-generated destructor stub
}

short int GPUStatus::getId() {
	return id;
}

short int GPUStatus::getGpuUsage() {
	return gpuUsage;
}

int GPUStatus::getTotalMemory() {
	return totalMemory;
}

int GPUStatus::getUsedMemory() {
	return usedMemory;
}

string GPUStatus::getName() {
	return name;
}

string GPUStatus::getVendorName() {
	return vendorName;
}

string GPUStatus::getIdentifier() {
	return identifier;
}

short int GPUStatus::getFanSpeed() {
	return fanSpeed;
}


void GPUStatus::setId(short int id) {
	this->id = id;
}

void GPUStatus::setGpuUsage(short int gpuUsage) {
	this->gpuUsage =  gpuUsage;
}

void GPUStatus::setTotalMemory(int totalMemory) {
	this->totalMemory = totalMemory;
}

void GPUStatus::setUsedMemory(int usedMemory) {
	this->usedMemory = usedMemory;
}

void GPUStatus::setName(string name) {
	this->name = name;
}

void GPUStatus::setVendorName(string vendorName) {
	this->vendorName = vendorName;
}

void GPUStatus::setIdentifier(string identifier) {
	this->identifier = identifier;
}

void GPUStatus::setFanSpeed(short int fanSpeed) {
	this->fanSpeed = fanSpeed;
}

bool GPUStatus::isAvailable() {
	return available;
}

void GPUStatus::setAvailable(bool isAvailable) {
	this->available = isAvailable;
}

} /* namespace KernelHive */
