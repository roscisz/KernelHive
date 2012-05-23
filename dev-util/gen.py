#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import random
import struct

INT_MAX = 2147483647

DEFAULT_TYPE = 'i'
DEFAULT_FILE_NAME = 'data'

if __name__ == '__main__':
	if len(sys.argv) < 2:
		print 'please provide the amount of numbers to generate'
		sys.exit(1)
	name = DEFAULT_FILE_NAME
	amount = int(sys.argv[1])
	numType = sys.argv[2] if len(sys.argv) > 3 else DEFAULT_TYPE

	if numType == 'i':
		genFunc = random.randint
		maxVal = INT_MAX
	elif numType == 'f':
		genFunc = random.uniform
		maxVal = 10

	data = open(name, 'wb')
	for i in range(0, amount):
		data.write(struct.pack(numType, genFunc(0, maxVal)))
	data.close()
	
