import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

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
    public boolean registerNewUser(String un, String pw, String fn, String ln, String sq, String sa, int age) {

        boolean success;

        if (isUsernameExist(un)) {
            success = false;
        }
        else {
            // encrypting input password
            SHAEncryption sha = new SHAEncryption();
            String shaPW = sha.getSHA(pw);

            // accessing registeredUser table (collection)
            MongoCollection<Document> collRU = db.getCollection("registeredUser");

            // creating new user document based on the input from UI
            Document newUser = new Document("username", un)
                    .append("password", shaPW)
                    .append("firstName", fn)
                    .append("lastName", ln)
                    .append("age", age)
                    .append("secureQ", sq)
                    .append("secureA", sa.toLowerCase());

            // insert newUser into registeredUser collection
            collRU.insertOne(newUser);
            System.out.println("Username " + un + " is registered!");
            success = true;

            // create new collection (table) for new user's post
            db.createCollection(un + "Post");

            // create followList for new user
            db.getCollection("followList").insertOne(
                    new Document("username", un).append("following", new ArrayList<String>()));
        }

        return success;
    }

    // checking if someone has already took the username in parameter
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

    // login to account, TODO decide to use User class or create new, or just use db data to set up text in GUI
    public boolean loginUser(String username, String password) {

        boolean isSuccess = false;

        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // ----- get document of matching u/n and p/w from registeredData -----

        // get SHA value of given password
        SHAEncryption sha = new SHAEncryption();
        String shaPW = sha.getSHA(password);

        // find document by filters (u/n and p/w)
        FindIterable<Document> docOne = collRU.find(
                and(
                    (Filters.eq("username", username)),
                    (Filters.eq("password", shaPW))
                ));
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents

        try {
            // cursor1 will only have 1 data if we found a match
            if (cursor1.hasNext()) {

                isSuccess = true; // successfully log in

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

        return isSuccess;
    }

    // create new post
    public void createNewPost(String un, String post) {

        // accessing posts table
        MongoCollection<Document> postColl = db.getCollection("postsRecord");

        // get current date
        Date now = new Date();

        // creating new user document based on the input from UI
        Document newPost = new Document("post", post)
                .append("date", now)
                .append("username", un);

        // insert newPost into registeredUser collection
        postColl.insertOne(newPost);
        System.out.println("New post from " + un + " is submitted!");
        System.out.println(newPost.toJson());
    }

    // get sorted post from this one user
    // can be used when we visiting other people's profile
    public void getUserPost(String un) {

        // accessing posts table
        MongoCollection<Document> postColl = db.getCollection("postsRecord");

        ArrayList<Document> userPost = new ArrayList<>(); // all posts from this user will be in here

        FindIterable<Document> postDocs = postColl
                .find(Filters.eq("username", un)) // get all documents from this user
                .sort(Sorts.descending("date")); // sorts by newest post first

        MongoCursor<Document> cursor = postDocs.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson()); // print username's document in String
                userPost.add(cursor.next()); // add every post Documents into allPosts
            }
        } finally {
            cursor.close();
        }

        // print
        for (Document post: userPost) {
            System.out.println(post.toJson());
        }
    }

    // get all posts from everyone we follow
    // use for homepage
    public void getEveryonePosts(String myUN) {

        // get who I'm following
        ArrayList<String> following = getFollowList(myUN);
        following.add(myUN); // "add" myself into follow list so I can see my own post in homepage
        System.out.println("Follow = " + following);

        // hold all posts from everyone I follow here
        ArrayList<Document> allPosts = new ArrayList<>();

        // accessing posts table
        MongoCollection<Document> postColl = db.getCollection("postsRecord");

        // get all posts in newest first order
        FindIterable<Document> everyPost = postColl.find().sort(Sorts.descending("date"));

        MongoCursor<Document> cursor = everyPost.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor.hasNext()) {
                Document currCursor = cursor.next(); // get current document from cursor
                String cursorUN = currCursor.get("username").toString(); // see username of that document
                //System.out.println("cursorUN = " + cursorUN);

                // if I follow user of current document
                if (following.contains(cursorUN))
                    allPosts.add(currCursor); // add that post into allPosts
            }
        } finally {
            cursor.close();
        }

        // print
        for (Document post: allPosts) {
            System.out.println(post.toJson());
        }
    }

    // get the list of who this user follow
    public ArrayList<String> getFollowList(String un) {
        // accessing follow list table (collection)
        MongoCollection<Document> followColl = db.getCollection("followList");

        // find user's document
        Document followDoc = (Document) followColl.find(Filters.eq("username", un)).first();
        //System.out.println(followDoc.toJson());

        // get username of everyone that this user is following
        ArrayList<String> following = (ArrayList<String>) followDoc.get("following");

        /*
        for (String followingWho: following) {
            System.out.println("\nFollowing: " + followingWho.toString());
            GetUser(followingWho.toString());
        }
        */

        return following;
    }

    // following other user
    public void followingOtherUser(String me, String other) {
        // accessing follow list table (collection)
        MongoCollection<Document> followColl = db.getCollection("followList");

        // get the ArrayList of who "me" is following
        ArrayList<String> following = getFollowList(me);

        // adding other user to my following list
        following.add(other);

        // updating database
        followColl.updateOne(
                eq("username", me),
                combine(set("following", following))
        );

        System.out.println(me + " is now following " + other);
    }

    // can be use to get friend's profile, TODO limit of what data we can get from this
    public void getUser(String un) {

        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // ----- get document of given username from registeredData -----

        System.out.println("\nPrint " + un + "\'s data\n--------------------\n");
        FindIterable<Document> docOne = collRU.find(Filters.eq("username", un)); // find document by filters
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor1.hasNext()) {
                System.out.println(cursor1.next().toJson()); // print username's document in String
            }
        } finally {
            cursor1.close();
        }
    }


    // ------------------------------------------------------------------ //
    // test + debug purposes only

    public static void main(String[] args) {
        DatabaseController dbc = new DatabaseController();
        //dbc.registerNewUser("admin", "admin1", "Admin", "FacebookLite", "Who am I?", "sudo poweruser", 585);
        //dbc.getUser("tom");
        //dbc.loginUser("admin", "admin1");
        //System.out.println(dbc.getFollowList("admin"));
        //dbc.followingOtherUser("test", "hilmi");

        //dbc.createNewPost("tom", "This don\'t even have music supported, unlike MySpace. Lame!");
        //dbc.getUserPost("tom");
        //dbc.getEveryonePosts("admin");
    }
}
