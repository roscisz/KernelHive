import time
import util
from util import StoppableThread


class MonitoringWorker(StoppableThread):
    def __init__(self, client, node_data, monitoring_tasks):
        StoppableThread.__init__(self)
        self.client = client
        self.node_data = node_data
        self.monitoring_tasks = monitoring_tasks

    def monitor(self, client, node_data):
        for monitoring_task in self.monitoring_tasks:
            node_data[monitoring_task.get_key()] = monitoring_task.monitor(client, node_data[monitoring_task.get_key()])

    def do_run(self):
        self.monitor(self.client, self.node_data)
        # TODO: sleep period as arg
        time.sleep(1)

    def finalize(self):
        self.client.close()


class MonitoringService(StoppableThread):
    def __init__(self, nodes, tasks, handlers):
        StoppableThread.__init__(self)
        self.clients = self.setup_ssh(nodes)
        self.tasks = tasks
        self.handlers = handlers
        self.infrastructure = self.discover_infrastructure(self.clients, self.tasks)
        self.workers = self.start_workers(self.infrastructure, self.clients, self.tasks)

    def start_workers(self, infrastructure, clients, tasks):
        workers = [MonitoringWorker(clients[node], infrastructure[node], tasks) for node in infrastructure.keys()]
        for worker in workers:
            worker.start()
        return workers

    def setup_ssh(self, nodes):
        return {node: util.setup_ssh_client(node) for node in nodes}

    def discover_node(self, client, tasks):
        return {task.get_key(): task.discover(client) for task in tasks}

    def discover_infrastructure(self, clients, tasks):
        return {node: self.discover_node(clients[node], tasks) for node in clients.keys()}

    def do_run(self):
        for handler in self.handlers:
            handler.handle(self.infrastructure)
        # TODO: sleep period as arg
        time.sleep(5)

    def finalize(self):
        for worker in self.workers:
            worker.join()

    def shutdown(self):
        for worker in self.workers:
            worker.shutdown()
        for node in self.clients.keys():
            self.clients[node].close()
        StoppableThread.shutdown(self)

