package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity(name = "account")
@Table(name = "account", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})

/**
 * A model class for the Account entity.
 */
public class Account {

  /**
   * The ID of the account.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Integer id;

  /**
   * The email of the account.
   */
  @Getter @Setter
  private String email;

  /**
   * The hashed password of the account.
   */
  @Getter
  private String password_hash;

  /**
   * Whether the account prefers Celsius or Fahrenheit.
   */
  @Getter @Setter
  private Boolean preferCelsius;

  public Account() {
  }

  /**
   * Creates a new Account object.
   *
   * @param id The ID of the account.
   * @param email The email of the account.
   * @param password The password of the account. (will be hashed)
   * @param preferCelsius Whether the account prefers Celsius or Fahrenheit.
   */
  public Account(Integer id, String email, String password, Boolean preferCelsius) {
    this.id = id;
    this.email = email;
    this.preferCelsius = preferCelsius;
    setPassword(password);
  }

  /**
   * Creates a new Account object.
   *
   * @param email The email of the account.
   * @param password The password of the account. (will be hashed)
   * @param preferCelsius Whether the account prefers Celsius or Fahrenheit.
   */
  public Account(String email, String password, Boolean preferCelsius) {
    this.email = email;
    this.preferCelsius = preferCelsius;
    setPassword(password);
  }

  /**
   * Sets the password of the account.
   *
   * @param password The password to set. (will be hashed)
   */
  public void setPassword(String password) {
    this.password_hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
  }

  /**
   * Verifies the password of the account.
   *
   * @param password The password to verify.
   * @return True if the password is correct, false otherwise.
   */
  public boolean verifyPassword(String password) {
    return BCrypt.checkpw(password, this.password_hash);
  }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", email='" + email + '\'' +
        ", password_hash='" + password_hash + '\'' +
        '}';
  }

}
