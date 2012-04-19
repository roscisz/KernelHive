/*
 * Base class for all threading objects.
 */
#ifndef THREAD_H_
#define THREAD_H_

namespace KernelHive {

class Thread {
public:
	virtual void run() = 0;
	virtual void pleaseStop() = 0;
	virtual ~Thread() { }

};

}

#endif /* THREAD_H_ */
