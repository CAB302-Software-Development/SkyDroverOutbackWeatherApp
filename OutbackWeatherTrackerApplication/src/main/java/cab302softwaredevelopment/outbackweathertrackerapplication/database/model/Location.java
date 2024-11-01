package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
class LocationId implements Serializable {
  private Account account;
  private Double longitude;
  private Double latitude;
}

@Getter
@Entity(name = "location")
@Table(name = "location")
@IdClass(LocationId.class)
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
/**
 * A model class for the Location entity.
 */
public class Location {

  /**
   * The unique identifier for the location.
   */
  @Column(name="id", nullable = false)
  private Long id;

  /**
   * The account that this location is associated with.
   */
  @Id
  @ManyToOne
  @PrimaryKeyJoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  @EqualsAndHashCode.Exclude
  private Account account;

  /**
   * The longitude of the location.
   */
  @Id
  @Column(name="longitude", nullable = false)
  private Double longitude;

  /**
   * The latitude of the location.
   */
  @Id
  @Column(name="latitude", nullable = false)
  private Double latitude;


  /**
   * The name of the location.
   */
  @Column(name="name", nullable = false)
  private String name;

  public Location() {
  }

  /**
   * Creates a new Location object.
   * @param account The account that this location is associated with.
   * @param longitude The longitude of the location.
   * @param latitude The latitude of the location.
   * @param name The name of the location.
   */
  public Location(Account account,Double longitude, Double latitude, String name) {
    this.account = account;
    this.longitude = longitude;
    this.latitude = latitude;
    this.name = name;
    this.id = generateId();
  }

  /**
   * Method to generate the hash ID from the composite key fields.
   * @return The hash value as an integer.
   */
  private long generateId() {
    return Objects.hash(account.getId(), longitude, latitude);
  }
}
