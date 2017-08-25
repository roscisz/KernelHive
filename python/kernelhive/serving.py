import os
from werkzeug.wrappers import Request, Response
from werkzeug.wsgi import SharedDataMiddleware
from werkzeug.serving import make_server
from jsonrpc import JSONRPCResponseManager, dispatcher


class HTTPJSONRPCServer:
    def __init__(self, hostname, port):
        application = SharedDataMiddleware(self.application,
                                           {'/': os.path.join(os.getcwd(), 'files/static')})
        self.srv = make_server(hostname, port, application)

    @staticmethod
    @Request.application
    def application(request):
        path = request.path.split('/')
        if len(path) > 2 and path[1] == 'dynamic':
            with open(os.path.join(os.getcwd(), 'files/dynamic/' + path[2]), 'rb') as f:
                data = f.read()
            response = Response(data, mimetype='application/octet-stream')
            response.headers['Cache-Control'] = 'no-cache, no-store, must-revalidate'
            response.headers['Pragma'] = 'no-cache'
            response.headers['Expires'] = '0'
            return response
        response = JSONRPCResponseManager.handle(request.data, dispatcher)
        return Response(response.json, mimetype='application/json')

    def add_service(self, method):
        dispatcher.add_method(method)

    def server_forever(self):
        print('Starting the HTTPJSONServer at http://%s:%d' % (self.srv.host, self.srv.port))
        self.srv.serve_forever()

    def shutdown(self):
        print('Shutting down the HTTPJSONSever...')
        self.srv.shutdown()
