#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import socket
import os

HOST = "localhost"
READ_BATCH = 1024

if __name__ == "__main__":
	if len(sys.argv) < 2:
		sys.exit("connection port must be specified")

	host = HOST
	port = int(sys.argv[1])

	if len(sys.argv) > 2:
		fsize = os.path.getsize(sys.argv[2])
	else:
		sys.exit("file not provided")

	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	sock.bind((host, port))
	sock.listen(1)
	conn, addr = sock.accept()
	print "connection from ", addr
	
	while 1:
		msg = conn.recv(1024)
		if not msg:
			break
		msg = msg.strip()
		print msg
		if msg == 'SIZE':
			print 'SIZE'
			conn.sendall(str(fsize))
		elif msg == 'GET':
			print 'GET'
			source = open(sys.argv[2], 'r')
			while 1:
				line = source.readline()
				if not line:
					source.close()
					break
				conn.sendall(line)
	
	conn.close()
	sock.close()

