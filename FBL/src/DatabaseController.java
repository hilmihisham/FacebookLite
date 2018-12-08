import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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

    // login to account, TODO decide to use User class or create new
    public void LoginUser(String username, String password) {

        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // ----- get document of matching u/n and p/w from registeredData -----
        String colUsername = "username", colPassword = "password"; // set key and value to look for in document

        // find document by filters (u/n and p/w)
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

    // submit a post
    public void submitPost(String un, String post) {

        // accessing user's post table (collection)
        MongoCollection<Document> userPostsColl = db.getCollection(un + "Posts");

        // get current date
        Date now = new Date();

        // get total posts for ID-ing new one
        long newPostCount = userPostsColl.countDocuments() + 1;
        String newPostID = un + newPostCount;
        //System.out.println("newPostID = " + newPostID);

        // creating new user document based on the input from UI
        Document newPost = new Document("post", post)
                .append("date", now)
                .append("postID", newPostID);

        // insert newUser into registeredUser collection
        userPostsColl.insertOne(newPost);
        System.out.println("Post #" + newPostCount + " from " + un + " is submitted!");
        System.out.println(newPost.toJson());
    }

    // get post collection of user
    public MongoCollection<Document> getPostsFrom(String un) {
        // accessing user's post table (collection)
        MongoCollection<Document> userPostsColl = db.getCollection(un + "Posts");

        return userPostsColl;
    }

    // get posts from everyone this user follow
    public void getFollowingPosts(String un) {
        // get array of username that this user follow
        ArrayList<String> following = getFollowList(un);

        // this is where we keep all the posts
        ArrayList<Document> allPosts = new ArrayList<>();

        // get posts from everyone that this user follow
        for (String followingWho : following) {
            MongoCollection<Document> userPosts = getPostsFrom(followingWho);

            FindIterable<Document> postDocs = userPosts.find(); // get all documents from this user
            MongoCursor<Document> cursor = postDocs.iterator(); // set up cursor to iterate rows of documents
            try {
                while (cursor.hasNext()) {
                    //System.out.println(cursor.next().toJson()); // print username's document in String
                    allPosts.add(cursor.next()); // add every post Documents into allPosts
                }
            } finally {
                cursor.close();
            }
        }

        // TODO sort allPosts by "date" before showing it all

        for (Document docs : allPosts) {
            System.out.println(docs.toJson()); // print out allPosts
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
        //if (we follow no one) do something (?)
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
                //(ArrayList<String>) followColl.find(eq("username", me)).first().get("following");

        // adding other user to my following list
        following.add(other);

        // updating in database
        followColl.updateOne(
                eq("username", me),
                combine(set("following", following))
        );

        System.out.println(me + " is now following " + other);
    }

    // can be use to get friend's profile, TODO limit of what data we can get from this
    public void GetUser(String un) {

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


    // ------------------------------------------------------------------ //
    // test + debug purposes only

    public static void main(String[] args) {
        DatabaseController dbc = new DatabaseController();
        //dbc.RegisterNewUser();
        //dbc.RegisterNewUser("test", "test", "Firstname", "Lastname", "test", "error", 65);
        //dbc.GetUser("tom");
        //dbc.LoginUser("admin", "admin1");
        //dbc.submitPost("tom", "it\'s 2126hrs now");
        //System.out.println(dbc.getFollowList("admin"));
        //dbc.getFollowingPosts("hilmi");
        //dbc.followingOtherUser("test", "hilmi");
    }
}
