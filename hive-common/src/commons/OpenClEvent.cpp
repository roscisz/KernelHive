/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Rafal Lewandowski
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
