import javafx.fxml.FXML;

public class HomePageUIController {

    GUIManager gui;
    FBLManager fbl;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;
    }

    @FXML
    public void settings() throws Exception
    {
        gui.loadSettingUIPage();
    }
}
