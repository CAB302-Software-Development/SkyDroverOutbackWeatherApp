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
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity(name = "account")
@Table(name = "account", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})
@Builder
@Getter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true,doNotUseGetters = true)
@ToString
/**
 * A model class for the Account entity.
 */
public class Account {

  /**
   * The ID of the account.
   */
  @Id
  @EqualsAndHashCode.Include
  private String id;

  /**
   * The username of the account.
   */
  @Setter
  @Column(nullable = false)
  @EqualsAndHashCode.Include
  private String username;

  /**
   * The email of the account.
   */
  @Setter
  @Column(nullable = false)
  @EqualsAndHashCode.Include
  private String email;

  /**
   * The hashed password of the account.
   */
  @EqualsAndHashCode.Include
  private String password;

  /**
   * Whether the account prefers Celsius or Fahrenheit.
   */
  @Setter
  @Default
  @EqualsAndHashCode.Include
  private Boolean preferCelsius = true;

  /**
   * Whether the account is a guest account.
   */
  @Setter
  @Default
  @EqualsAndHashCode.Include
  private Boolean isGuest = false;

  /**
   * The accounts selected theme.
   */
  @Setter
  @Default
  @EqualsAndHashCode.Include
  private Theme currentTheme = Theme.Light;

  /**
   * The accounts selected layout.
   */
  @Setter
  @Default
  @EqualsAndHashCode.Include
  private String selectedLayout = "default";

  /**
   * The JWT token of the account once logged in.
   */
  @Setter
  @EqualsAndHashCode.Include
  private String JWTToken;

  /**
   * The users layouts.
   */
  @Convert(converter = WidgetInfoListConverter.class)
  @Column(length=LONG32)
  @Default
  @Setter
  @EqualsAndHashCode.Include
  private HashMap<String, WidgetInfo[]> dashboardLayouts = new WidgetInfoListConverter().convertToEntityAttribute("{'default':[]}");

  /**
   * The users custom alert conditions.
   */
  @Convert(converter = CustomAlertConditionListConverter.class)
  @Column(length=LONG32)
  @Default
  @Setter
  @EqualsAndHashCode.Include
  private List<CustomAlertCondition> customAlertConditions = List.of();

  /**
   * The date that data was last modified. (for server sync)
   */
  @Setter
  private Date lastModified; // TODO

  public Account() {
  }

  /**
   * TODO: Add Javadoc
   * @param id
   * @param email
   * @param password
   * @param preferCelsius
   * @param isGuest
   * @param currentTheme
   * @param selectedLayout
   * @param dashboardLayouts
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

  public String GetDashboardLayoutsString() {
    return new WidgetInfoListConverter().convertToDatabaseColumn(dashboardLayouts);
  }

  public void SetDashboardLayoutsString(String layouts) {
    dashboardLayouts = new WidgetInfoListConverter().convertToEntityAttribute(layouts);
  }

  public String GetCustomAlertConditionsString() {
    return new CustomAlertConditionListConverter().convertToDatabaseColumn(customAlertConditions);
  }

  public void SetCustomAlertConditionsString(String conditions) {
    customAlertConditions = new CustomAlertConditionListConverter().convertToEntityAttribute(conditions);
  }
}
