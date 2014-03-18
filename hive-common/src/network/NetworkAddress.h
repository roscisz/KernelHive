/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
 * Copyright (c) 2014 Pawel Rosciszewski
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
#ifndef NETWORKADDRESS_H_
#define NETWORKADDRESS_H_

#define MAX_HOSTPORT_SIZE 50

namespace KernelHive {

class NetworkAddress {
public:
	NetworkAddress(char *host, int port);
	NetworkAddress(char *host, char *port);
	virtual ~NetworkAddress();
	const char* toString();

	char *host;
	int port;

};

}

#endif /* NETWORKADDRESS_H_ */
