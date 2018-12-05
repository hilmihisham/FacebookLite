import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

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
        dbc.GetUser("tom");
    }

}
