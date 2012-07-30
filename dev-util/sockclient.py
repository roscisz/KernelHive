#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import socket
import os
import struct
import re
import random

HOST = "localhost"
READ_BATCH = 20480
INT_MAX = 2147483647
OUTPUT_FILE = 'output'

DATA_ID = str(random.randint(0, 100))

if __name__ == "__main__":
	if len(sys.argv) < 2:
		sys.exit("connection port must be specified")

	host = HOST
	port = int(sys.argv[1])

	if len(sys.argv) > 2:
		fsize = os.path.getsize(sys.argv[2])
	else:
		sys.exit("file not provided")

	ofile = OUTPUT_FILE + DATA_ID;

	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	sock.bind((host, port))
	sock.listen(1)

	while 1:
		conn, addr = sock.accept()
		print "connection from ", addr
		while 1:
			msg = conn.recv(READ_BATCH)
			if not msg:
				break
			msg = msg.strip()
			print 'Received message: ', msg
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
			elif re.match(r'0 (\d+)', msg):
				conn.sendall(DATA_ID)
			elif re.match(r'4 (\d+) (\d+)', msg):
				matcher = re.match(r'4 (\d+) (\d+) (.*)', msg, re.DOTALL)
				print 'Received a package of size ', matcher.group(2)
				with open(ofile, 'ab') as out:
					out.write(matcher.group(3))
			else:
				with open(ofile, 'ab') as out:
					out.write(msg)

		conn.close()
		print 'Connection closed'

	sock.close()

