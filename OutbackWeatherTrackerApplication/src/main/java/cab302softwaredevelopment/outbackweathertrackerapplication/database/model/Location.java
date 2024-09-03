package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "locations")
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name="longitude", nullable = false)
  private Double longitude;

  @Column(name="latitude", nullable = false)
  private Double latitude;

  @Column(name="elevation", nullable = false)
  private Double elevation;

  @Column(name="name", nullable = false)
  private String name;

  public Location() {
  }

  public Location(int id, Double longitude, Double latitude, Double elevation, String name) {
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


  public int getId() {
    return id;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getElevation() {
    return elevation;
  }

  public String getName() {
    return name;
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
