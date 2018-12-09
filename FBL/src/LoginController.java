import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    GUIManager gui;
    FBLManager fbl;

    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Label errors;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;
    }

    public void forgotpassword() throws Exception {
        //Go to the forgot password page
        gui.loadForgotPasswordPage();
    }

    public void register() throws Exception {
        //Go to the register page
        gui.loadRegisterPage();
    }

    public void login() throws Exception {
        //Attempt to login with entered credentials
        String user = username.getText();
        String pass = password.getText();

        if(user.equals("")) {
            errors.setText("Please enter a username.");
            return;
        }
        if(pass.equals("")) {
            errors.setText("Please enter a password.");
            return;
        }

        if (fbl.login(user,pass)) {
            //login success
            gui.loadHomePage();
        }
        else {
            //failed to login, display error on screen
            errors.setText("Invalid username or password.");
        }
    }
}