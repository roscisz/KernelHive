#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <syslog.h>
#include <exception>

#include "UnitManager.h"
#include "commons/Logger.h"

using namespace std;

void daemonize();
void configureLogs();
void forkAndExitParent();
void createNewSession();
void prepareWorkingDirectory();

int main() {
	//daemonize();
	configureLogs();

	// TODO: Read configuration
	// TODO: Connect to signals

	KernelHive::UnitManager* unitManager = new KernelHive::UnitManager();
	unitManager->listen();

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
    	KernelHive::Logger::log(KernelHive::FATAL, msg);
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