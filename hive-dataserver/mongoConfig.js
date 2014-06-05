db = new Mongo().getDB("admin");
db.createUser(
  {
    user: "hive-dataserver",
    pwd: "hive-dataserver",
    roles:
    [
      {
        role: "userAdminAnyDatabase",
        db: "admin" 
      }
      ,
      {
        role: "readWriteAnyDatabase",
        db: "admin" 
      }
    ]
  }
);
