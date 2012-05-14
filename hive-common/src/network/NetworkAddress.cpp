/*
 * NetworkAddress.cpp
 *
 *  Created on: 14-05-2012
 *      Author: roy
 */

#include <cstdio>
#include <string.h>
#include "NetworkAddress.h"

NetworkAddress::NetworkAddress(char *host, int port) {
	this->host = host;
	this->port = port;
}

NetworkAddress::NetworkAddress(char *host, char *port) {
	this->host = host;
	sscanf(port, "%d", &this->port);
}

NetworkAddress::~NetworkAddress() {
	// TODO Auto-generated destructor stub
}
