#!/usr/bin/env python
# -*- coding: utf-8 -*-

##
# sockclient.py read|write port [file] 
#
# A simple socket client. Allows to operate in either of 2 modes:
# 	- write: will read input and wait for a socket connection on a given
#		port to send that data
#	- read: will attempt to read data from a given port
#
# If in the write mode, the client will wait for user input and send it
# over the socket. If the file is specified, the file's content will be sent
# instead.
#

import sys
import socket
import os

HOST = "localhost"
READ_BATCH = 1024

if __name__ == "__main__":
	if len(sys.argv) < 3:
		sys.exit("both command (read|write) and connection port must be specified")

	command = sys.argv[1]
	host = HOST
	port = int(sys.argv[2])

	if command == "read":
		sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		sock.connect((host, port))
		while 1:
			data = sock.recv(READ_BATCH)
			if not data:
				break
			print data
		sock.close()
	elif command == "write":
		if len(sys.argv) > 3:
			fsize = os.path.getsize(sys.argv[3])
			print fsize
			source = open(sys.argv[3], "r")
		else:
			print "file not provided"
			sys.exit(1)
		sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		sock.bind((host, port))
		sock.listen(1)
		conn, addr = sock.accept()
		print "connection from ", addr
		conn.sendall(str(fsize))
		while 1:
			try:
				line = source.readline()
			except:
				KeyboardInterrupt
			if not line:
				break
			conn.sendall(line)
		conn.close()
		sock.close()

