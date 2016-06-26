/**
 * Copyright (c) 2014 Gdansk University of Technology
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
#include <cstdio>
#include <stdarg.h>
#include "Logger.h"

namespace KernelHive {

void Logger::log(Priority priority, const char* fmt, ...) {
	va_list fmt_args;

	if(priority < PRIORITY) return;

	fprintf(stdout, priorityToString(priority));
	va_start(fmt_args, fmt);
	vfprintf(stdout, fmt, fmt_args);
	va_end (fmt_args);
}

const char *Logger::priorityToString(Priority priority) {
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

}
