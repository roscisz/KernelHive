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

			static const char *itoa(int number);

		};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_KH_UTILS_H */

