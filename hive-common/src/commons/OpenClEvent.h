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
#ifndef OPEN_CL_EVENT_H
#define OPEN_CL_EVENT_H

#include <CL/cl.h>

namespace KernelHive {

	class OpenClEvent {

	public:
		/**
		 * Constructs a new OpenClEvent object.
		 *
		 * @param openClEvent an OpenCL event
		 */
		OpenClEvent(cl_event openClEvent);

		/**
		 * The copy constructor.
		 *
		 * @param event the OpenClEvent object to copy from
		 */
		OpenClEvent(const OpenClEvent& event);

		/**
		 * The destructor.
		 */
		virtual ~OpenClEvent();

		/**
		 * Returns an actual OpenCL event represented by this object.
		 *
		 * @return an OpenCL event
		 */
		cl_event getOpenClEvent();

	private:
		cl_event openClEvent;

	};

} /* namespace KernelHive */

#endif /* OPEN_CL_EVENT_H */
