/*
 * ClusterProxy.h
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#ifndef CLUSTERPROXY_H_
#define CLUSTERPROXY_H_

#include "UnitManager.h"

class ClusterProxy {
private:
	UnitManager *listener;
	int sockfd;
public:
	ClusterProxy(const char *host, int port, UnitManager *listener);
	virtual ~ClusterProxy();
	void sendMessage(char *msg);
};

#endif /* CLUSTERPROXY_H_ */
