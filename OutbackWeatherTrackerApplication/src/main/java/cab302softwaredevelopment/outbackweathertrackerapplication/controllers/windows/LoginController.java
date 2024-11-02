package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters.LocationListConverter;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.UserModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CreateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UpdateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UserLoginRequestDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ConnectionService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.InputService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserApiService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCrypt;

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
    private VBox loginPane;
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
    private VBox signupPane;
    @FXML
    private TextField emailTextFieldSignup, usernameTextFieldSignup;
    @FXML
    private PasswordField passwordTextFieldSignup, passwordTextConfirm;
    @FXML
    private Button signupButton, btnLocation;
    @FXML
    private Hyperlink loginLink, guestLinkSignup;
    @FXML
    private Label signupMessageLabel;
    @FXML
    private Text signupTitle;


    private LocationCreateModel selectedLocation = null;
    private boolean isLogin = true;
    private UserService userService;

    @FXML
    public void initialize() {
        userService = UserService.getInstance();
    }


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
            String email = emailTextFieldLogin.getText().toLowerCase(Locale.ROOT);
            String password = passwordTextFieldLogin.getText();
            if (handleLogin(email, password)) {
                continueToApplication();
            }
        } else if (e.getSource() == signupButton) {
            String email = emailTextFieldSignup.getText().toLowerCase(Locale.ROOT);
            String username = usernameTextFieldSignup.getText();
            String password = passwordTextFieldSignup.getText();
            String passwordConfirm = passwordTextConfirm.getText();
            if (handleSignUp(email, username, password, passwordConfirm, selectedLocation)) {
                continueToApplication();
            }
        } else if (e.getSource() == guestLinkLogin || e.getSource() == guestLinkSignup) {
            if(handleGuestAccess()) {
                continueToApplication();
            }
        } else if (e.getSource() == btnLocation) {
            selectedLocation = InputService.getLocation("Select location", "Please select an initial location for weather readings");
            if (selectedLocation == null) {
                btnLocation.setText("Select Location");
            } else {
                btnLocation.setText("Change Location");
            }
        }
    }

    private boolean handleGuestAccess() {
        selectedLocation = InputService.getLocation("Select location", "Please select an initial location for weather readings");
        if (selectedLocation == null) return false;
        userService.logout(selectedLocation);
        return true;
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

            boolean offlineMode = ConnectionService.getInstance().isOffline();
            AccountDAO accountDAO = new AccountDAO();

            Account localAccount;

            if (!offlineMode) {
                // Login to account on server
                UserApiService userApiService = new UserApiService();
                String token;
                try {
                    token = userApiService.login(email, password);
                } catch (Exception e) {
                    throw new RuntimeException(e); // TODO: Handle this better (show error message)
                }

                // Get the account
                UserModel userModel;
                Account remoteAccount;
                try {
                    userModel = userApiService.getCurrentUser(token);
                    remoteAccount = userApiService.getCurrentAccount(userModel, token);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Check the last modified date
                localAccount = (new AccountDAO.AccountQuery())
                    .whereEmail(email)
                    .getSingleResult();

                if (localAccount == null
                    || remoteAccount.getLastModified().getTime() > localAccount.getLastModified()
                    .getTime()) {
                    // Save the account
                    if (localAccount == null) {
                        // Account doesnt exist locally, create it
                        accountDAO.insert(remoteAccount);
                    } else {
                        // Account exists locally, update it
                        accountDAO.update(remoteAccount);
                    }

                    // The local locations are not up to date, update them
                    List<Location> localLocations = LocationService.getInstance()
                        .getLocationsForUser(localAccount);
                    List<Location> remoteLocations = new LocationListConverter().convertToEntityAttribute(
                        userModel.getLocations());

                    LocationDAO locationDAO = new LocationDAO();
                    // Delete all locations
                    for (Location location : localLocations) {
                        locationDAO.delete(location);
                    }

                    for (Location location : remoteLocations) {
                        locationDAO.insert(location);
                    }

                    assert localAccount != null;
                    localAccount.setLastModified(userModel.getUserAccountUpdateDate());
                    accountDAO.update(localAccount);
                } else {
                    // Update the remote account

                    // Get the locations
                    List<Location> locations = LocationService.getInstance()
                        .getLocationsForUser(localAccount);

                    UpdateUserDTO userDTO = new UpdateUserDTO();
                    userDTO.setUsername(localAccount.getUsername());
                    userDTO.setUserPassword(localAccount.getPassword());
                    userDTO.setUserEmail(localAccount.getEmail());
                    userDTO.setUserTheme(localAccount.getCurrentTheme().toString());
                    userDTO.setPreferCelsius(localAccount.getPreferCelsius());
                    userDTO.setSelectedLayout(localAccount.getSelectedLayout());
                    userDTO.setDashboardLayout(localAccount.GetDashboardLayoutsString());
                    userDTO.setLocations(
                        new Gson().toJson(locations)); // Locations are stored as a JSON string

                    CreateUserDTO result;
                    try {
                        result = userApiService.updateUser(localAccount.getId(), userDTO,
                            localAccount.getJWTToken());
                    } catch (Exception e) {
                        throw new RuntimeException(
                            e); // TODO: Handle this better (show error message)
                    }
                }
            }

            // Get the account
            localAccount = (new AccountDAO.AccountQuery())
                .whereEmail(email)
                .getSingleResult();

            if (localAccount == null) {
                showErrorMessage("No account exists with specified email.");
                return false;
            }

            if (!localAccount.verifyPassword(password)) {
                showErrorMessage("Incorrect password");
                return false;
            }

            userService.login(localAccount);
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
    public boolean handleSignUp(String email, String username, String password, String passwordConfirm, LocationCreateModel location) {
        try {
            if (!Objects.equals(password, passwordConfirm)) {
                showErrorMessage("Passwords do not match");
                return false;
            }

            if (location == null) {
                showErrorMessage("Select a valid location");
                return false;
            }

            if (username.isEmpty() || username.isBlank()) {
                showErrorMessage("Username required");
                return false;
            }

            if (!validateCredentials(email, password)) return false;

            if ((new AccountDAO.AccountQuery()).whereEmail(email).getSingleResult() != null) {
                showErrorMessage("User already exists with this email.");
                return false;
            }

            Account createdAccount = userService.createUser(email, username, password, location);

            if (createdAccount == null) {
                showErrorMessage("Error creating account.");
                return false;
            }

            userService.login(createdAccount);

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

    public void handleEnter(ActionEvent actionEvent) {
        if (isLogin) {
            String email = emailTextFieldLogin.getText().toLowerCase(Locale.ROOT);
            String password = passwordTextFieldLogin.getText();
            if (handleLogin(email, password)) {
                continueToApplication();
            }
        } else {
            String email = emailTextFieldSignup.getText().toLowerCase(Locale.ROOT);
            String username = usernameTextFieldSignup.getText();
            String password = passwordTextFieldSignup.getText();
            String passwordConfirm = passwordTextConfirm.getText();
            if (handleSignUp(email, username, password, passwordConfirm, selectedLocation)) {
                continueToApplication();
            }
        }
    }
}