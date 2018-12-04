import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    GUIManager gui;
    FBLManager fbl;

    @FXML
    TextField userName;
    @FXML
    PasswordField password;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;
    }

    public void forgotpassword() throws Exception{
        System.out.println("forgot password lol rip XD");
        gui.loadForgotPasswordPage();
    }

    public void register() throws Exception {
        gui.loadRegisterPage();
    }

    public void login() {
        //need to make sure both fields are filled and then validate with database
        //should display warning somewhere if field not filled or login doesn't work

        //userName.getText();
        //password.getText();
    }
}