/*
 * Helpers.h
 *
 *  Created on: 13-07-2013
 *      Author: szymon
 */

#ifndef HELPERS_H_
#define HELPERS_H_

namespace KernelHive {

class Helpers {
public:
	static bool parseString(const char* text, int* parsed);
	static bool parseString(const char* text, unsigned int* parsed);
	static bool parseString(const char* text, short int* parsed);
};

} /* namespace KernelHive */
#endif /* HELPERS_H_ */
