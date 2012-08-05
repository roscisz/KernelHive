#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import random
import struct

DEFAULT_FILE_NAME = 'data'

if __name__ == '__main__':
	if len(sys.argv) < 3:
		print 'please provide the range'
		sys.exit(1)
	name = DEFAULT_FILE_NAME
	f = int(sys.argv[1])
	t = int(sys.argv[2])

	data = open(name, 'wb')
	data.write(struct.pack('f', f))
	data.write(struct.pack('f', t))
	data.close()
	
