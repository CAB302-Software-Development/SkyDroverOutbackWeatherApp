package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import static org.hibernate.Length.LONG32;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Converter(autoApply = true)
@EqualsAndHashCode
class LayoutsConverter implements AttributeConverter<HashMap<String, WidgetInfo[]>, String> {

  private static final Gson gson = new Gson();

  @Override
  public String convertToDatabaseColumn(HashMap<String, WidgetInfo[]> layouts) {
    return gson.toJson(layouts);  // Convert the Layouts object to a JSON string
  }

  @Override
  public HashMap<String, WidgetInfo[]> convertToEntityAttribute(String json) {
    Type type = new TypeToken<HashMap<String, WidgetInfo[]>>() {}.getType();
    return gson.fromJson(json, type); // Convert the JSON string back to a Layouts object
  }
}

@Entity(name = "account")
@Table(name = "account", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})
@Builder
@Getter
@EqualsAndHashCode
@ToString
/**
 * A model class for the Account entity.
 */
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
  private String JWTToken; // TODO

  /**
   * The users layouts.
   */
  @Convert(converter = LayoutsConverter.class)
  @Column(length=LONG32)
  @Default
  @Setter
  private HashMap<String, WidgetInfo[]> dashboardLayouts = new LayoutsConverter().convertToEntityAttribute("{'default':[]}");

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
  public Account(String id, String username, String email,String password,Boolean preferCelsius,Boolean isGuest,Theme currentTheme,String selectedLayout, String JWTToken, HashMap<String,WidgetInfo[]> dashboardLayouts,Date lastModified) {
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
    this.lastModified = new Date(System.currentTimeMillis());
  }

  /**
   * Sets the password of the account.
   *
   * @param password The password to set. (will be hashed)
   */
  public void setPassword(String password) {
    this.password = BCrypt.hashpw(password, "$2a$10$iToUqSeCDLopg8IdvWsuiO");
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
    return new LayoutsConverter().convertToDatabaseColumn(dashboardLayouts);
  }

  public void SetDashboardLayoutsString(String layouts) {
    dashboardLayouts = new LayoutsConverter().convertToEntityAttribute(layouts);
  }
}
