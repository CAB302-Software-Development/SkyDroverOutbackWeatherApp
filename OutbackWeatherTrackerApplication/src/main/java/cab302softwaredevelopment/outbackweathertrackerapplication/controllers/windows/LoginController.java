package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController {

    public static final int HEIGHT = 400;
    public static final int WIDTH = 700;

    // Email regex pattern
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    // Password regex pattern (at least 8 chars, 1 digit, 1 upper, 1 lower, 1 special)
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";


    // Login elements
    @FXML
    private AnchorPane loginPane;
    @FXML
    private TextField emailTextFieldLogin;

    @FXML
    private PasswordField passwordTextFieldLogin;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPasswordLink, signUpLink, guestLinkLogin;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Text loginTitle;


    // Sign up elements
    @FXML
    private AnchorPane signupPane;
    @FXML
    private TextField emailTextFieldSignup;

    @FXML
    private PasswordField passwordTextFieldSignup;

    @FXML
    private Button signupButton;

    @FXML
    private Hyperlink loginLink, guestLinkSignup;

    @FXML
    private Label signupMessageLabel;

    @FXML
    private Text signupTitle;



    private boolean isLogin = true;

    /**
     * Swaps view to display sign up screen
     */
    @FXML
    private void switchToSignup() {
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        signupPane.setVisible(true);
        signupPane.setManaged(true);
        isLogin = false;
    }

    /**
     * Swaps view to display login screen
     */
    @FXML
    private void switchToLogin() {
        signupPane.setVisible(false);
        signupPane.setManaged(false);
        loginPane.setVisible(true);
        loginPane.setManaged(true);
        isLogin = true;
    }

    @FXML
    private void handleButtonPress(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String email = emailTextFieldLogin.getText();
            String password = passwordTextFieldLogin.getText();
            if (handleLogin(email, password)) {
                continueToApplication();
            }
        } else if (e.getSource() == signupButton) {
            String email = emailTextFieldSignup.getText();
            String password = passwordTextFieldSignup.getText();
            if (handleSignUp(email, password)) {
                continueToApplication();
            }
        } else if (e.getSource() == guestLinkLogin || e.getSource() == guestLinkSignup) {
            LoginState.logout();
            continueToApplication();
        }
    }

    public boolean handleLogin(String email, String password) {
        if (isInvalidEmail(email)) {
            showErrorMessage("Invalid email format.");
            return false;
        }

        if (isInvalidPassword(password)) {
            showErrorMessage("Password must be at least 8 characters, " +
                    "include one uppercase letter, one digit, and one special character.");
            return false;
        }

        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.getByEmail(email);
        if (account == null) {
            showErrorMessage("No account exists with specified email.");
            return false;
        }

        if (!account.verifyPassword(password)) {
            showErrorMessage("Incorrect password");
            return false;
        }

        LoginState.login(account);
        return true;
    }

    public boolean handleSignUp(String email, String password) {
        if (isInvalidEmail(email)) {
            showErrorMessage("Invalid email format.");
            return false;
        }

        if (isInvalidPassword(password)) {
            showErrorMessage("Password must be at least 8 characters, include one uppercase letter, one digit, and one special character.");
            return false;
        }

        AccountDAO accountDAO = new AccountDAO();

        if (accountDAO.getByEmail(email) != null) {
            showErrorMessage("User already exists with this email.");
            return false;
        }

        Account account = Account.builder()
                .email(email)
                .password(password)
                .build();

        accountDAO.insert(account);

        LoginState.login(account);

        return true;
    }

    /**
     * Checks if the given email is invalid based on a predefined email pattern.
     *
     * @param email The email string to validate.
     * @return true if the email does not match the pattern, false otherwise.
     */
    private boolean isInvalidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return !pattern.matcher(email).matches();
    }

    /**
     * Checks if the given password is invalid based on a predefined password pattern.
     *
     * @param password The password string to validate.
     * @return true if the password does not match the pattern, false otherwise.
     */
    private boolean isInvalidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return !pattern.matcher(password).matches();
    }

    /**
     * Shows an error message to the corresponding login or sign up screen
     * @param message Message to be displayed
     */
    private void showErrorMessage(String message) {
        if (isLogin) {
            loginMessageLabel.setText(message);
            loginMessageLabel.setVisible(true);
            signupMessageLabel.setVisible(false);
        } else {
            signupMessageLabel.setText(message);
            signupMessageLabel.setVisible(true);
            loginMessageLabel.setVisible(false);
        }
    }

    private void clearInputFields() {
        emailTextFieldSignup.setText("");
        emailTextFieldLogin.setText("");
        passwordTextFieldLogin.setText("");
        passwordTextFieldSignup.setText("");
    }

    /**
     * Closes the login / sign up window and initializes the main application view
     */
    private void continueToApplication() {
        try {
            ((Stage) loginPane.getScene().getWindow()).close();
            ApplicationEntry.openMainWindow();
        } catch (IOException e) {
            showErrorMessage("Error starting application");
            e.printStackTrace();
        }
    }
}