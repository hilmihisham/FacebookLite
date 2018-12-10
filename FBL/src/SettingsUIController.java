import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

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
// force the field to be numeric only
        UpdateAgeField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(!UpdateAgeField.getText().matches("\\d*")) {
                    UpdateAgeField.setText(UpdateAgeField.getText().replaceAll("[^\\d]", ""));
                }
            }
        });
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
