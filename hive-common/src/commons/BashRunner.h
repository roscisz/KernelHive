/*
 * BashRunner.h
 *
 *  Created on: 16-04-2013
 *      Author: szymon
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
