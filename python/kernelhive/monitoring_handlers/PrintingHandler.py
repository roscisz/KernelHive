from kernelhive.monitoring import MonitoringHandler


class PrintingHandler(MonitoringHandler):
    def handle_monitoring(self, infrastructure):
        print(str(infrastructure))

