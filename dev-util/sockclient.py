#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import socket

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
			source = open(sys.argv[3], "r")
		else:
			source = sys.stdin
		sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		sock.bind((host, port))
		sock.listen(1)
		conn, addr = sock.accept()
		print "connection from ", addr
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

