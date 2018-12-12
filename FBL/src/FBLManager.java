//Class for managing all backend data and the database

import org.bson.Document;
import java.util.ArrayList;

public class FBLManager {

    private DatabaseController dbc;
    private String userName;
    private String fName;
    private String lName;
    private String status;
    private String friend;
    private int age;
    private boolean hideFriends;
    private boolean hidePosts;
    private boolean hideAge;
    private boolean hideStatus;

    public boolean isFriend;

    public UserDoc doc;
    public UserPosts userPost;
    public UserFriends friendList;

    public FBLManager() {
        dbc = new DatabaseController();
        userName = fName = lName = status = friend = "";
        age = 18;
        hideFriends = hidePosts = hideAge = hideStatus = false;

        userPost = new UserPosts();
    }

    public boolean login(String user, String pass) {
        //Attempt to login with given username and password
        doc = new UserDoc();
        if(dbc.loginUser(user, pass, doc)) { // find matching credential in db
            userName = user;
            //load in user settings:
            fName = doc.getFirstName();
            lName = doc.getLastName();
            age = doc.getAge();
            status = doc.getStatus();
            hideFriends = doc.getHideFriends();
            hidePosts = doc.getHidePosts();
            hideAge = doc.getHideAge();
            hideStatus = doc.getHideStatus();
            //System.out.println(fName + " " + lName + " " + age + " Status: " + status);
            //System.out.println("" + hideStatus + hideAge + hidePosts + hideFriends);

            // initialize and get friends list during login
            friendList = new UserFriends();
            getFriendsList();
            getSuggestedFriends();

            return true;
        }
        else
            return false;
    }

    public void getFriendsList() {
        dbc.getFollowList(userName, friendList);
    }

    public void getSuggestedFriends() {
        dbc.getSuggestedFriends(userName, friendList);
    }

    public void logout() {
        //Logout of current user profile
        userName = fName = lName = status = friend = "";
        age = 18;
        hideFriends = hidePosts = hideAge = hideStatus = false;
    }

    public boolean register(String user, String pass, String fName, String lName,String sQuestion, String sAnswer, int age) {
        //Attempt to register new user with given credentials
        return dbc.registerNewUser(user, pass, fName, lName, sQuestion, sAnswer, age);
    }

    public UserDoc getUser(String un){
        return dbc.getUser(un);
    }

    public String getUsername() {
        return userName;
    }

    public String getFirstName(){
        return fName;
    }

    public String getLastName(){
        return lName;
    }

    public String getStatus(){
        return status;
    }

    public String getFriend(){
        return friend;
    }

    public boolean getIsFriend(){
        return isFriend;
    }

    public int getAge(){
        return age;
    }

    public boolean getHidePosts(){
        return hidePosts;
    }

    public boolean getHideAge(){
        return hideAge;
    }

    public boolean getHideFriends(){
        return hideFriends;
    }

    public boolean getHideStatus(){
        return hideStatus;
    }

    public void setFriend(String friend){
        this.friend = friend;
        dbc.getFollowList(userName, friendList);
        if (friendList.friendsList.contains(friend))
            isFriend = true;
        else
            isFriend = false;
    }

    public void setStatus(String status){
        if(!this.status.equals(status)) {
            this.status = status;
            dbc.setStatus(userName,status);
        }
    }

    public void setAge(int age){
        if(!(this.age == age)) {
            this.age = age;
            dbc.setAge(userName,age);
        }
    }

    public void setHideSettings(boolean friends, boolean posts, boolean age, boolean status){
        hideFriends = friends;
        hidePosts = posts;
        hideAge = age;
        hideStatus = status;
        dbc.setHideSettings(userName,friends,posts,age,status);
    }

    public boolean getSecureQuestion(String un, ForgotPasswordController.UserData ud) {
        return dbc.getDataToResetPassword(un, ud);
    }

    public void changePassword(String un, String newPW, String sq, String sa) {
        dbc.changingPassword(un, newPW, sq, sa);
    }

    public void addNewPost(String postText) {
        //Adds a new post for logged in user. After calling this, update the post list by calling "getPosts"
        if(userName.equals(""))
            return;
        dbc.createNewPost(userName, postText);
    }

    // get our following + our own posts
    public void getHomepagePosts() {
        if (userName.equals(""))
            return;
        dbc.getEveryonePosts(userName, userPost, friendList.friendsList);
    }

    // Get all posts from the other user we clicked on
    // if that user has made their posts private, it'll be empty ArrayList
    public void getPosts(String un) {
        if(userName.equals(""))
            return;
        dbc.getOneUserPost(un, userPost);
    }

    public void deletePost(String un, long date) {
        if (un.equals(userName)) {
            dbc.deletePost(un, date);
        }
    }

    public void getOtherFriendsList() {
        dbc.getFollowList(friend, friendList);
    }

    public void getMyFriends() {
        dbc.getFollowList(userName, friendList);
    }

    public void addFriend(String user){
        dbc.followingOtherUser(userName,user,friendList);
    }

    public void removeFriend(String user){
        dbc.removeFriend(userName,user,friendList);
    }
}
