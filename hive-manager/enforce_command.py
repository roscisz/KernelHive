import signal
import time

from kernelhive import util
from kernelhive.monitoring import MonitoringService

stop = False


class ProcessMonitoringTask:
    def __init__(self, command):
        self.command = command

    def get_key(self):
        return 'process (%s)' % self.command

    def discover(self, client):
        _, stdout, _ = client.exec_command('pgrep -f "%s"' % self.command)
        return stdout.read().split('\n')[:-1]

    def monitor(self, client, output):
        return self.discover(client)


class ProcessEnforcingMonitoringHandler:
    def __init__(self, command):
        self.command = command
        self.connections = {}

    def ensure_connection(self, node):
        if node not in self.connections.keys():
            self.connections[node] = util.setup_ssh_client(node)
        return self.connections[node]

    def run_command(self, node, command):
        client = self.ensure_connection(node)
        client.exec_command(command)

    def handle(self, infrastructure):
        for node in infrastructure.keys():
            if not len(infrastructure[node]['process (%s)' % self.command]):
                print('Enforcing %s on %s ' % (self.command, node))
                self.run_command(node, self.command)

    def shutdown(self):
        for node in self.connections.keys():
            self.connections[node].close()


def main():
    # TODO: read host list and command from commandline
    hosts = ['localhost']
    command = 'tail -f /dev/null'

    handler = ProcessEnforcingMonitoringHandler(command)
    task = ProcessMonitoringTask(command)

    monitoring_service = MonitoringService(hosts, [task], [handler])
    monitoring_service.start()

    def shutdown(signum, frame):
        monitoring_service.shutdown()
        handler.shutdown()
        global stop
        stop = True

    signal.signal(signal.SIGINT, shutdown)
    signal.signal(signal.SIGTERM, shutdown)

    while not stop:
        time.sleep(5)

    monitoring_service.join()

if __name__ == "__main__":
    main()
