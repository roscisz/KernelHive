/*
 * ClusterProxy.h
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#ifndef CLUSTERPROXY_H_
#define CLUSTERPROXY_H_

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include "commons/TCPClient.h"

namespace KernelHive {

class ClusterProxy : public TCPClient {
public:
	ClusterProxy(const char *host, int port);
	virtual ~ClusterProxy();
	void sendUpdate();
};

#endif /* CLUSTERPROXY_H_ */

}
