package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.HashMap;

/**
 * Controller class for managing user profile settings, allowing users to update email, password, and preferences.
 * Also provides functionality for clearing stored user data, with restricted access for guest users.
 */
public class ProfileController extends BasePage {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private CheckBox celsiusCheckBox;
    @FXML
    private Button saveButton, clearDataButton;
    @FXML
    private Label guestUserLabel;

    /**
     * Updates the profile data displayed on the profile screen.
     * This method is called to refresh profile information when necessary.
     */
    @Override
    public void updateData() {
        updateProfileInfo();
    }

    /**
     * Loads and displays the current user's profile information.
     * Disables editing for guest users and updates the UI accordingly.
     */
    private void updateProfileInfo() {
        if (userService.isGuest()) {
            handleGuestUser();
        } else {
            emailField.setText(userService.getCurrentAccount().getEmail());
            celsiusCheckBox.setSelected(userService.getCurrentAccount().getPreferCelsius());
        }
    }

    /**
     * Handles profile settings for guest users, disabling fields and showing a message indicating limited access.
     */
    private void handleGuestUser() {
        // Disable all fields and buttons
        emailField.setDisable(true);
        passwordField.setDisable(true);
        confirmPasswordField.setDisable(true);
        celsiusCheckBox.setDisable(true);
        saveButton.setDisable(true);
        clearDataButton.setDisable(true);

        // Display a message to the guest user
        guestUserLabel.setText("Guest users cannot update profile details. Please log in or create an account.");
        guestUserLabel.setVisible(true);
    }

    /**
     * Saves the updated profile information, including email, password, and temperature preference.
     * Validates input and displays feedback on success or failure.
     */
    @FXML
    private void handleSave() {
        if (userService.isGuest()) {
            MainController.showAlert("Guest Account", "Guest users cannot update profile details.");
            return;
        }

        String newEmail = emailField.getText();
        String newPassword = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        boolean preferCelsius = celsiusCheckBox.isSelected();

        if (!newPassword.equals(confirmPassword)) {
            MainController.showAlert("Password Error", "Passwords do not match.");
            return;
        }

        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setEmail(newEmail);
        if (!newPassword.isEmpty()) {
            updateModel.setPassword(newPassword);
        }
        updateModel.setPreferCelsius(preferCelsius);

        boolean updateSuccess = userService.updateCurrentAccount(updateModel);
        if (updateSuccess) {
            MainController.showAlert("Success", "Profile updated successfully.");
        } else {
            MainController.showAlert("Error", "Failed to update profile.");
        }
    }

    /**
     * Clears all stored data, including dashboard layouts and locations, for the current user.
     * Displays a confirmation prompt before proceeding with the data reset.
     */
    @FXML
    private void handleClearStoredData() {
        if (userService.isGuest()) {
            MainController.showAlert("Guest Account", "Guest users cannot clear stored data.");
            return;
        }

        // Show a confirmation dialog to the user
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Data Reset");
        confirmationAlert.setHeaderText("Are you sure you want to clear all stored data?");
        confirmationAlert.setContentText("This action will reset your dashboard and delete all stored locations.");

        ButtonType confirmButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                AccountUpdateModel updateModel = new AccountUpdateModel();
                updateModel.setDashboardLayouts(new HashMap<>() {{
                    put("default", new WidgetInfo[0]);
                }});
                updateModel.setSelectedLayout("default");

                boolean updateSuccess = userService.updateCurrentAccount(updateModel);
                updateSuccess = locationService.deleteAllUserLocations() && updateSuccess;

                if (updateSuccess) {
                    MainController.showAlert("Success", "Stored data cleared successfully.");
                } else {
                    MainController.showAlert("Error", "Failed to clear stored data.");
                }
            }
        });
    }
}
