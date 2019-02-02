# FacebookLite
Offline "Fakebook" using JavaFX for its GUI and MongoDB for the database. This is a group project made for my CSUN COMP585 (Graphical User Interface) class. 

As per the requirement, the user is able to register and login to their account, reset their password, and toggle privacy setting for their friends list, posts, age, and status. 

In the database, users' password was encrypted with **SHA-256 hashing** using Java inbuilt `MessageDigest` class. The simple implementation of this encryption can be seen in the code [here](https://github.com/hilmihisham/FacebookLite/blob/master/FBL/src/SHAEncryption.java)

## Note:

Command to change to database path
```
mongod --dbpath /path/to/Project-4/data
```

access database using `mongo` > `use FacebookLite`

## Database Overview

### Created `DatabaseController.java` to hold all the codes to read, write, edit, etc to the `FacebookLite` database
 - call `FBLManager.java` to access `DatabaseController.java`
 - `DatabaseController.java` will open a connection to the database
 - `DatabaseController.java` will be populated with methods to be use in different situation to read/write/etc to database
 - each different method will need to specify which Collection (Table) to access to before doing anything that needs to be done

### note: all value in database is in String, unless specified

### `registeredUser`: 
 - Created `registeredUser` to hold all registered users 
 - Collection formatting:
   ```
   "username" | "password" | "firstName" | "lastName" | "age" (int) | "secureQ" | "secureA"
   ```

### `postsRecord`
 - all created posts will be in here
 - Collection formatting:
   ```
   "post" | "date" (java.util.Date) | "username"
   ```

### `followList`
 - everyone will be here, and the list of who they follows
 - Collection formatting:
   ```
   "username" | "following" (ArrayList<String>)
   ```

---

## Experimenting with database 
```
// creating collection (table)
> db.createCollection("registerData")
{ "ok" : 1 }

> show collections
registerData

// inserting data into a collection
> db.registerData.insert
        ({"username":"admin","password":"admin","firstName":"admin","lastName":"facebook","age":"100"})
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
> db.registerData.find
        ({$and: [{"username":"admin"}, {"password":"admin"}]}).pretty()
{
        "_id" : ObjectId("5c0720e34db1df1ff2cc9f85"),
        "username" : "admin",
        "password" : "admin",
        "firstName" : "admin",
        "lastName" : "facebook",
        "age" : "100"
}
```
