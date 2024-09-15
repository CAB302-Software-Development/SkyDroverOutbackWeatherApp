package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity(name = "account")
@Table(name = "account")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Integer id;

  @Getter @Setter
  private String email;

  @Getter
  private String password_hash;

  @Getter @Setter
  private Boolean preferCelsius;

  public Account() {
  }

  public Account(Integer id, String email, String password_hash) {
    this.id = id;
    this.email = email;
    setPassword_hash(password_hash);
  }

  public Account(String email, String password_hash) {
    this.email = email;
    setPassword_hash(password_hash);
  }

  public void setPassword_hash(String password) {
    this.password_hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
  }

  public boolean checkPassword(String password) {
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
