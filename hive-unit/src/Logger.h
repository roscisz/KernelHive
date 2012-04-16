/*
 * Logger.h
 *
 *  Created on: 06-04-2012
 *      Author: roy
 */

#ifndef LOGGER_H_
#define LOGGER_H_

namespace KernelHive {

enum Priority {
    DEBUG,
    INFO,
    ERROR,
    FATAL,
};

#define PRIORITY DEBUG

class Logger {
public:
	static void log(Priority priority, const char *fmt, ...);
	static char *priorityToString(Priority priority);
};

#endif /* LOGGER_H_ */

}
