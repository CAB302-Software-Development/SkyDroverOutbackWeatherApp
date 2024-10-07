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
import java.util.HashMap;
import java.util.UUID;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Converter(autoApply = true)
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
/**
 * A model class for the Account entity.
 */
public class Account {

  /**
   * The ID of the account.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

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
   * The users layouts.
   */
  @Convert(converter = LayoutsConverter.class)
  @Column(length=LONG32)
  @Default
  private HashMap<String, WidgetInfo[]> dashboardLayouts = new LayoutsConverter().convertToEntityAttribute("{'default':[]}");

  public Account() {
  }

  public Account(UUID id, String email,String password,Boolean preferCelsius,Boolean isGuest,Theme currentTheme,String selectedLayout,HashMap<String,WidgetInfo[]> dashboardLayouts){
    this.id = id;
    this.email = email;
    setPassword(password);
    this.preferCelsius = preferCelsius;
    this.isGuest = isGuest;
    this.currentTheme = currentTheme;
    this.selectedLayout = selectedLayout;
    this.dashboardLayouts = dashboardLayouts;
  }

  /**
   * Sets the password of the account.
   *
   * @param password The password to set. (will be hashed)
   */
  public void setPassword(String password) {
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
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

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        '}';
  }

}
