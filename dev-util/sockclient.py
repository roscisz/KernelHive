#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import socket
import os
import struct
import random

HOST = "localhost"
READ_BATCH = 1496
INT_MAX = 2147483647
OUTPUT_FILE = 'output'

COUNTER = 0

def nextNum():
	global COUNTER
	COUNTER = COUNTER + 1
	return COUNTER

DATA_ID = nextNum()

def btoi(strn):
	return struct.unpack("<I", strn)[0]

def itob(num):
	return struct.pack("i", num)

if __name__ == "__main__":
	if len(sys.argv) < 2:
		sys.exit("connection port must be specified")

	host = HOST
	port = int(sys.argv[1])

	if len(sys.argv) > 2:
		fsize = os.path.getsize(sys.argv[2])
	else:
		sys.exit("file not provided")

	ofile = OUTPUT_FILE + str(DATA_ID);

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
			msg = msg[4:] # We don't need no message sizes
			msgSize = len(msg)
			#print map(ord, msg)
			print 'Received message of size ', msgSize
			if btoi(msg[0:4]) == 1:
				conn.sendall(itob(fsize))
			elif btoi(msg[0:4]) == 2:
				source = open(sys.argv[2], 'r')
				while 1:
					data = source.read(READ_BATCH)
					if not data:
						source.close()
						break
					conn.sendall(data)
			elif btoi(msg[0:4]) == 0:
				conn.sendall(itob(DATA_ID))
			elif btoi(msg[0:4]) == 4:
				datId = btoi(msg[4:8])
				pkgSize = btoi(msg[8:12])
				print 'Received a data package of size ', pkgSize
				with open(ofile, 'ab') as out:
					out.write(msg[12:msgSize])
			else:
				print 'Unknown command'

		conn.close()
		print 'Connection closed'

	sock.close()

