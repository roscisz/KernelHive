from threading import Thread

from monitoring import *
from connectivity import ConnectionManager
from serving import HTTPJSONRPCServer


class Manager(Thread):
    def __init__(self, hostname, port, monitors, handlers):
        Thread.__init__(self)
        self.monitors = monitors
        self.handlers = handlers

        self.server = HTTPJSONRPCServer(hostname, port)
        self.configure_services()
        self.configure_handlers()
        self.configure_monitors()

        self.connection_manager = ConnectionManager()
        self.monitoring_service = MonitoringService(self.monitors, self.handlers, self.connection_manager)

    def configure_monitors(self):
        pass

    def configure_handlers(self):
        pass

    def configure_services(self):
        self.add_service(self.add_node)

    def add_service(self, method):
        self.server.add_service(method)

    def add_node(self, node_hostname):
        self.monitoring_service.add_node(node_hostname)

    def shutdown(self):
        print('Shutting down the manager...')
        self.monitoring_service.shutdown()
        self.monitoring_service.join()
        self.connection_manager.shutdown()
        self.server.shutdown()

    def run(self):
        self.monitoring_service.start()
        self.server.server_forever()
