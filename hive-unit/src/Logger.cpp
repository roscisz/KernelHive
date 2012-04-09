/*
 * Logger.cpp
 *
 *  Created on: 06-04-2012
 *      Author: roy
 */

#include <cstdio>
#include <stdarg.h>
#include "Logger.h"

void Logger::log(Priority priority, const char* fmt, ...) {
	va_list fmt_args;

	if(priority < PRIORITY) return;

	fprintf(stdout, priorityToString(priority));
	va_start(fmt_args, fmt);
	vfprintf(stdout, fmt, fmt_args);
	va_end (fmt_args);
}

char *Logger::priorityToString(Priority priority) {
	switch(priority) {
	case DEBUG:
		return "DEBUG: ";
	case INFO:
		return "INFO: ";
	case ERROR:
		return "ERROR: ";
	case FATAL:
		return "FATAL: ";
	default:
		return "UNDEFINED ERROR: ";
	}
}
