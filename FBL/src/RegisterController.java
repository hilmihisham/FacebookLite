import com.mongodb.MongoException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    GUIManager gui;
    FBLManager fbl;
    DatabaseController dbc;

    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    TextField age;
    @FXML
    TextField secureQuestion;
    @FXML
    TextField secureAnswer;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;

        // add focus listener to age TextField
        age.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) { // if age TextField focus lost
                ageInputDone();
            }
        }));
    }

    private void connectToDB() throws MongoException {
        dbc = new DatabaseController();
    }

    public void back() throws Exception {
        gui.loadLoginPage();
    }

    public void ageInputDone() {

        int ageInput;

        try {
            ageInput = Integer.parseInt(age.getText());
        } catch (NumberFormatException e) {
            // show dialog of error input
            age.getStyleClass().add("errorInput");
            age.clear();
            age.setPromptText("Use numbers only");
        }
    }

    public void register() {
        //need to check that all fields aren't empty and are properly formatted
        //if not, there needs to be a warning message on the UI

        boolean registerSuccess = false;
        boolean inputComplete = true;
        String[] input = new String[6];

        // populate input[] with user inputs
        input[0] = username.getText();
        input[1] = password.getText();
        input[2] = firstName.getText();
        input[3] = lastName.getText();
        input[4] = secureQuestion.getText();
        input[5] = secureAnswer.getText().toLowerCase();

        // checking if all TextField is filled
        for (String data: input) {
            if (data.equals("")) {
                //System.out.println("data = " + data);
                inputComplete = false;
            }
        }

        // all TextField filled (including age field)
        if (inputComplete && (!age.getText().equals(""))) {
            int inputAge = Integer.parseInt(age.getText());
            System.out.println("all green to register!");

            // opening connection to database
            try {
                connectToDB();
            } catch (MongoException me) {
                System.out.println("Cannot connect to database");
                //dbc.closeConnection();
                return;
            }

            // registering user to database
            if (dbc != null) {
                registerSuccess = dbc.registerNewUser
                        (input[0], input[1], input[2], input[3], input[4], input[5], inputAge);
                dbc.closeConnection();
            }

            // call popup window to confirm registration complete (?)
            // use registerSuccess = true/false

            // get back to login page
            if (registerSuccess) {
                try {
                    back();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
