import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfileUIController {
    GUIManager gui;
    FBLManager fbl;

    @FXML
    TextField ProfileUIUserName;

    public void initialize(GUIManager gui, FBLManager fbl){
        this.gui = gui;
        this.fbl = fbl;

        ProfileUIUserName.setText(fbl.getFirstName());
    }

    public void back() throws Exception {
        gui.loadHomePage();
    }

}
