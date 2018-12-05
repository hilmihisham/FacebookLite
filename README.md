# Project-4

Note:

Command to change to database path
```
mongod --dbpath /path/to/Project-4/data
```

access database using `mongo` > `use FacebookLite`

## Notes on database

Dec 04: 
 - Created `registeredUser` to hold all registered users (name changed from `registerData`)
 - Collection formatting:
   ```
   "username" | "password" | "firstName" | "lastName" | "age" (int) 
   ```
 - Created `DatabaseController.java` to hold all the codes to read, write, edit, etc to the `FacebookLite` database

## Experimenting with database 
```
// creating collection (table)
> db.createCollection("registerData")
{ "ok" : 1 }

> show collections
registerData

// inserting data into a collection
> db.registerData.insert({"username":"admin","password":"admin","firstName":"admin","lastName":"facebook","age":"100"})
WriteResult({ "nInserted" : 1 })

// listing out all data in a collection
> db.registerData.find().pretty()
{
        "_id" : ObjectId("5c0720e34db1df1ff2cc9f85"),
        "username" : "admin",
        "password" : "admin",
        "firstName" : "admin",
        "lastName" : "facebook",
        "age" : "100"
}

// finding data using <key, value> pair
> db.registerData.find({"password":"admin"}).pretty()
{
        "_id" : ObjectId("5c0720e34db1df1ff2cc9f85"),
        "username" : "admin",
        "password" : "admin",
        "firstName" : "admin",
        "lastName" : "facebook",
        "age" : "100"
}

// finding data using multiple <key, value> pair
> db.registerData.find({$and: [{"username":"admin"}, {"password":"admin"}]}).pretty()
{
        "_id" : ObjectId("5c0720e34db1df1ff2cc9f85"),
        "username" : "admin",
        "password" : "admin",
        "firstName" : "admin",
        "lastName" : "facebook",
        "age" : "100"
}
```