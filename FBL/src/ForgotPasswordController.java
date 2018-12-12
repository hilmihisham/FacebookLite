import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * will be used for ForgotPassword.fxml
 * this scene will have two "page" (query username, and change password)
 * try not to be confused :P
 */
public class ForgotPasswordController {

    GUIManager gui;
    FBLManager fbl;
    UserData userData;
    String username;

    // pass variables as object if you wanna update data from given parameter, i.e. passed by reference
    public class UserData {
        public String question, answer;
    }

    // first page
    @FXML
    Label usernameLabel;
    @FXML
    TextField usernameInput;
    @FXML
    Label notExistNotice;
    @FXML
    Button retrieveButton;

    // second page
    @FXML
    Label questionLabelLeft;
    @FXML
    Label answerLabel;
    @FXML
    Label newPWLabel;
    @FXML
    Label questionLabelData;
    @FXML
    TextField answerField;
    @FXML
    Label answerErrorNotice;
    @FXML
    PasswordField newPasswordField;
    @FXML
    Label pwErrorNotice;
    @FXML
    Button resetPWButton;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;

        userData = new UserData();

        // load first "page" when we first into this scene
        setupFirstView(true);
    }

    // true if we're in first "page" (only username field visible)
    // false if we're in 2nd "page" (question and new password)
    private void setupFirstView(boolean isFirst) {
        usernameLabel.setVisible(isFirst);
        usernameInput.setVisible(isFirst);
        if (notExistNotice != null)
            notExistNotice.setVisible(false); // error always hidden first
        retrieveButton.setVisible(isFirst);

        questionLabelLeft.setVisible(!isFirst);
        answerLabel.setVisible(!isFirst);
        newPWLabel.setVisible(!isFirst);
        questionLabelData.setVisible(!isFirst);
        answerField.setVisible(!isFirst);
        if (answerErrorNotice != null)
            answerErrorNotice.setVisible(false); // error always hidden first
        newPasswordField.setVisible(!isFirst);
        if (pwErrorNotice != null)
            pwErrorNotice.setVisible(false); // error always hidden first
        resetPWButton.setVisible(!isFirst);
    }

    public void back() throws Exception {
        gui.loadLoginPage();
    }

    // clicking retrieve button
    public void getUsernameFromDB() throws Exception {
        username = usernameInput.getText();
        boolean userExist;

        if (!username.equals("")) {
            System.out.println("Input username = " + username);

            // find username in db
            userExist = fbl.getSecureQuestion(username, userData);

            //if username exists, load reset page
            if (userExist) {
                questionLabelData.setText(userData.question);
                setupFirstView(false);
                System.out.println("Q+A = \'" + userData.question + "\' & \'" + userData.answer + "\'");
            }
            // else, show error notice
            else
                notExistNotice.setVisible(true);
        }
    }

    // clicking reset password button
    public void resetPassword() throws Exception {

        String answer = answerField.getText().toLowerCase(); // we saved all answers in db in lower case
        String newPW = newPasswordField.getText();

        // if answer empty
        if (answer.equals("")) {
            answerErrorNotice.setText("Answer cannot be blank.");
            answerErrorNotice.setVisible(true);
        }
        //if answer not match
        else if (!answer.equals(userData.answer)) {
            answerErrorNotice.setText("Invalid answer.");
            answerErrorNotice.setVisible(true);
        }
        else {
            answerErrorNotice.setVisible(false);
        }

        // if new password empty
        if (newPW.equals("")) {
            pwErrorNotice.setText("Password cannot be blank.");
            pwErrorNotice.setVisible(true);
        } else {
            pwErrorNotice.setVisible(false);
        }

        // if both TextField not empty
        if (!answer.equals("") && !newPW.equals("")) {

            //if answer not match
            if (!answer.equals(userData.answer)) {
                answerErrorNotice.setText("Invalid answer.");
                answerErrorNotice.setVisible(true);
            }
            else {
                // changed password in database
                fbl.changePassword(username, newPW, userData.question, answer);

                // have popup window of password changed confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FacebookLite");
                alert.setHeaderText("Reset password");
                alert.setContentText("Your password has been successfully reset.");
                alert.showAndWait();

                // call back() to jump to login page
                back();
            }

        }

    }
}
