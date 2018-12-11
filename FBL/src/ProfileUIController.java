import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfileUIController {
    GUIManager gui;
    FBLManager fbl;

    public void initialize(GUIManager gui, FBLManager fbl){
        this.gui = gui;
        this.fbl = fbl;
    }

    @FXML
    Label ProfileNameLabel;

    @FXML
    public void back() throws Exception {
        gui.loadHomePage();
    }

}
