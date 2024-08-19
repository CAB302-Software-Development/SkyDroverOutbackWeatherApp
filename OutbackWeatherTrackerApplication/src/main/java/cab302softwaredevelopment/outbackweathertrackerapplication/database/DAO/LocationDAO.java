package cab302softwaredevelopment.outbackweathertrackerapplication.database.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;

public class LocationDAO {
  private Connection connection;

  public LocationDAO() {
    connection = DatabaseConnection.getInstance();
  }

  public void createTable() {
    try {
      Statement createTable = connection.createStatement();
      createTable.execute(
          "CREATE TABLE IF NOT EXISTS locations ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
              + "longitude DOUBLE NOT NULL, "
              + "latitude DOUBLE NOT NULL, "
              + "elevation DOUBLE NOT NULL"
              + ")"
      );
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public void insert(Location location) {
    try {
      PreparedStatement insertLocation = connection.prepareStatement(
          "INSERT INTO locations (longitude, latitude, elevation) VALUES (?, ?, ?)"
      );
      insertLocation.setDouble(1, location.getLongitude());
      insertLocation.setDouble(2, location.getLatitude());
      insertLocation.setDouble(3, location.getElevation());
      insertLocation.execute();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public void update(Location location) {
    try {
      PreparedStatement updateLocation = connection.prepareStatement(
          "UPDATE locations SET longitude = ?, latitude = ?, elevation = ? WHERE id = ?"
      );
      updateLocation.setDouble(1, location.getLongitude());
      updateLocation.setDouble(2, location.getLatitude());
      updateLocation.setDouble(3, location.getElevation());
      updateLocation.setInt(4, location.getId());
      updateLocation.execute();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public void delete(int id) {
    try {
      PreparedStatement deleteLocation = connection.prepareStatement("DELETE FROM locations WHERE id = ?");
      deleteLocation.setInt(1, id);
      deleteLocation.execute();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public List<Location> getAll() {
    List<Location>locations = new ArrayList<>();
    try {
      Statement getAll = connection.createStatement();
      ResultSet rs = getAll.executeQuery("SELECT * FROM locations");
      while (rs.next()) {
        locations.add(
            new Location(
                rs.getInt("id"),
                rs.getDouble("longitude"),
                rs.getDouble("latitude"),
                rs.getDouble("elevation")
            )
        );
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return locations;
  }

  public Location getById(int id) {
    try {
      PreparedStatement getLocation = connection.prepareStatement("SELECT * FROM locations WHERE id = ?");
      getLocation.setInt(1, id);
      ResultSet rs = getLocation.executeQuery();
      if (rs.next()) {
        return new Location(
            rs.getInt("id"),
            rs.getDouble("longitude"),
            rs.getDouble("latitude"),
            rs.getDouble("elevation")
        );
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return null;
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }
}
