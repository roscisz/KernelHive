/*
 * IReportable.h
 *
 *  Created on: 18-04-2012
 *      Author: roy
 */

#ifndef IREPORTABLE_H_
#define IREPORTABLE_H_

namespace KernelHive {

class IReportable {
public:
	IReportable() { };
	virtual ~IReportable() { };
	virtual int getPercentDone() = 0;
};

}

#endif /* IREPORTABLE_H_ */
