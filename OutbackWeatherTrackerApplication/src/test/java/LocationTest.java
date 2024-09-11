import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;

@Execution(SAME_THREAD)
@Timeout(value = 1000, unit = TimeUnit.MILLISECONDS) // no test should take longer than 1 second
public class LocationTest {
  static LocationDAO locationDAO = new LocationDAO();
  static Location location1 = new Location(153.02333324, -27.467331464, 27.0,"Brisbane"); // brisbane
  static Location location2 = new Location(153.06064, -27.58003, 	58.0,"Sunnybank"); // sunnybank
  static Location location3 = new Location(153.0246, 	-27.53436, 	48.0,"Moorooka"); // Moorooka
  static Location location4 = new Location(	153.10236, 		-27.50578, 	14.0,"Carindale"); // Carindale
  static Location location5 = new Location(	152.9, 	-27.5, 		114.0,"Brookfield"); // Brookfield
  static Location location6 = new Location(130.9889, 	-25.2406, 		507.0,"Yulara"); // Yulara

  public void addLocations() {
    // Insert the new locations
    locationDAO.insert(location1);
    locationDAO.insert(location2);
    locationDAO.insert(location3);
    locationDAO.insert(location4);
    locationDAO.insert(location5);
    locationDAO.insert(location6);
  }

  @BeforeAll
  public static void testLocationsGetAllEmpty() {
    // Create the database connection
    Session session = DatabaseConnection.getSession();

    // Verify the connection
    assertTrue(session.isConnected());

    // Verify that the connection is in memory
    assertTrue(
        session.getSessionFactory().getProperties().get("hibernate.connection.url").toString()
            .contains("jdbc:h2:mem:db1"));

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");

    session.close();
  }

  @AfterEach
  public void cleanup() {
    // Create the database connection
    Session session = DatabaseConnection.getSession();

    // Verify the connection
    assertTrue(session.isConnected());

    // Verify that the connection is in memory
    assertTrue(
        session.getSessionFactory().getProperties().get("hibernate.connection.url").toString()
            .contains("jdbc:h2:mem:db1"));

    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Delete the locations
    for (Location location : locations) {
      locationDAO.delete(location);
    }

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");
    session.close();
  }
  
  @Test
  public void testAddLocations() {
    // Insert the new locations
    addLocations();

    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Verify the locations
    assertEquals(6, locations.size());

    // Verify that the locations got assigned an ID and that they're unique
    List<Integer> seenIds = new ArrayList<>();
    for (Location location : locations) {
      assertFalse(seenIds.contains(location.getId()), "Location ID should be unique");
      seenIds.add(location.getId());
    }
  }

  @Test
  public void testGetLocationsByID() {
    // Insert the new locations
    addLocations();

    // Retrieve the locations
    Location location1_result = locationDAO.getById(location1.getId()); // brisbane
    Location location2_result = locationDAO.getById(location2.getId()); // sunnybank
    Location location3_result = locationDAO.getById(location3.getId()); // Moorooka
    Location location4_result = locationDAO.getById(location4.getId()); // Carindale
    Location location5_result = locationDAO.getById(location5.getId()); // Brookfield
    Location location6_result = locationDAO.getById(location6.getId()); // Yulara

    // Verify the locations
    assertEquals(location1.getName(), location1_result.getName());
    assertEquals(location1.getLatitude(), location1_result.getLatitude());
    assertEquals(location1.getLongitude(), location1_result.getLongitude());

    assertEquals(location2.getName(), location2_result.getName());
    assertEquals(location2.getLatitude(), location2_result.getLatitude());
    assertEquals(location2.getLongitude(), location2_result.getLongitude());

    assertEquals(location3.getName(), location3_result.getName());
    assertEquals(location3.getLatitude(), location3_result.getLatitude());
    assertEquals(location3.getLongitude(), location3_result.getLongitude());

    assertEquals(location4.getName(), location4_result.getName());
    assertEquals(location4.getLatitude(), location4_result.getLatitude());
    assertEquals(location4.getLongitude(), location4_result.getLongitude());

    assertEquals(location5.getName(), location5_result.getName());
    assertEquals(location5.getLatitude(), location5_result.getLatitude());
    assertEquals(location5.getLongitude(), location5_result.getLongitude());

    assertEquals(location6.getName(), location6_result.getName());
    assertEquals(location6.getLatitude(), location6_result.getLatitude());
    assertEquals(location6.getLongitude(), location6_result.getLongitude());
  }

  @Test
  public void testDeleteLocations() {
    // Insert the new locations
    addLocations();

    // Delete the locations
    locationDAO.delete(location1);
    locationDAO.delete(location2);
    locationDAO.delete(location3);
    locationDAO.delete(location4);
    locationDAO.delete(location5);
    locationDAO.delete(location6);

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");
  }

  @Test
  public void testDeleteLocationsByID() {
    // Insert the new locations
    addLocations();

    // Delete the locations
    locationDAO.delete(location1.getId());
    locationDAO.delete(location2.getId());
    locationDAO.delete(location3.getId());
    locationDAO.delete(location4.getId());
    locationDAO.delete(location5.getId());
    locationDAO.delete(location6.getId());

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");
  }
}
