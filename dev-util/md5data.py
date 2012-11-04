#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import struct
import hashlib

DATA_FILE_NAME = 'input'

if __name__ == '__main__':
	if len(sys.argv) < 4:
		print 'Expected arguments: password string, state from, state to'
		sys.exit(1)

	passwd = sys.argv[1];
	stateFrom = long(sys.argv[2])
	stateTo = long(sys.argv[3])

	m = hashlib.md5()
	m.update(passwd)
	dgst = m.digest()

	data = open(DATA_FILE_NAME, 'wb')
	data.write(dgst)
	data.write(struct.pack('qq', stateFrom, stateTo))
	data.close()
	
