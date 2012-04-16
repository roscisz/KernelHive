/*
 * IClusterListener.h
 *
 *  Created on: 09-04-2012
 *      Author: roy
 */

#ifndef ICLUSTERLISTENER_H_
#define ICLUSTERLISTENER_H_

namespace KernelHive {

class IClusterListener {
public:
	IClusterListener();
	virtual ~IClusterListener();
	virtual void onMessage(char *message) {};
};

#endif /* ICLUSTERLISTENER_H_ */

}
