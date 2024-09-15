package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "location")
@Table(name = "location")
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter
  private Integer id;

  @Column(name="longitude", nullable = false)
  @Getter
  private Double longitude;

  @Column(name="latitude", nullable = false)
  @Getter
  private Double latitude;

  @Column(name="elevation", nullable = false)
  @Getter
  private Double elevation;

  @Column(name="name", nullable = false)
  @Getter
  private String name;

  public Location() {
  }

  public Location(Integer id, Double longitude, Double latitude, Double elevation, String name) {
    this.id = id;
    this.longitude = longitude;
    this.latitude = latitude;
    this.elevation = elevation;
    this.name = name;
  }

  public Location(Double longitude, Double latitude,Double elevation, String name) {
    this.longitude = longitude;
    this.latitude = latitude;
    this.elevation = elevation;
    this.name = name;
  }


  @Override
  public String toString() {
    return "Location{" +
        "id=" + id +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", elevation=" + elevation +
        ", name='" + name + "'" +
        '}';
  }

}
