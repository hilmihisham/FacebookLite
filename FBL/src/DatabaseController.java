//Class for managing all database operations

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

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
                    .append("secureA", sa.toLowerCase())
                    .append("status", "")
                    .append("hidefriends",false)
                    .append("hideposts",false)
                    .append("hideage",false)
                    .append("hidestatus",false);

            // insert newUser into registeredUser collection
            collRU.insertOne(newUser);
            System.out.println("Username " + un + " is registered!");
            success = true;
            
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

    // login to account
    public boolean loginUser(String username, String password, UserDoc doc) {

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
                doc.doc = new Document(userDoc);

                System.out.println("\n\nPrint " + username + "\'s data\n--------------------\n");
                System.out.println(userDoc.toJson()); // print username's document in JSON

                String fn = userDoc.getString("firstName");
                String ln = userDoc.getString("lastName");
                int age = userDoc.getInteger("age");

                //System.out.println(userDoc.get("lastName")); // getting data from user document using key
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
        long now = new Date().getTime();

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
    public void getOneUserPost(String un, UserPosts up) {

        // see if that user have made their posts private or not
        boolean isPrivate = false;

        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        FindIterable<Document> docOne = collRU.find(Filters.eq("username", un)); // find document by filters
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor1.hasNext()) {
                isPrivate = cursor1.next().getBoolean("hideposts");
            }
        } finally {
            cursor1.close();
        }

        if (!isPrivate) {
            // accessing posts table
            MongoCollection<Document> postColl = db.getCollection("postsRecord");

            //ArrayList<Document> userPost = new ArrayList<>(); // all posts from this user will be in here
            if (!up.postDocs.isEmpty())
                up.postDocs.clear(); // clearing out the arrayList first

            FindIterable<Document> postDocs = postColl
                    .find(Filters.eq("username", un)) // get all documents from this user
                    .sort(Sorts.descending("date")); // sorts by newest post first

            MongoCursor<Document> cursor = postDocs.iterator(); // set up cursor to iterate rows of documents
            try {
                while (cursor.hasNext()) {
                    //System.out.println(cursor.next().toJson()); // print username's document in String
                    up.postDocs.add(cursor.next()); // add every post Documents into allPosts
                }
            } finally {
                cursor.close();
            }

            // print
            for (Document post : up.postDocs) {
                System.out.println(post.toJson());
                System.out.println("^^^ post date: " + new Date(post.getLong("date")).toString() + "\n");
            }
        }
        else {
            System.out.println(un + " has made their posts private");
        }
    }

    // get all posts from everyone we follow
    // use for homepage
    public void getEveryonePosts(String myUN, UserPosts up, ArrayList<String> following) {

        // get who I'm following
        //ArrayList<String> following = getFollowList(myUN);
        //following.add(myUN); // "add" myself into follow list so I can see my own post in homepage
        //System.out.println("Follow = " + following);

        // hold all posts from everyone I follow here
        //ArrayList<Document> allPosts = new ArrayList<>();
        if (!up.postDocs.isEmpty()) up.postDocs.clear();

        // accessing posts table
        MongoCollection<Document> postColl = db.getCollection("postsRecord");

        // get all posts in newest first order
        FindIterable<Document> everyPost = postColl.find().sort(Sorts.descending("date"));

        MongoCursor<Document> cursor = everyPost.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor.hasNext()) {
                Document currCursor = cursor.next(); // get current document from cursor
                String cursorUN = currCursor.getString("username"); // see username of that document
                //System.out.println("cursorUN = " + cursorUN);

                // if post by me or I follow user of current document
                if (cursorUN.equals(myUN) || following.contains(cursorUN))
                    up.postDocs.add(currCursor); // add that post into postDocs
            }
        } finally {
            cursor.close();
        }

        // print
        for (Document post: up.postDocs) {
            System.out.println(post.toJson());
        }
    }

    // get the list of who this user follow, send it back to UserFriends class
    public void getFollowList(String un, UserFriends uf) {
        // accessing follow list table (collection)
        MongoCollection<Document> followColl = db.getCollection("followList");

        // find user's document
        Document followDoc = followColl.find(Filters.eq("username", un)).first();
        //System.out.println(followDoc.toJson());

        // get username of everyone that this user is following into UserFriends class
        uf.friendsList.addAll((ArrayList<String>) followDoc.get("following"));

        // debugging
        /*
        for (String followingWho: uf.friendsList) {
            System.out.println("Following: " + followingWho.toString());
            //GetUser(followingWho.toString());
        }
        */
    }

    public void getSuggestedFriends(String un, UserFriends uf) {
        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        FindIterable<Document> allDocs = collRU.find(); // get all documents
        MongoCursor<Document> cursor = allDocs.iterator(); // set up cursor to iterate rows of documents
        try {
            while (cursor.hasNext()) {
                String nextUsername = cursor.next().getString("username");
                if (!nextUsername.equals(un) && !uf.friendsList.contains(nextUsername)) {
                    uf.suggestion.add(nextUsername);
                }
            }
        } finally {
            cursor.close();
        }
    }

    // following other user
    public void followingOtherUser(String me, String other, UserFriends uf) {
        // accessing follow list table (collection)
        MongoCollection<Document> followColl = db.getCollection("followList");

        // get the ArrayList of who "me" is following
        //ArrayList<String> following = getFollowList(me);

        // adding other user to my following list
        if (!uf.friendsList.isEmpty()) uf.friendsList.add(other);

        // updating database
        followColl.findOneAndUpdate(
                eq("username", me),
                Updates.set("following", uf.friendsList)
        );

        System.out.println(me + " is now following " + other);
    }

    public void setStatus(String un, String status){
        //update status in database for user
        MongoCollection<Document> collRU = db.getCollection("registeredUser");
        // updating user database
        collRU.findOneAndUpdate(
                eq("username", un),
                Updates.set("status", status)
        );
    }

    public void setAge(String un, int age){
        //update age in database for user
        MongoCollection<Document> collRU = db.getCollection("registeredUser");
        // updating user database
        collRU.findOneAndUpdate(
                eq("username", un),
                Updates.set("age", age)
        );
    }

    public void setHideSettings(String un, boolean friends, boolean posts, boolean age, boolean status){
        //update age in database for user
        MongoCollection<Document> collRU = db.getCollection("registeredUser");
        // updating user database
        collRU.findOneAndUpdate(
                eq("username", un),
                and(
                        Updates.set("hidefriends", friends),
                        Updates.set("hideposts",posts),
                        Updates.set("hideage",age),
                        Updates.set("hidestatus",status)
                )
        );
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

    public boolean getDataToResetPassword(String un, ForgotPasswordController.UserData ud) {

        boolean userExist = false;

        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        FindIterable<Document> docOne = collRU.find(Filters.eq("username", un)); // find document by filters
        MongoCursor<Document> cursor1 = docOne.iterator(); // set up cursor to iterate rows of documents
        try {
            if (cursor1.hasNext()) { // if we found user
                userExist = true;

                // extract user data
                Document userData = cursor1.next();
                ud.question = userData.getString("secureQ");
                ud.answer = userData.getString("secureA");
                //System.out.println(cursor1.next().toJson()); // print username's document in String
            }
        } finally {
            cursor1.close();
        }

        return userExist;
    }

    public void changingPassword(String un, String pw, String secureQ, String secureA) {
        // accessing registeredUser table (collection)
        MongoCollection<Document> collRU = db.getCollection("registeredUser");

        // get SHA value of given password
        SHAEncryption sha = new SHAEncryption();
        String shaPW = sha.getSHA(pw);

        // updating user database
        collRU.findOneAndUpdate(
                and(
                        eq("username", un),
                        eq("secureQ", secureQ),
                        eq("secureA", secureA)
                ),
                Updates.set("password", shaPW)
        );

        // test (print out user after update)
        //getUser(un);
    }

    public void deletePost(String un, long postDate) {
        // accessing posts table
        MongoCollection<Document> postColl = db.getCollection("postsRecord");

        postColl.findOneAndDelete(
                and(
                        eq("username", un),
                        eq("date", postDate)
                )
        );
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

        //dbc.createNewPost("tom", "yes it is..");
        //dbc.getUserPost("tom");
        //dbc.getEveryonePosts("admin");

        //dbc.deletePost("test", 61505317734509L);
        //dbc.deletePost("test", new Date());
        //dbc.setAge("hilmi", 100);
        //dbc.getOneUserPost("hughman", new UserPosts());

        //dbc.getFollowList("admin");
    }
}
