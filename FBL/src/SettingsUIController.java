import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class SettingsUIController {

    GUIManager gui;
    FBLManager fbl;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;
    }

    @FXML
    TextField UpdateAgeField;

    @FXML
    CheckBox HideFriendsCBox;

    @FXML
    CheckBox HideAgeCBox;

    @FXML
    CheckBox HidePostsCBox;

    public void addTextfieldConstraints()
    {

    }

    @FXML
    public void exit() throws Exception
    {
        gui.loadHomePage();
    }

    @FXML
    public void save() throws Exception
    {
        //save the settings changes code here

        //return to home page
        exit();
    }
}
