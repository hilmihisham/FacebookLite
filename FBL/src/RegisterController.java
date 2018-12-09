import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    GUIManager gui;
    FBLManager fbl;

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
    @FXML
    Label errors;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;
        addListeners();
    }

    private void addListeners(){
        age.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[0-9]*"))
                    age.setText(oldValue);
            }
        });

//        age.focusedProperty().addListener(((observable, oldValue, newValue) -> {
//            if (!newValue) { // if age TextField focus lost
//                ageInputDone();
//            }
//        }));
    }

//    public void ageInputDone() {
//        int ageInput;
//        try {
//            ageInput = Integer.parseInt(age.getText());
//        } catch (NumberFormatException e) {
//            // show dialog of error input
//            age.getStyleClass().add("errorInput");
//            age.clear();
//            age.setPromptText("Use numbers only");
//        }
//    }

    public void back() throws Exception {
        gui.loadLoginPage();
    }

    public void register() throws Exception{
        //Try to register new user with currently entered fields
        String fName = firstName.getText();
        String lName = lastName.getText();
        String user = username.getText();
        String pass = password.getText();
        String sQuestion = secureQuestion.getText();
        String sAnswer = secureAnswer.getText();
        int inputAge;
        try{ inputAge = Integer.parseInt(age.getText()); }
        catch(Exception e){ inputAge=0; }

        if(fName.equals("")) {
            errors.setText("Please enter your first name.");
            return;
        }
        if(lName.equals("")) {
            errors.setText("Please enter your last name.");
            return;
        }
        if(user.equals("")) {
            errors.setText("Please enter a username.");
            return;
        }
        if(pass.equals("")) {
            errors.setText("Please enter a password.");
            return;
        }
        if(inputAge<18 || inputAge>999) {
            errors.setText("Invalid Age.");
            return;
        }
        if(sQuestion.equals("")){
            errors.setText("Please enter a security question.");
            return;
        }
        if(sAnswer.equals("")){
            errors.setText("Please enter an answer to your security question.");
            return;
        }

        boolean result = fbl.register(username.getText(),password.getText(),firstName.getText(),lastName.getText(),
                secureQuestion.getText(),secureAnswer.getText().toLowerCase(),inputAge);

        if(result){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration");
            alert.setHeaderText(null);
            alert.setContentText("Hi " + fName + "! Successfully registered a new account with the username: " + user);
            alert.showAndWait();
            gui.loadLoginPage();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Username");
            alert.setHeaderText("Username already exists!");
            alert.setContentText("Please try a different username.");
            alert.showAndWait();
        }
    }

}
