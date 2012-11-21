#!/usr/bin/env python

import sys
from time import mktime
from time import strptime

FORMAT = '%H:%M:%S'

if __name__ == '__main__':
	s1 = sys.argv[1]
	s2 = sys.argv[2]
	t1 = mktime(strptime(s1, FORMAT))
	t2 = mktime(strptime(s2, FORMAT))
	print str(int(t1 - t2))
