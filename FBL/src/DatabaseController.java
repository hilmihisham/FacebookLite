import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocumentReader;
import org.bson.BsonReader;
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

    public void RegisterNewUser() {
        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // creating new user document based on the input from UI - hard-coded for testing, TODO use variables
        Document newUser = new Document("username", "tom")
                .append("password", "myspace")
                .append("firstName", "Tom")
                .append("lastName", "MySpace")
                .append("age", 15);

        // insert newUser into registeredUser collection
        collRU.insertOne(newUser);
    }

    // can be used for login purposes
    public void LoginUser(String username, String password) {

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
                int age = Integer.parseInt(userDoc.getString("age"));

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

        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // ----- get all documents from registeredUser -----

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

        // ----- get document of username "tom" from registeredData -----
        System.out.println("\n\nPrint " + username + "\'s data\n--------------------\n");
        String colUsername = "username", searchedUser = username; // set key and value to look for in document
        FindIterable<Document> docOne = collRU.find(Filters.eq(colUsername, searchedUser)); // find document by filters
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor1.hasNext()) {
                System.out.println(cursor1.next().toString()); // print username's document in String
            }
        } finally {
            cursor1.close();
        }

    }

    // ------------------------------------------------------------------ //

    // test + debug purposes only
    public static void main(String[] args) {
        DatabaseController dbc = new DatabaseController();
        //dbc.RegisterNewUser();
        //dbc.GetUser("tom");
        dbc.LoginUser("admin", "admin1");
    }

}
