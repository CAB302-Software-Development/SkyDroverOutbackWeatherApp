package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "location")
@Table(name = "location")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Location {

  /**
   * The ID of the location.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter
  private Integer id;

  /**
   * The account that this location is associated with.
   */
  @ManyToOne
  @PrimaryKeyJoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Getter
  private Account account;

  /**
   * The longitude of the location.
   */
  @Column(name="longitude", nullable = false)
  @Getter
  private Double longitude;

  /**
   * The latitude of the location.
   */
  @Column(name="latitude", nullable = false)
  @Getter
  private Double latitude;

  /**
   * The elevation of the location.
   */
  @Column(name="elevation", nullable = false)
  @Getter
  private Double elevation;

  /**
   * The name of the location.
   */
  @Column(name="name", nullable = false)
  @Getter
  private String name;

  public Location() {
  }

  /**
   * Creates a new Location object.
   * @param account The account that this location is associated with.
   * @param longitude The longitude of the location.
   * @param latitude The latitude of the location.
   * @param elevation The elevation of the location.
   * @param name The name of the location.
   */
  public Location(Account account,Double longitude, Double latitude,Double elevation, String name) {
    this.account = account;
    this.longitude = longitude;
    this.latitude = latitude;
    this.elevation = elevation;
    this.name = name;
  }


  @Override
  public String toString() {
    return "Location{" +
        "id=" + id +
        ", account=" + account +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", elevation=" + elevation +
        ", name='" + name + "'" +
        '}';
  }

}
