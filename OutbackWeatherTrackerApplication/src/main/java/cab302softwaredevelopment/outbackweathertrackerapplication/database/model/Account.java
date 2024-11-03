package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import static org.hibernate.Length.LONG32;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters.*;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Represents an account in the database.
 */
@Entity(name = "account")
@Table(name = "account", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})
@Builder
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Account {
  /**
   * The ID of the account.
   */
  @Id

  private String id;

  /**
   * The username of the account.
   */
  @Setter
  @Column(nullable = false)
  private String username;

  /**
   * The email of the account.
   */
  @Setter
  @Column(nullable = false)
  private String email;

  /**
   * The hashed password of the account.
   */
  private String password;

  /**
   * Whether the account prefers Celsius or Fahrenheit.
   */
  @Setter
  @Default
  private Boolean preferCelsius = true;

  /**
   * Whether the account is a guest account.
   */
  @Setter
  @Default
  private Boolean isGuest = false;

  /**
   * The accounts selected theme.
   */
  @Setter
  @Default
  private Theme currentTheme = Theme.Light;

  /**
   * The accounts selected layout.
   */
  @Setter
  @Default
  private String selectedLayout = "default";

  /**
   * The JWT token of the account once logged in.
   */
  @Setter
  private String JWTToken;

  /**
   * The users layouts.
   */
  @Convert(converter = WidgetInfoListConverter.class)
  @Column(length=LONG32)
  @Default
  @Setter
  private HashMap<String, WidgetInfo[]> dashboardLayouts = new WidgetInfoListConverter().convertToEntityAttribute("{'default':[]}");

  /**
   * The users custom alert conditions.
   */
  @Convert(converter = CustomAlertConditionListConverter.class)
  @Column(length=LONG32)
  @Default
  @Setter
  private List<CustomAlertCondition> customAlertConditions = List.of();

  /**
   * The date that data was last modified. (for server sync)
   */
  @Setter
  private Date lastModified;

  /**
   * Creates a new account.
   * @param id Database ID
   * @param username Username of the account
   * @param email Email of the account
   * @param password Password of the account (will be hashed)
   * @param preferCelsius Whether the account prefers Celsius or Fahrenheit
   * @param isGuest Whether the account is a guest account
   * @param currentTheme The accounts selected theme
   * @param selectedLayout The accounts selected layout
   * @param JWTToken The JWT token of the account once logged in
   * @param dashboardLayouts The users layouts
   * @param customAlertConditions The users custom alert conditions
   * @param lastModified The date that data was last modified. (for server sync)
   */
  public Account(String id, String username, String email,String password,Boolean preferCelsius,Boolean isGuest,Theme currentTheme,String selectedLayout, String JWTToken, HashMap<String,WidgetInfo[]> dashboardLayouts, List<CustomAlertCondition> customAlertConditions, Date lastModified) {
    this.id = id;
    this.username = username;
    this.email = email;
    setPassword(password);
    this.preferCelsius = preferCelsius;
    this.isGuest = isGuest;
    this.currentTheme = currentTheme;
    this.selectedLayout = selectedLayout;
    this.JWTToken = JWTToken;
    this.dashboardLayouts = dashboardLayouts;
    this.customAlertConditions = customAlertConditions;
    this.lastModified = new Date(System.currentTimeMillis());
  }

  /**
   * Sets the password of the account.
   *
   * @param password The password to set. (will be hashed)
   */
  public void setPassword(String password) {
    setPassword(password, true);
  }

  /**
   * Sets the password of the account.
   *
   * @param password The password to set. (will be hashed)
   * @param hash Whether to hash the password or not.
   */
  public void setPassword(String password, boolean hash) {
    if (hash) {
      this.password = BCrypt.hashpw(password, "$2a$10$iToUqSeCDLopg8IdvWsuiO");
    } else {
      this.password = password;
    }
  }

  /**
   * Verifies the password of the account.
   *
   * @param password The password to verify.
   * @return True if the password is correct, false otherwise.
   */
  public boolean verifyPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  /**
   * Serializes the dashboard layouts to a string.
   * @return The serialized dashboard layouts in json format.
   */
  public String GetDashboardLayoutsString() {
    return new WidgetInfoListConverter().convertToDatabaseColumn(dashboardLayouts);
  }

  /**
   * Deserializes the dashboard layouts from a string.
   * @param layouts The serialized dashboard layouts in json format.
   */
  public void SetDashboardLayoutsString(String layouts) {
    dashboardLayouts = new WidgetInfoListConverter().convertToEntityAttribute(layouts);
  }

  /**
   * Serializes the custom alert conditions to a string.
   * @return The serialized custom alert conditions in json format.
   */
  public String GetCustomAlertConditionsString() {
    return new CustomAlertConditionListConverter().convertToDatabaseColumn(customAlertConditions);
  }

  /**
   * Deserializes the custom alert conditions from a string.
   * @param conditions The serialized custom alert conditions in json format.
   */
  public void SetCustomAlertConditionsString(String conditions) {
    customAlertConditions = new CustomAlertConditionListConverter().convertToEntityAttribute(conditions);
  }
}
