#include <istream>
#include <streambuf>
#include <string>

#include "OpenClHelper.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	OpenClHelper::OpenClHelper() {
	}

	OpenClHelper::~OpenClHelper() {
	}

	std::string OpenClHelper::readSource(std::istream& inputStream) {
		std::string source;

		source.assign((std::istreambuf_iterator<char>(inputStream)),
				std::istreambuf_iterator<char>());

		return source;
	}

}

