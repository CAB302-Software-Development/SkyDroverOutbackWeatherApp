package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.HashMap;

public class ProfileController extends BasePage {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private CheckBox celsiusCheckBox;

    @Override
    public void updateData() {
        updateProfileInfo();
    }

    private void updateProfileInfo() {
        emailField.setText(userService.getCurrentAccount().getEmail());
        celsiusCheckBox.setSelected(userService.getCurrentAccount().getPreferCelsius());
    }

    @FXML
    private void handleSave() {
        String newEmail = emailField.getText();
        String newPassword = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        boolean preferCelsius = celsiusCheckBox.isSelected();

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Error", "Passwords do not match.");
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
            showAlert("Success", "Profile updated successfully.");
        } else {
            showAlert("Error", "Failed to update profile.");
        }
    }

    @FXML
    private void handleClearStoredData() {
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
                    showAlert("Success", "Stored data cleared successfully.");
                } else {
                    showAlert("Error", "Failed to clear stored data.");
                }
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}