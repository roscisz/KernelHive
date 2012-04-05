#include <cstdio>
#include <cstdlib>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

#include "UnitManager.h"
using namespace std;

void daemonize(void)
{
    pid_t pid, sid;

    if ( getppid() == 1 ) return;

    pid = fork();
    if (pid < 0) {
        exit(EXIT_FAILURE);
    }
    if (pid > 0) {
        exit(EXIT_SUCCESS);
    }

    umask(0);

    sid = setsid();
    if (sid < 0) {
        exit(EXIT_FAILURE);
    }

    if ((chdir("/")) < 0) {
        exit(EXIT_FAILURE);
    }
}

void configure_logs() {
	// TODO: Prepare syslog
	/*close(STDIN_FILENO);
	close(STDOUT_FILENO);
	close(STDERR_FILENO);*/
    /*freopen( "/dev/null", "r", stdin);
    freopen( "/dev/null", "w", stdout);
    freopen( "/dev/null", "w", stderr);*/
}

int main() {
	// Don't demonize for the sake of easy debug
	//daemonize();
	configure_logs();

	// TODO: Read configuration
	// TODO: Connect to signals

	UnitManager* unitManager = new UnitManager();

	// Hand control to the unitManager
	unitManager->listen();

	delete unitManager;

	return 0;
}
