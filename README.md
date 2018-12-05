# Project-4

Note:

Command to change to database path
```
mongod --dbpath /path/to/Project-4/data
```

access database using `mongo` > `use FacebookLite`

## Notes on database

Dec 04: 
 - Created `registeredUser` to hold all registered users
 - Collection formatting:
   ```
   "username" | "password" | "firstName" | "lastName" | "age" (int) 
   ```
 - Created `DatabaseController.java` to hold all the codes to read, write, edit, etc to the `FacebookLite` database
