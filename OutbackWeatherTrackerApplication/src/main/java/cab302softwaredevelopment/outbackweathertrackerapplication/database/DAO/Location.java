package cab302softwaredevelopment.outbackweathertrackerapplication.database.DAO;

public class Location {

  private int id;
  private double longitude;
  private double latitude;

  public Location(int id, Double longitude, Double latitude) {
    this.id = id;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public Location(Double longitude, Double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
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


  @Override
  public String toString() {
    return "Location{" +
        "id=" + id +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        '}';
  }
}
