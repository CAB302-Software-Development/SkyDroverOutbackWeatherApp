import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
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
  static AccountDAO accountDAO = new AccountDAO();
  static LocationDAO locationDAO = new LocationDAO();
  static List<Location> locationsTemplate = new ArrayList<>();
  static List<Account> accountsTemplate = new ArrayList<>();

  public void addAccounts() {
    // Insert the new accounts
    for (Account account : accountsTemplate) {
      accountDAO.insert(account);
    }
  }

  public void addLocations() {
    // Insert the new locations
    for (Location location : locationsTemplate) {
      locationDAO.insert(location);
    }
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

    // Add the accounts to the template
    accountsTemplate.add(new Account("test1@gmail.com", "password1"));
    accountsTemplate.add(new Account("test2@gmail.com", "password2"));
    accountsTemplate.add(new Account("test3@gmail.com", "password3"));

    // Add the locations to the template
    locationsTemplate.add(new Location(accountsTemplate.get(0),153.02333324, -27.467331464, 27.0,"Brisbane")); // brisbane
    locationsTemplate.add(new Location(accountsTemplate.get(0),153.06064, -27.58003, 	58.0,"Sunnybank")); // sunnybank
    locationsTemplate.add(new Location(accountsTemplate.get(0),153.0246, 	-27.53436, 	48.0,"Moorooka")); // Moorooka
    locationsTemplate.add(new Location(accountsTemplate.get(0),153.10236, 		-27.50578, 	14.0,"Carindale")); // Carindale
    locationsTemplate.add(new Location(accountsTemplate.get(0),152.9, 	-27.5, 		114.0,"Brookfield")); // Brookfield
    locationsTemplate.add(new Location(accountsTemplate.get(0),130.9889, 	-25.2406, 		507.0,"Yulara")); // Yulara
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

    // Retrieve the accounts
    List<Account> accounts = accountDAO.getAll();

    // Delete the accounts
    for (Account account : accounts) {
      accountDAO.delete(account.getId());
    }

    // Verify the accounts
    assertEquals(0, accountDAO.getAll().size(), "Accounts should be empty");

    session.close();
  }
  
  @Test
  public void testAddLocations() {
    // Insert the new locations
    addAccounts();
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
    addAccounts();
    addLocations();

    // Retrieve the locations
    List<Location> locations = new ArrayList<>();
    for (Location location : locationsTemplate) {
      locations.add(locationDAO.getById(location.getId()));
    }

    // Verify the locations
    for (int i = 0; i < locations.size(); i++) {
      assertNotNull(locations.get(i));
      assertEquals(locationsTemplate.get(i).getName(), locations.get(i).getName());
      assertEquals(locationsTemplate.get(i).getLatitude(), locations.get(i).getLatitude());
      assertEquals(locationsTemplate.get(i).getLongitude(), locations.get(i).getLongitude());
    }
  }

  @Test
  public void testDeleteLocations() {
    // Insert the new locations
    addAccounts();
    addLocations();

    // Delete the locations
    for (Location location : locationsTemplate) {
      locationDAO.delete(location);
    }

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");
  }

  @Test
  public void testDeleteLocationsByID() {
    // Insert the new locations
    addAccounts();
    addLocations();

    // Delete the locations
    for (Location location : locationsTemplate) {
      locationDAO.delete(location.getId());
    }

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");
  }
}
