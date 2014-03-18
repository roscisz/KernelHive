/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
#ifndef WORKER_H_
#define WORKER_H_

#define WORKER_STD_ARGS		5

#include "../threading/ThreadManager.h"
#include "../threading/SynchronizedBuffer.h"
#include "../network/NetworkAddress.h"
#include "../network/UDPReporter.h"
#include "../network/IReportable.h"
#include "../network/ProgressReporter.h"

namespace KernelHive {

class Worker : public IReportable {
public:
	Worker(char **argv);
	int getPercentDone();
	virtual void work(char *const argv[]) = 0;
	virtual ~Worker();
protected:
	/** The thread manager - allows to run and control thread operations. */
	ThreadManager* threadManager;

	/** An index variable used to extract worker parameters from the parameters array. */
	int paramOffset;

	/**
	 * Returns the parameter pointed by the paramOffset and increments
	 * the paramOffset value.
	 *
	 * @param params the worker parameters array
	 * @return the parameter which was pointed by the paramOffset
	 */
	char* nextParam(char *const argv[]);

	void setPercentDone(int percentDone);

	void reportOver(const char* uploadIDs);

	void reportPreview(SynchronizedBuffer *buffer);

private:
	NetworkAddress *clusterTCPAddress;
	UDPReporter *reporter;
	ProgressReporter *previewReporter;
	int percentDone;
	int jobID;
};

}

#endif /* WORKER_H_ */
