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
