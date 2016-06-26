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
#ifndef KERNEL_HIVE_KH_UTILS_H
#define KERNEL_HIVE_KH_UTILS_H

#include <istream>
#include <string>

namespace KernelHive {

		/**
		 * Provides a set of utilities for working with OpenCL API.
		 */
		class KhUtils {

		public:
			/**
			 * The default constructor.
			 */
			KhUtils();

			/**
			 * The destructor.
			 */
			virtual ~KhUtils();

			/**
			 * Reads a provided stream and returns it as a string.
			 *
			 * @param inputStream the stream to read from
			 * @return a string containing what has been read
			 */
			static std::string readStream(std::istream& inputStream);
			
			/**
			 * Converts the provided CString into an int. Throws an
			 * exception if the conversion cannot be performed.
			 *
			 * @param string the CString to convert
			 * @return the converted number, if successful
			 */
			static int atoi(const char* string);

			static float atof(const std::string& s);

			static std::string itoa(int number);

		};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_KH_UTILS_H */

