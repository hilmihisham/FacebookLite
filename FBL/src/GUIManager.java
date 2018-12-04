import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager {

    private FBLManager fbl;
    private Scene scene;

    public GUIManager(Stage primaryStage) throws Exception{
        fbl = new FBLManager();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController login = loader.getController();
        login.initialize(this,fbl);
        primaryStage.setTitle("FacebookLite");
        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void loadRegisterPage() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root = loader.load();
        RegisterController register = loader.getController();
        register.initialize(this,fbl);
        scene.setRoot(root);
    }

    public void loadLoginPage() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController login = loader.getController();
        login.initialize(this,fbl);
        scene.setRoot(root);
    }

    public void loadProfilePage() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        ProfileUIController profile = loader.getController();
        profile.initialize(this,fbl);
        scene.setRoot(root);
    }
}
