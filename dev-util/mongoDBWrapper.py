import gridfs
from pymongo import MongoClient


class MongoDBWrapper:

    counterName = "package"

    def __init__(self, host=None, port=None):
        database = MongoClient(host, port)['hive-dataserver']
        database.authenticate('hive-dataserver', 'hive-dataserver', source='admin')
        self.counters = database.counters
        self.packages = gridfs.GridFS(database)
        if self.counters.find({"_id": self.counterName}).count() != 1:
            self.counters.insert({"_id": self.counterName, "seq": 0})

    def allocate(self):
        newid = self.counters.find_and_modify(query={'_id': self.counterName}, update={'$inc': {'seq': 1}}, new=True)['seq']
        return newid

    def get_size(self, id):
        return self.packages.get(id).length

    def get(self, id):
        return self.packages.get(id).read()

    def delete(self, id):
        return self.packages.delete(id)

    def put(self, data, id):
        self.packages.put(data, _id=id, filename=id)
