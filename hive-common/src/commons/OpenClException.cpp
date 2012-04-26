#include "OpenClException.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	OpenClException::OpenClException(std::string message, cl_int openClErrorCode) {
		this->message = message;
		this->openClErrorCode = openClErrorCode;
	}

	OpenClException::OpenClException(OpenClException& exception) {
		this->message = exception.getMessage();
		this->openClErrorCode = exception.getOpenClErrorCode();
	}

	OpenClException::~OpenClException() {
		// TODO Auto-generated destructor stub
	}

	std::string OpenClException::getMessage() {
		return message;
	}

	cl_int OpenClException::getOpenClErrorCode() {
		return openClErrorCode;
	}

} /* namespace KernelHive */
