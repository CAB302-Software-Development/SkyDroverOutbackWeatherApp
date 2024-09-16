package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import jakarta.persistence.NoResultException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    public void switchToSignup() {
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
    public void switchToLogin() {
        signupPane.setVisible(false);
        signupPane.setManaged(false);
        loginPane.setVisible(true);
        loginPane.setManaged(true);
        isLogin = true;
    }

    /**
     * Action handler for the "Login" button
      */
    @FXML
    public void handleLogin() {
        if(!isLogin) return;

        String email = emailTextFieldLogin.getText();
        String password = passwordTextFieldLogin.getText();

        if (isInvalidEmail(email)) {
            showErrorMessage("Invalid email format.");
            return;
        }

        if (isInvalidPassword(password)) {
            showErrorMessage("Password must be at least 8 characters, include one uppercase letter, one digit, and one special character.");
            return;
        }

        AccountDAO accountDAO = new AccountDAO();
        Account account;
        try {
            account = accountDAO.getByEmail(email);
        } catch (NoResultException e) {
            showErrorMessage("No account exists with specified email.");
            return;
        }

        if (!account.verifyPassword(password)) {
            showErrorMessage("Incorrect password");
            return;
        }

        try {
            continueToApplication();
        } catch (IOException e) {
            showErrorMessage("Error starting application");
            e.printStackTrace();
        }
    }

    /**
     * Action handler for the "Sign Up" button
     */
    @FXML
    public void handleSignUp() {
        if(isLogin) return;

        String email = emailTextFieldSignup.getText();
        String password = passwordTextFieldSignup.getText();

        if (isInvalidEmail(email)) {
            showErrorMessage("Invalid email format.");
            return;
        }

        if (isInvalidPassword(password)) {
            showErrorMessage("Password must be at least 8 characters, include one uppercase letter, one digit, and one special character.");
            return;
        }

        AccountDAO accountDAO = new AccountDAO();

        try {
            if (accountDAO.getByEmail(email) != null) {
                showErrorMessage("User already exists with this email.");
                return;
            }
        } catch (NoResultException e) {
            Logger.printLog("User does not already exist. Creating account...");
        }

        Account account = new Account(email, password, true);

        try {
            accountDAO.insert(account);
            accountDAO.getByEmail(email);
        } catch (Exception e) {
            showErrorMessage("Error creating new account.");
            e.printStackTrace();
            return;
        }

        Logger.printLog("Account created with email: " + email);

        try {
            continueToApplication();
        } catch (IOException e) {
            showErrorMessage("Error starting application");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGuestAccess() {
        try {
            continueToApplication();
        } catch (IOException e) {
            showErrorMessage("Error starting application");
            e.printStackTrace();
        }
    }

    private static boolean isInvalidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return !pattern.matcher(email).matches();
    }

    private static boolean isInvalidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return !pattern.matcher(password).matches();
    }

    /**
     * Show an invalid login message
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
        clearInputFields();
    }

    private void clearInputFields() {
        emailTextFieldSignup.setText("");
        emailTextFieldLogin.setText("");
        passwordTextFieldLogin.setText("");
        passwordTextFieldSignup.setText("");
    }

    /**
     * Closes the login / sign up window and initializes the main application view
     * @throws IOException
     */
    private void continueToApplication() throws IOException {
        ((Stage) loginPane.getScene().getWindow()).close();

        FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/main-view.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        Scene scene = new Scene(root, MainController.WIDTH, MainController.HEIGHT);
        controller.setScene(scene);

        Stage mainStage = new Stage();
        mainStage.setTitle(ApplicationEntry.stageTitle);
        mainStage.setScene(scene);
        mainStage.show();
    }
}