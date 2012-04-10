#ifndef KERNEL_HIVE_OPEN_CL_HELPER_H
#define KERNEL_HIVE_OPEN_CL_HELPER_H

#include <istream>
#include <string>

namespace KernelHive {

		/**
		 * Provides a set of utilities for working with OpenCL API.
		 */
		class OpenClHelper {

		public:
			/**
			 * The default constructor.
			 */
			OpenClHelper();

			/**
			 * The destructor.
			 */
			virtual ~OpenClHelper();

			/**
			 * Reads source code from the provided stream. 
			 *
			 * @param inputStream the stream to read the source from
			 * @return a string containing the source code read
			 * 		from the stream
			 */
			std::string readSource(std::istream& inputStream);
			
		};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_OPEN_CL_HELPER_H */

