import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *  All methods to read and edit the database should go here
 */
public class DatabaseController {

    private MongoClient client;
    private MongoDatabase db;

    // constructor
    public DatabaseController() {
        // connecting to server on default port 27017
        client = MongoClients.create();
        // accessing db FacebookLite
        db = client.getDatabase("FacebookLite");
    }

    public void RegisterNewUser() {
        // accessing registeredUser table (collection)
        MongoCollection<Document> collRD = db.getCollection("registeredUser");

        // creating new user document based on the input from UI - hard-coded for testing, TODO use variables
        Document newUser = new Document("username", "hello")
                .append("password", "world")
                .append("firstName", "Hello")
                .append("lastName", "World")
                .append("age", 50);

        // insert newUser into registeredUser collection
        collRD.insertOne(newUser);
    }

    // ------------------------------------------------------------------ //

    // test + debug purposes only
    public static void main(String[] args) {
        DatabaseController dbc = new DatabaseController();
        dbc.RegisterNewUser();
    }

}
