package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

public class LoginController {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 400;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Hyperlink guestLink;

    @FXML
    private Label invalidLoginLabel;

    @FXML
    private Button closeButton;

    @FXML
    private Text loginTitle;

    @FXML
    public void initialize() {
        // Make sure to check if the label is not null
        if (invalidLoginLabel != null) {
            invalidLoginLabel.setVisible(false);
        }
    }


    // Action handler for the "Login" button
    @FXML
    public void handleLogin() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        // Simple validation logic for demonstration
        if (email.isEmpty() || password.isEmpty()) {
            showInvalidLoginMessage("Please fill in both email and password.");
        } else if (validateLogin(email, password)) {
            // Proceed with successful login (replace with actual logic)
            System.out.println("Login successful!");
            invalidLoginLabel.setVisible(false); // Hide the invalid login message if login is valid
        } else {
            showInvalidLoginMessage("Invalid login. Please try again.");
        }
    }

    // Action handler for the "Forgot Password" link
    @FXML
    public void handleForgotPassword() {
        // Logic for handling forgot password
        System.out.println("Forgot password link clicked!");
        // You can show a new window or dialog for password recovery.
    }

    // Action handler for the "Sign Up" link
    @FXML
    public void handleSignUp() {
        // Logic for handling sign-up
        System.out.println("Sign up link clicked!");
        // You can redirect to a registration window or page.
    }

    // Action handler for the "Continue as Guest" link
    @FXML
    public void handleContinueAsGuest() {
        // Logic for continuing as a guest
        System.out.println("Continue as guest link clicked!");
        // You can bypass login and proceed with guest user logic.
    }

    // Action handler for the "Close" button
    @FXML
    public void handleClose() {
        // Close the window (assuming this is a standalone window)
        System.out.println("Close button clicked!");
        // Replace this line with actual stage closing logic if needed
        closeButton.getScene().getWindow().hide();
    }

    // Utility method to validate login credentials (replace with actual authentication logic)
    private boolean validateLogin(String email, String password) {
        // Simple mock logic: accept login if email is "user" and password is "pass"
        return "user".equals(email) && "pass".equals(password);
    }

    // Show an invalid login message
    private void showInvalidLoginMessage(String message) {
        invalidLoginLabel.setText(message);
        invalidLoginLabel.setVisible(true); // Show the error message
    }
}