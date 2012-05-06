#include "OpenClEvent.h"

namespace KernelHive {

// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

	OpenClEvent::OpenClEvent(cl_event event) {
		this->openClEvent = event;
	}

	OpenClEvent::OpenClEvent(const OpenClEvent& event) {
		this->openClEvent = event.openClEvent;
	}

	OpenClEvent::~OpenClEvent() {
		// TODO Auto-generated destructor stub
	}

	cl_event OpenClEvent::getOpenClEvent() {
		return openClEvent;
	}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

} /* namespace KernelHive */
