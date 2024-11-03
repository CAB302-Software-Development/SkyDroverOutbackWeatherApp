package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * This class represents the user data model stored in the MongoDB collection "UserData".
 * It includes fields such as username, password, email, and account creation and update dates.
 * Annotations are used to map the class to the MongoDB collection and to provide aliasing.
 */
@Setter
@Getter
public class UserModel {

    /**
     * The unique identifier for each user.
     */
    private String id;

    /**
     * The username of the user. This field is mapped to the "user_name" field in the MongoDB
     * document.
     */
    private String username;

    /**
     * The password of the user.
     * This field is stored as "user_password" in the MongoDB document and should be encrypted.
     */
    private String userPassword;

    /**
     * The email address of the user.
     * This field is mapped to the "user_email" field in the MongoDB document.
     */
    private String userEmail;

    /**
     * The application theme of the user.
     * This field is mapped to the "user_theme" field in the MongoDB document.
     */
    private String userTheme;

    /**
     * The preferred temperature unit of the user.
     * This field is mapped to the "prefer_celsius" field in the MongoDB document.
     */
    private Boolean preferCelsius;

    /**
     * The selected layout of the user.
     * This field is mapped to the "selected_layout" field in the MongoDB document.
     */
    private String selectedLayout;

    /**
     * The dashboard layout of the user.
     * This field is mapped to the "dashboard_layout" field in the MongoDB document.
     */
    private String dashboardLayout;

    /**
     * The selected locations of the user.
     * This field is mapped to the "locations" field in the MongoDB document.
     */
    private String locations;

    /**
     * The selected custom alert conditions of the user.
     * This field is mapped to the "custom_alert_conditions" field in the MongoDB document.
     */
    private String customAlertConditions;

    /**
     * The date when the user's account was created.
     * This field is initialized with the current date and mapped to "user_account_creation_date".
     */
    private Date userAccountCreationDate = new Date();

    /**
     * The date when the user's account was last updated.
     * This field is initialized with the current date and mapped to "user_account_update_date".
     */
    private Date userAccountUpdateDate = new Date();
}