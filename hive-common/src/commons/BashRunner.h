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
#ifndef BASHRUNNER_H_
#define BASHRUNNER_H_

#include <stdio.h>

namespace KernelHive {

class BashRunner {
public:
	BashRunner(const char* command);
	virtual ~BashRunner();

	const char* execute();
	const char* execute(char* param);
	const char* execute(int param);
	const char* execute(unsigned int param);

private:
	const char* command;
	const char* innerExecute(const char* param);
};

} /* namespace KernelHive */
#endif /* BASHRUNNER_H_ */
