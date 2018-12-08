import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import static com.mongodb.client.model.Filters.and;

/**
 *  All methods to read and edit the database should go here
 */
public class DatabaseController {

    private MongoClient client;
    private MongoDatabase db;

    // constructor
    public DatabaseController() {
        client = MongoClients.create(); // connecting to server on default port 27017
        db = client.getDatabase("FacebookLite"); // accessing db FacebookLite
    }

    // closing connection to database
    public void closeConnection() {
        client.close();
        System.out.println("Connection to db closed.");
    }

    // registering new user from "Register" FXML page
    public boolean RegisterNewUser(String un, String pw, String fn, String ln, String sq, String sa, int age) {

        boolean success;

        if (isUsernameExist(un)) {
            success = false;
        }
        else {
            // accessing registeredUser table (collection)
            MongoCollection<Document> collRU = db.getCollection("registeredUser");

            // creating new user document based on the input from UI
            Document newUser = new Document("username", un)
                    .append("password", pw)
                    .append("firstName", fn)
                    .append("lastName", ln)
                    .append("age", age)
                    .append("secureQ", sq)
                    .append("secureA", sa);

            // insert newUser into registeredUser collection
            collRU.insertOne(newUser);
            System.out.println("Username " + un + " is registered!");
            success = true;
        }

        return success;
    }

    public boolean isUsernameExist(String un) {

        boolean exist = false;

        // accessing registeredUser table (collection)
        MongoCollection<Document> collection = db.getCollection("registeredUser");

        // ----- get document of given username from registeredData -----
        System.out.println("\nVerifying if username already exists or not\n--------------------\n");
        String colUsername = "username"; // set key and value to look for in document
        FindIterable<Document> docOne = collection.find(Filters.eq(colUsername, un)); // find document by filters
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents
        try {
            // cursor1 will only have 1 data if we found a match
            if (cursor1.hasNext()) {
                System.out.println("Username already exists");
                exist = true;
            }
            else {
                System.out.println("Username is available to register");
            }
        } finally {
            cursor1.close();
        }

        return exist;
    }

    // can be used for login purposes
    public void LoginUser(String username, String password) {

        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // ----- get document of matching u/n and p/w from registeredData -----
        String colUsername = "username", colPassword = "password"; // set key and value to look for in document

        // find document by filters
        FindIterable<Document> docOne = collRU.find(
                and(
                    (Filters.eq(colUsername, username)),
                    (Filters.eq(colPassword, password))
                ));
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents

        try {
            // cursor1 will only have 1 data if we found a match
            if (cursor1.hasNext()) {

                Document userDoc = cursor1.next(); // JSON string

                System.out.println("\n\nPrint " + username + "\'s data\n--------------------\n");
                System.out.println(userDoc.toJson()); // print username's document in JSON

                String fn = userDoc.getString("firstName");
                String ln = userDoc.getString("lastName");
                int age = userDoc.getInteger("age");

                //System.out.println(userDoc.get("lastName")); // getting data from user document using key
                User user = new User(fn, ln, age);
            }
            else {
                System.out.println("username and/or password didn\'t match");
            }
        }
        finally {
            cursor1.close();
        }

    }

    // can be use to get friend's profile
    public void GetUser(String username) {

        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // ----- get document of given username from registeredData -----

        System.out.println("\n\nPrint " + username + "\'s data\n--------------------\n");
        String colUsername = "username"; // set key and value to look for in document
        FindIterable<Document> docOne = collRU.find(Filters.eq(colUsername, username)); // find document by filters
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor1.hasNext()) {
                System.out.println(cursor1.next().toString()); // print username's document in String
            }
        } finally {
            cursor1.close();
        }

        // ----- get all documents from registeredUser (example) -----
        /*
        System.out.println("Print all users\n---------------\n");
        FindIterable<Document> docAll = collRU.find(); // get all documents
        MongoCursor<Document> cursor = docAll.iterator(); // set up cursor tu iterate each rows of documents
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson()); // print a row in JSON
            }
        } finally {
            cursor.close();
        }
        */
    }

    // ------------------------------------------------------------------ //
    // test + debug purposes only

    public static void main(String[] args) {
        DatabaseController dbc = new DatabaseController();
        //dbc.RegisterNewUser();
        //dbc.GetUser("tom");
        //dbc.LoginUser("admin", "admin1");
    }
}
