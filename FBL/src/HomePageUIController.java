import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class HomePageUIController {

    GUIManager gui;
    FBLManager fbl;

    @FXML
    Label name;
    @FXML
    Label age;
    @FXML
    Label status;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;
        loadUserData();
    }

    private void loadUserData(){
        name.setText(fbl.getFirstName() + " " + fbl.getLastName());
        name.setAlignment(Pos.CENTER);

        if(fbl.getHideAge() == false) {
            age.setText("Age: " + fbl.getAge());
            age.setAlignment(Pos.CENTER);
        }
        else
            age.setText("");

        if(fbl.getHideStatus() == false) {
            status.setText(fbl.getStatus());
            status.setAlignment(Pos.CENTER);
        }
        else
            status.setText("");
    }

    @FXML
    public void settings() throws Exception {
        gui.loadSettingUIPage();
    }

    public void logout() throws Exception{
        fbl.logout();
        gui.loadLoginPage();
    }
}
