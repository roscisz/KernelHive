#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import socket
import os
import struct
import random

# Global variables
HOST = "localhost"
READ_BATCH = 1496
INT_MAX = 2147483647
OUTPUT_FILE = 'output'
COUNTER = 0

# Utility functions
def nextNum():
	global COUNTER
	COUNTER = COUNTER + 1
	return COUNTER

def btoi(strn):
	return struct.unpack("<I", strn)[0]

def itob(num):
	return struct.pack("i", num)

def register(repo, ident, path):
	print 'Registering %s with ID %d' % (path, ident)
	repo[ident] = path

# Sockclient main
if __name__ == "__main__":
	if len(sys.argv) < 2:
		sys.exit("Connection port must be specified")

	host = HOST
	port = int(sys.argv[1])
	repo = { }

	if len(sys.argv) > 2:
		i = 2;
		while i < len(sys.argv):
			ident = nextNum()
			register(repo, ident, sys.argv[i])
			i += 1
	else:
		sys.exit("File not provided")

	#ofile = OUTPUT_FILE + str(nextNum());

	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	sock.bind((host, port))
	sock.listen(1)

	while 1:
		conn, addr = sock.accept()
		print "Connection from ", addr
		while 1:
			msg = conn.recv(READ_BATCH)
			if not msg:
				break
			msg = msg[4:] # We don't need no message sizes
			msgSize = len(msg)
			#print map(ord, msg)
			#print 'Received message of size ', msgSize
			if btoi(msg[0:4]) == 1: # GETSIZE
				ident = btoi(msg[4:8])
				fSize = os.path.getsize(repo[ident])
				print 'GETSIZE %d (will repond with %d)' % (ident, fSize)
				conn.sendall(itob(fSize))
			elif btoi(msg[0:4]) == 2: # GET
				ident = btoi(msg[4:8])
				source = open(repo[ident], 'r')
				print 'GET %d (will respond with data from %s)' % (ident, repo[ident])
				while 1:
					data = source.read(READ_BATCH)
					if not data:
						source.close()
						break
					conn.sendall(data)
			elif btoi(msg[0:4]) == 0: # ALLOCATE
				ident = nextNum()
				outFile = OUTPUT_FILE + str(ident)
				print 'ALLOCATE %d (will repond with %d)' % (btoi(msg[4:8]), ident)
				register(repo, ident, outFile)
				conn.sendall(itob(ident))
			elif btoi(msg[0:4]) == 4: # PUT
				datId = btoi(msg[4:8])
				pkgSize = btoi(msg[8:12])
				ofile = repo[datId]
				print 'PUT %d %d (will write to %s)' % (datId, pkgSize, ofile)
				with open(ofile, 'ab') as out:
					out.write(msg[12:msgSize])
			else:
				print 'Unknown command'

		conn.close()
		print 'Connection closed'

	sock.close()

