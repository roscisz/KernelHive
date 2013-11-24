#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <syslog.h>
#include <exception>
#include <vector>

#include "commons/ByteBuffer.h"
#include "commons/KhUtils.h"
#include "commons/Logger.h"
#include "network/UDPClient.h"
#include "threading/ThreadManager.h"
#include "threading/Thread.h"

#include "CyclicSystemMonitor.h"
#include "HostStatus.h"
#include "HostStatusSerializer.h"
#include "UnitManager.h"

#define MONITOR_INTERVAL 1000
#define MONITORING_PORT 31341

using namespace std;
using namespace KernelHive;

void daemonize();
void configureLogs();
void forkAndExitParent();
void createNewSession();
void prepareWorkingDirectory();

int main(int argc, char* argv[]) {
	if(argc < 2) {
    	Logger::log(FATAL, "No cluster hostname specified.\n");
		exit(EXIT_FAILURE);
	}
	char* clusterAddress = argv[1];

	//daemonize();
	configureLogs();

	// TODO: Read configuration
	// TODO: Connect to signals

	try {
		CyclicSystemMonitor* monitoringThread = new CyclicSystemMonitor(
				new UDPClient(new NetworkAddress(clusterAddress, MONITORING_PORT)), MONITOR_INTERVAL);

		ThreadManager* threadManager = new ThreadManager();
		threadManager->runThread(monitoringThread);

		UnitManager* unitManager = new UnitManager(clusterAddress, monitoringThread->getSystemMonitor());
		unitManager->listen();
	} catch(string &e) {
		cerr << "Exception: " << e << endl;
	}

	return 0;
}

void daemonize()
{
	try {
		forkAndExitParent();
		createNewSession();
		prepareWorkingDirectory();
	}
    catch(const char *msg) {
    	Logger::log(FATAL, msg);
		exit(EXIT_FAILURE);
    }
}

void configureLogs() {
	// TODO: Prepare syslog

	/*
	Logger::configure()...
	close(STDIN_FILENO);
	close(STDOUT_FILENO);
	close(STDERR_FILENO);*/
}

void forkAndExitParent() {
	pid_t pid = fork();
	if (pid < 0)
		throw("fork() failed.\n");
	if (pid > 0)
		exit(EXIT_SUCCESS);
}

void createNewSession() {
    pid_t sid = setsid();
    if (sid < 0)
    	throw("setsid() failed.\n");
}

void prepareWorkingDirectory() {
    umask(0);
    if ((chdir("/")) < 0)
    	throw("chdir() failed.\n");
}
