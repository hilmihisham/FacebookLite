//Class for managing all backend data and the database

import org.bson.Document;
import java.util.ArrayList;

public class FBLManager {

    private DatabaseController dbc;
    private String userName;
    private String fName;
    private String lName;
    private String status;
    private int age;
    private boolean hideFriends;
    private boolean hidePosts;
    private boolean hideAge;
    private boolean hideStatus;

    public FBLManager() {
        dbc = new DatabaseController();
        userName = fName = lName = status = "";
        age = 18;
        hideFriends = hidePosts = hideAge = hideStatus = false;
    }

    public boolean login(String user, String pass) {
        //Attempt to login with given username and password
        UserDoc doc = new UserDoc();
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
            return true;
        }
        else
            return false;
    }

    public void logout() {
        //Logout of current user profile
        userName = fName = lName = status = "";
        age = 18;
        hideFriends = hidePosts = hideAge = hideStatus = false;
    }

    public boolean register(String user, String pass, String fName, String lName,String sQuestion, String sAnswer, int age) {
        //Attempt to register new user with given credentials
        return dbc.registerNewUser(user, pass, fName, lName, sQuestion, sAnswer, age);
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

    public void getPosts(ArrayList<Document> userPost) {
        //Get all posts made by the logged in user.
        if(userName.equals(""))
            return;
        dbc.getUserPost(userName, userPost);
    }
}
