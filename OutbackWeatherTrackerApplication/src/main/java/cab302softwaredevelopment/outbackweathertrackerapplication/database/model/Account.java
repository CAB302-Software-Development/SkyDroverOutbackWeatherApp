package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "account")
@Table(name = "account")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Integer id;

  @Getter @Setter
  private String email;

  @Getter @Setter
  private String password;

  public Account() {
  }

  public Account(Integer id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public Account(String email, String password) {
    this.email = email;
    this.password = password;
  }




  @Override
  public String toString() {
    return "Location{" +
        "id=" + id +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        '}';
  }

}
