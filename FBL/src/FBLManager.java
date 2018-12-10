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
        if(dbc.loginUser(user, pass)) { // find matching credential in db
            userName = user;
            //load in user settings:
            fName = dbc.getUserFirstName(userName);
            lName = dbc.getUserLastName(userName);
            age = dbc.getUserAge(userName);
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
            //TODO change this in database too
        }
    }

    public void setAge(int age){
        if(!(this.age == age)) {
            this.age = age;
            //TODO change this in database too
        }
    }

    public void setHidePosts(boolean hide){
        if(!(hidePosts == hide)) {
            hidePosts = hide;
            //TODO change this in database too
        }
    }

    public void setHideFriends(boolean hide){
        if(!(hideFriends == hide)) {
            hideFriends = hide;
            //TODO change this in database too
        }
    }

    public void setHideAge(boolean hide){
        if(!(hideAge == hide)) {
            hideAge = hide;
            //TODO change this in database too
        }
    }

    public void setHideStatus(boolean hide){
        if(!hideStatus == hide) {
            hideStatus = hide;
            //TODO change this in database too
        }
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
