#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import socket
import os
import struct
import re
import random

HOST = "localhost"
READ_BATCH = 1024

INT_MAX = 2147483647

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

	while 1:
		conn, addr = sock.accept()
		print "connection from ", addr
		while 1:
			msg = conn.recv(1024)
			if not msg:
				break
			msg = msg.strip()
			#print 'Received message: ', msg
			#print len(msg)
			if re.match(r'1 \d+', msg):
				conn.sendall(str(fsize))
			elif re.match(r'2 \d+', msg):
				source = open(sys.argv[2], 'r')
				while 1:
					data = source.read(READ_BATCH)
					if not data:
						source.close()
						break
					conn.sendall(data)
			elif re.match(r'0 (\d+) (.*)', msg):
				matcher = re.match(r'0 (\d+) (.*)', msg)
				conn.sendall(str(random.randint(0, INT_MAX)))
				print len(matcher.group(2))
				with open('output', 'ab') as out:
					#out.write(matcher.group(2))
					out.write(msg)
			else:
				print len(msg)
				with open('output', 'ab') as out:
					out.write(msg)

		conn.close()
		print 'Connection closed'

	sock.close()

