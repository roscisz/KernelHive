/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
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
#include "BashRunner.h"
#include <iostream>

namespace KernelHive {

BashRunner::BashRunner(const char* command) {
	this->command = command;

}

BashRunner::~BashRunner() {
	// TODO Auto-generated destructor stub
}

std::string BashRunner::execute() {
	FILE* pipe = popen(command, "r");
	std::string content;
	char line[1024];
	while(!feof(pipe)) {
		if(fgets(line, 1024, pipe) != NULL) {
			content.append(line);
		}
	}
	pclose(pipe);
	return content;
}

} /* namespace KernelHive */
