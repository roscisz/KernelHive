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
#include "Helpers.h"

#include <cstdlib>
#include <sstream>
#include <string>

namespace KernelHive {

bool Helpers::parseString(const char* text, int* parsed) {
	if(text == NULL) {
		return false;
	}
	std::stringstream ss(text);
	std::string line;
	if(std::getline(ss, line)) {
		*parsed = strtol(line.c_str(), NULL, 0);
		return true;
	} else {
		return false;
	}
}

bool Helpers::parseString(const char* text, unsigned int* parsed) {
	if(text == NULL) {
		return false;
	}
	std::stringstream ss(text);
	std::string line;
	if(std::getline(ss, line)) {
		*parsed = strtoul(line.c_str(), NULL, 0);
		return true;
	} else {
		return false;
	}
}

bool Helpers::parseString(const char* text, short int* parsed) {
	return parseString(text, (int*)parsed);
}

} /* namespace KernelHive */
