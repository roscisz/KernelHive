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
