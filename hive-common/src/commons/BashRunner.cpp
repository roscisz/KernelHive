/*
 * BashRunner.cpp
 *
 *  Created on: 16-04-2013
 *      Author: szymon
 */

#include "BashRunner.h"
#include <string>
#include <iostream>

namespace KernelHive {

BashRunner::BashRunner(const char* command) {
	this->command = command;

}

BashRunner::~BashRunner() {
	// TODO Auto-generated destructor stub
}

const char* BashRunner::execute() {
	FILE* pipe = popen(command, "r");
	std::string content;
	char line[1024];
	while(!feof(pipe)) {
		if(fgets(line, 1024, pipe) != NULL) {
			content.append(line);
		}
	}
	pclose(pipe);
	return content.c_str();
}

} /* namespace KernelHive */
