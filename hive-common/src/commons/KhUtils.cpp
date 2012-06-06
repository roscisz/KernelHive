#include <istream>
#include <streambuf>
#include <string>
#include <sstream>

#include "KhUtils.h"
#include "KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Static Members									 //
// ========================================================================= //

	std::string KhUtils::readStream(std::istream& inputStream) {
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

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	KhUtils::KhUtils() { }

	KhUtils::~KhUtils() { }

}
