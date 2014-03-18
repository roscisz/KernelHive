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
#ifndef KERNEL_HIVE_OPEN_CL_EXCEPTION
#define KERNEL_HIVE_OPEN_CL_EXCEPTION

#include <string>
#include <CL/cl.h>

namespace KernelHive {

	/**
	 * An exception used for indicating OpenCL errors.
	 */
	class OpenClException {
	public:
		/**
		 * The constructor - initialized with a string class.
		 *
		 * @param message the error message of this exception
		 * @param openClErrorCode the OpenCL error code
		 */
		OpenClException(std::string& message, cl_int openClErrorCode);

		/**
		 * The constructor - initialized with a C string.
		 *
		 * @param message the error message of this exception
		 * @param openClErrorCode the OpenCL error code
		 */
		OpenClException(const char* message, cl_int openClErrorCode);

		/**
		 * The destructor.
		 */
		virtual ~OpenClException();

		/**
		 * Gets the error message.
		 *
		 * @return the message
		 */
		std::string getMessage();

		/**
		 * Gets the OpenCL error code
		 *
		 * @return the OpenCL error code
		 */
		cl_int getOpenClErrorCode();

	private:
		/** The error message. */
		std::string message;

		/** The OpenCL error code. */
		cl_int openClErrorCode;
	};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_OPEN_CL_EXCEPTION */
