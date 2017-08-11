from werkzeug.wrappers import Request, Response
from werkzeug.serving import make_server
from jsonrpc import JSONRPCResponseManager, dispatcher
from threading import Thread

from kernelhive.monitoring import *
from kernelhive.connectivity import ConnectionManager


class Manager(Thread):
    def __init__(self, hostname, port, monitors, handlers):
        Thread.__init__(self)
        self.monitors = monitors
        self.handlers = handlers

        self.configure_services()
        self.configure_handlers()
        self.configure_monitors()

        self.connection_manager = ConnectionManager()
        self.monitoring_service = MonitoringService(self.monitors, self.handlers, self.connection_manager)

        self.srv = make_server(hostname, port, self.application)

    def configure_monitors(self):
        pass

    def configure_handlers(self):
        pass

    def configure_services(self):
        self.add_service(self.add_node)

    def add_service(self, method):
        dispatcher.add_method(method)

    def add_node(self, node_hostname):
        self.monitoring_service.add_node(node_hostname)

    def shutdown(self):
        self.monitoring_service.shutdown()
        self.monitoring_service.join()
        self.srv.shutdown()

    @staticmethod
    @Request.application
    def application(request):
        response = JSONRPCResponseManager.handle(request.data, dispatcher)
        return Response(response.json, mimetype='application/json')

    def run(self):
        self.monitoring_service.start()
        self.srv.serve_forever()
