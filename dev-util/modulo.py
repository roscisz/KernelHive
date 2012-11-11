#!/usr/bin/env python

import sys

alf = [ 'a', 'b', 'c', 'd',
		'e', 'f', 'g', 'h',
		'i', 'j', 'k', 'l',
		'm', 'n', 'o', 'p',
		'q', 'r', 's', 't',
		'u', 'v', 'w', 'x',
		'y', 'z' ] 

n = len(alf)

def calc(state):
	p = []
	while state >= 0:
		diff = state % n
		p.append(alf[diff])
		state -= diff
		state -= n
		state /= n
	p.reverse()
	print ''.join(p)

def calc2(pswd):
	total = 0
	idx = 0
	for char in reversed(list(pswd)):
		total += (ord(char)-96) * n**idx
		idx += 1
	print total - 1

if __name__ == '__main__':
	val = sys.argv[1]
	try:
		calc(long(val))
	except ValueError:
		calc2(val)

