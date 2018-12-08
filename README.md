# Project-4

Note:

Command to change to database path
```
mongod --dbpath /path/to/Project-4/data
```

access database using `mongo` > `use FacebookLite`

## Database Overview

### Created `DatabaseController.java` to hold all the codes to read, write, edit, etc to the `FacebookLite` database
 - just call `DatabaseController.java` from any UI controller and it will open a connection to the database
 - `DatabaseController.java` will be populated with methods to be use in different situation to read/write/etc to database
 - each different method will need to specify which Collection (Table) to access to before doing anything that needs to be done

### `registeredUser`: 
 - Created `registeredUser` to hold all registered users 
 - Collection formatting:
   ```
   "username" | "password" | "firstName" | "lastName" | "age" (int) | "secureQ" | secureA
   ```

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