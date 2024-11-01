package cab302softwaredevelopment.outbackweathertrackerapplication.models.dto;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

  /**
   * The username of the updated user.
   */
  private String userName;

  /**
   * The password for the new updated account.
   */
  private String userPassword;

  /**
   * The email address of the updated user.
   */
  private String userEmail;

  /**
   * The application theme of the updated user.
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
  private String dashboardLayout;

  /**
   * The selected locations of the user.
   */
  private String locations;
}
