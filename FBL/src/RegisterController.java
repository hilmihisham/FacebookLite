
public class RegisterController {

    GUIManager gui;
    FBLManager fbl;

    public void initialize(GUIManager gui, FBLManager fbl){
        this.gui = gui;
        this.fbl = fbl;
    }

    public void back() throws Exception{
        gui.loadLoginPage();
    }
}
