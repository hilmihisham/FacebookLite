import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * will be used for both ForgotPassword.fxml and ForgotPasswordReset.fxml
 */
public class ForgotPasswordController {

    GUIManager gui;
    FBLManager fbl;

    @FXML
    TextField usernameInput;
    @FXML
    Label notExistNotice;
    @FXML
    Label questionLabel;
    @FXML
    TextField answerField;
    @FXML
    Label matchingErrorNotice;
    @FXML
    PasswordField newPasswordField;

    public void initialize(GUIManager gui, FBLManager fbl) {
        this.gui = gui;
        this.fbl = fbl;

        // hide error notice at launch
        if (notExistNotice != null)
            notExistNotice.setVisible(false);

        // hide error notice at first
        if (matchingErrorNotice != null)
            matchingErrorNotice.setVisible(false);

        // get question string from db and setText to questionLabel
        if (questionLabel != null)
            questionLabel.setText("test question");
    }

    public void back() throws Exception {
        gui.loadLoginPage();
    }

    // clicking retrieve button
    public void getUsernameFromDB() throws Exception {
        String username = usernameInput.getText();

        if (!username.equals("")) {
            System.out.println("Input username = " + username);
            // find username in db

            //if username exists, load reset page
            gui.loadResetPasswordPage();
            // else, show error notice
            //notExistNotice.setVisible(true);
        }
    }

    // clicking reset password button
    public void resetPassword() throws Exception {

        String answer = answerField.getText();
        String newPW = newPasswordField.getText();

        // if both TextField not empty
        if (!answer.equals("") && !newPW.equals("")) {
            // check answer with database
            //boolean isCorrect = false;

            //if answer not match
            //if (!isCorrect) {
            //matchingErrorNotice.setVisible(true);
            //return;
            //}
            //else {
                // changed password in database

                // have popup window of password changed confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FacebookLite");
                alert.setHeaderText("Resetting password");
                alert.setContentText("Reset complete. Your password have been successfully changed.");
                alert.showAndWait();

                // call back() to jump to login page
                back();
            //}

        }

    }
}
