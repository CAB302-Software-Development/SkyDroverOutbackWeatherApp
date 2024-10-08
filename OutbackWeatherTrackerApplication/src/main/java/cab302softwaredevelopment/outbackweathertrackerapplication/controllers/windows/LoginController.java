package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import javafx.fxml.FXML;
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
    public static final String TITLE = "Login";

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


    // Private front-end logic / helpers

    @FXML
    private void switchToSignup() {
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        signupPane.setVisible(true);
        signupPane.setManaged(true);
        isLogin = false;
    }

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

    private void showErrorMessage(String message) {
        if (loginMessageLabel != null && signupMessageLabel != null) {
            if (isLogin) {
                loginMessageLabel.setText(message);
                loginMessageLabel.setVisible(true);
                signupMessageLabel.setVisible(false);
            } else {
                signupMessageLabel.setText(message);
                signupMessageLabel.setVisible(true);
                loginMessageLabel.setVisible(false);
            }
        } else {
            Logger.printLog(message);
        }

    }

    private void continueToApplication() {
        try {
            ApplicationEntry.openMainWindow();
            ((Stage) loginPane.getScene().getWindow()).close();
        } catch (IOException e) {
            showErrorMessage("Error starting application");
            e.printStackTrace();
        }
    }


    // Public business logic

    /**
     *
     * @param email
     * @param password
     * @return
     */
    public boolean handleLogin(String email, String password) {
        try {
            if (!validateCredentials(email, password)) return false;

            Account account = (new AccountDAO.AccountQuery())
                    .whereEmail(email)
                    .getSingleResult();

            if (account == null) {
                showErrorMessage("No account exists with specified email.");
                return false;
            }

            if (!account.verifyPassword(password)) {
                showErrorMessage("Incorrect password");
                return false;
            }

            LoginState.login(account);
            return true;        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param email
     * @param password
     * @return
     */
    public boolean handleSignUp(String email, String password) {
        try {
            if (!validateCredentials(email, password)) return false;

            if ((new AccountDAO.AccountQuery()).whereEmail(email).getSingleResult() != null) {
                showErrorMessage("User already exists with this email.");
                return false;
            }

            AccountDAO accountDAO = new AccountDAO();

            Account newAccount = Account.builder()
                    .email(email)
                    .password(password)
                    .build();

            accountDAO.insert(newAccount);

            Account createdAccount = (new AccountDAO.AccountQuery())
                    .whereEmail(email)
                    .getSingleResult();

            if (createdAccount == null) {
                showErrorMessage("Error creating account.");
                return false;
            }

            LoginState.login(createdAccount);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param email
     * @param password
     * @return
     */
    public boolean validateCredentials(String email, String password) {
        if (!Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
            showErrorMessage("Invalid email format.");
            return false;
        }

        if (!Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()) {
            showErrorMessage("Password must be at least 8 characters, include one uppercase letter, one digit, and one special character.");
            return false;
        }

        return true;
    }
}