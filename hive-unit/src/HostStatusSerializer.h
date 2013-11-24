/*
 * HostStatusSerializer.h
 *
 *  Created on: 16-04-2013
 *      Author: szymon
 */

#ifndef HostStatusSerializer_H_
#define HostStatusSerializer_H_

#include "HostStatus.h"
#include "commons/ByteBuffer.h"

namespace KernelHive {

class HostStatusSerializer {
public:
	HostStatusSerializer();
	ByteBuffer* serializeToSequentialMessage(HostStatus* hostStatus);
	ByteBuffer* serializeToInitialMessage(HostStatus* hostStatus);
	string serializeToStringInitialMessage(HostStatus* hostStatus);
};

} /* namespace KernelHive */
#endif /* HostStatusSerializer_H_ */
