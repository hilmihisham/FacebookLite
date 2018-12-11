import com.mongodb.operation.UpdateUserOperation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SettingsUIController {

    GUIManager gui;
    FBLManager fbl;

    @FXML
    TextField UpdateAgeField;
    @FXML
    Label ageError;
    @FXML
    TextField UpdateStatusField;
    @FXML
    CheckBox HideFriendsCBox;
    @FXML
    CheckBox HideAgeCBox;
    @FXML
    CheckBox HidePostsCBox;
    @FXML
    CheckBox HideStatusCBox;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;
        addTextFieldConstraints();
        loadSettings();
    }

    private void addTextFieldConstraints() {
        // force the age field to be numeric only
        UpdateAgeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[0-9]*"))
                    UpdateAgeField.setText(oldValue);
            }
        });
    }

    private void loadSettings(){
        HideFriendsCBox.setSelected(fbl.getHideFriends());
        HideAgeCBox.setSelected(fbl.getHideAge());
        HidePostsCBox.setSelected(fbl.getHidePosts());
        HideStatusCBox.setSelected(fbl.getHideStatus());
        UpdateAgeField.setText("" + fbl.getAge());
        UpdateStatusField.setText(fbl.getStatus());
    }

    @FXML
    public void exit() throws Exception {
        gui.loadHomePage();
    }

    @FXML
    public void save() throws Exception{
        //save the settings changes code here

        //Age error checks:
        int inputAge;
        try{ inputAge = Integer.parseInt(UpdateAgeField.getText()); }
        catch(Exception e){ inputAge=0; }
        if(inputAge<18 || inputAge>999) {
            ageError.setText("Invalid Age.");
            return;
        }

        //Write out settings:
        fbl.setAge(inputAge);
        fbl.setStatus(UpdateStatusField.getText());
        fbl.setHideSettings(HideFriendsCBox.isSelected(),HidePostsCBox.isSelected(),
                HideAgeCBox.isSelected(),HideStatusCBox.isSelected());

        exit(); //return to home page
    }
}
