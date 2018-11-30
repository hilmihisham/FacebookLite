import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    GUIManager gui;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gui = new GUIManager(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
