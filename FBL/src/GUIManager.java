//Class for managing and changing GUI scenes

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager {

    private FBLManager fbl;
    private Scene scene;

    public GUIManager(Stage primaryStage) throws Exception {
        fbl = new FBLManager();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController login = loader.getController();
        login.initialize(this, fbl);
        primaryStage.setTitle("FacebookLite");
        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // no to resize!
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void loadRegisterPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root = loader.load();
        RegisterController register = loader.getController();
        register.initialize(this, fbl);
        scene.setRoot(root);
    }

    public void loadLoginPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController login = loader.getController();
        login.initialize(this, fbl);
        scene.setRoot(root);
    }

    public void loadForgotPasswordPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgotPassword.fxml"));
        Parent root = loader.load();
        ForgotPasswordController forgot = loader.getController();
        forgot.initialize(this, fbl);
        scene.setRoot(root);
    }

    public void loadHomePage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePageUI.fxml"));
        Parent root = loader.load();
        HomePageUIController home = loader.getController();
        home.initialize(this, fbl);
        scene.setRoot(root);
    }

    public void loadProfileUIPage() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileUI.fxml"));
        Parent root = loader.load();
        ProfileUIController profile = loader.getController();
        profile.initialize(this,fbl);
        scene.setRoot(root);
    }

    public void loadSettingUIPage() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsUI.fxml"));
        Parent root = loader.load();
        SettingsUIController settings = loader.getController();
        settings.initialize(this, fbl);
        scene.setRoot(root);
    }
}
