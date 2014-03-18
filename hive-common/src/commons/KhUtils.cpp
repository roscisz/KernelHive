/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
 * Copyright (c) 2014 Pawel Rosciszewski
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
#include <istream>
#include <streambuf>
#include <string>
#include <sstream>
#include <iostream>

#include <cstdio>
#include "KhUtils.h"
#include "KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Static Members									 //
// ========================================================================= //

	std::string KhUtils::readStream(std::istream& inputStream) {
		if (inputStream.fail()) {
			throw KernelHiveException("Invalid input file");
		}

		std::string source;

		source.assign((std::istreambuf_iterator<char>(inputStream)),
				std::istreambuf_iterator<char>());

		return source;
	}

	int KhUtils::atoi(const char* string) {
		std::string str = string;
		std::stringstream sstream(str);
		int value;

		if ((sstream >> value).fail() || !(sstream >> std::ws).eof()) {
			std::stringstream ss;
			ss << "The value '";
			ss << string;
			ss << "' cannot be converted to int!";
			std::string message = ss.str();
			throw KernelHiveException(message);
		}

		return value;
	}

	const char *KhUtils::itoa(int number) {
		std::string ret;
		sprintf((char *)ret.c_str(), "%d", number);
		return ret.c_str();
	}

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	KhUtils::KhUtils() { }

	KhUtils::~KhUtils() { }

}

