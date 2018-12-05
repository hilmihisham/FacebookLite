import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    GUIManager gui;
    FBLManager fbl;

    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField userName;
    @FXML
    PasswordField password;
    @FXML
    TextField age;

    public void initialize(GUIManager gui, FBLManager fbl){
        this.gui = gui;
        this.fbl = fbl;
    }

    public void back() throws Exception {
        gui.loadLoginPage();
    }

    public void register() {
        //need to check that all fields aren't empty and are properly formatted
        //if not, there needs to be a warning message on the UI

        //firstName.getText();
        //lastName.getText();
        //username.getText();
        //password.getText()
        //age.getText();
    }
}
