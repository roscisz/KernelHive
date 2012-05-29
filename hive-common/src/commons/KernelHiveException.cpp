#include "KernelHiveException.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

KernelHiveException::KernelHiveException(std::string& message) {
	this->message = message;
}

KernelHiveException::KernelHiveException(const char* message) {
	this->message = message;
}

KernelHiveException::~KernelHiveException() {
	// TODO Auto-generated destructor stub
}

std::string KernelHiveException::getMessage() {
	return message;
}

} /* namespace KernelHive */
