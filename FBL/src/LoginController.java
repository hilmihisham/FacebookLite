
public class LoginController {

    GUIManager gui;
    FBLManager fbl;

    public void initialize(GUIManager gui, FBLManager fbl){
        this.gui = gui;
        this.fbl = fbl;
    }

    public void forgotpassword() {
        System.out.println("forgot password lol rip XD");
    }

    public void register() throws Exception{
        gui.loadRegisterPage();
    }
}
