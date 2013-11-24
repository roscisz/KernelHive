/*
 * Helpers.cpp
 *
 *  Created on: 13-07-2013
 *      Author: szymon
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
