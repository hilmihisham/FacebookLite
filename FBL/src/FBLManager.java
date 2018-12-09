//Class for managing all backend data and the database

import org.bson.Document;
import java.util.ArrayList;

public class FBLManager {

    private DatabaseController dbc;
    private String userName;

    public FBLManager() {
        dbc = new DatabaseController();
        userName = "";
    }

    public boolean login(String user, String pass) {
        //Attempt to login with given username and password
        if(dbc.loginUser(user, pass)) { // find matching credential in db
            userName = user;
            return true;
        }
        else
            return false;
    }

    public void logout() {
        //Logout of current user profile
        userName = "";
    }

    public boolean register(String user, String pass, String fName, String lName,String sQuestion, String sAnswer, int age) {
        //Attempt to register new user with given credentials
        return dbc.registerNewUser(user, pass, fName, lName, sQuestion, sAnswer, age);
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
