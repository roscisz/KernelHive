import signal
import time

from kernelhive.monitoring import MonitoringHandler
from kernelhive.management import Manager
from kernelhive.monitors.ProcessMonitor import ProcessMonitor
from kernelhive.monitoring_handlers.PrintingHandler import PrintingHandler

stop = False


class ProcessEnforcer(Manager, MonitoringHandler):
    def __init__(self, hostname, port, monitors, handlers):
        self.process_monitor = ProcessMonitor()
        Manager.__init__(self, hostname, port, monitors, handlers, landing_page='process_enforcer.html')

    def configure_services(self):
        Manager.configure_services(self)
        self.add_service(self.add_process)

    def configure_monitors(self):
        self.monitors.append(self.process_monitor)

    def configure_handlers(self):
        self.handlers.append(self)

    def add_process(self, process):
        self.process_monitor.add_process(process)

    def shutdown(self):
        Manager.shutdown(self)
        self.connection_manager.shutdown_connections()

    def handle_monitoring(self, infrastructure):
        for node in infrastructure.keys():
            for process in infrastructure[node]['processes'].keys():
                if not len(infrastructure[node]['processes'][process]):
                    print('Enforcing %s on %s ' % (process, node))
                    self.connection_manager.run_command(node, process)


def main():
    # TODO: read server hostname, port host list and command from commandline
    manager = ProcessEnforcer('localhost', 31333, [], [PrintingHandler()])
    manager.start()

    def shutdown(signum, frame):
        manager.shutdown()
        global stop
        stop = True

    signal.signal(signal.SIGINT, shutdown)
    signal.signal(signal.SIGTERM, shutdown)

    while not stop:
        time.sleep(5)

    manager.join()

if __name__ == "__main__":
    main()
