/*
 * ClusterProxy.cpp
 *
 *  Created on: 05-04-2012
 *      Author: roy
 */

#include "ClusterProxy.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

ClusterProxy::ClusterProxy(const char *host, int port, UnitManager *listener) {

	this->listener = listener;

	struct sockaddr_in serveraddr;
	struct hostent *server;

	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0)
		// TODO: syslog
		;//error("ERROR opening socket");

	server = gethostbyname(host);
	if (server == NULL) {
		// TODO: syslog
		fprintf(stderr, "ERROR, no such host as %s\n", host);
		exit(0);
	}

	bzero((char *) &serveraddr, sizeof(serveraddr));
	serveraddr.sin_family = AF_INET;
	bcopy((char *)server->h_addr,
			(char *)&serveraddr.sin_addr.s_addr, server->h_length);
	serveraddr.sin_port = htons(port);

	if (connect(sockfd, (struct sockaddr *)&serveraddr, sizeof(serveraddr)) < 0)
		//TODO: syslog
		;//error("ERROR connecting");

	// FORK a listening thread
    /*bzero(buf, BUFSIZE);
    n = read(sockfd, buf, BUFSIZE);
    if (n < 0)
      error("ERROR reading from socket");
    printf("Echo from server: %s", buf);*/

}

ClusterProxy::~ClusterProxy() {
}

void ClusterProxy::sendMessage(char *msg) {
	int n = write(sockfd, msg, strlen(msg));
	if (n < 0)
		// TODO: syslog
		;//error("ERROR writing to socket");
}
