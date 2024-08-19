package cab302softwaredevelopment.outbackweathertrackerapplication.database.DAO;

public class Location {

  private int id;
  private double longitude;
  private double latitude;
  private double elevation;

  public Location(int id, Double longitude, Double latitude, Double elevation) {
    this.id = id;
    this.longitude = longitude;
    this.latitude = latitude;
    this.elevation = elevation;
  }

  public Location(Double longitude, Double latitude,Double elevation) {
    this.longitude = longitude;
    this.latitude = latitude;
    this.elevation = elevation;
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


  @Override
  public String toString() {
    return "Location{" +
        "id=" + id +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", elevation=" + elevation +
        '}';
  }
}
