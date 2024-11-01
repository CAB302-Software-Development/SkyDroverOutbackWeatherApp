package cab302softwaredevelopment.outbackweathertrackerapplication.models.dto;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * This class represents the model used for creating a new user. It contains the essential details
 * such as username, password, and email required to create a user.
 */
@Setter
@Getter
public class CreateUserDTO {

  /**
   * The username of the new user.
   */
  private String username;

  /**
   * The password for the new user account.
   */
  private String userPassword;

  /**
   * The email address of the new user.
   */
  private String userEmail;

  /**
   * The application theme of the new user.
   */
  private String userTheme;

  /**
   * The preferred Celsius of the user.
   */
  private boolean preferCelsius;

  /**
   * The selected layout of the user.
   */
  private String selectedLayout;

  /**
   * The dashboard layout of the user.
   */
  private JSONObject dashboardLayout;

  /**
   * The selected locations of the user.
   */
  private JSONObject locations;
}