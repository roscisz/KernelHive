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
#include "network/TCPClient.h"
#include "network/TCPClientListener.h"
#include "network/NetworkAddress.h"
#include "HostStatus.h"

namespace KernelHive {

class ClusterProxy : public TCPClient {
public:
	ClusterProxy(NetworkAddress *clusterAddress, TCPClientListener *listener);
	virtual ~ClusterProxy();
	void sendUpdate(HostStatus* hoststatus);
};

#endif /* CLUSTERPROXY_H_ */

}
