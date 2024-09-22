import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;

@Execution(SAME_THREAD)
@Timeout(value = 10000, unit = TimeUnit.MILLISECONDS) // no test should take longer than 10 seconds
public class LocationDAOTest extends DBTest {

  @Test
  public void testLocationsGetAllEmpty() {
    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Verify the locations
    assertEquals(0, locations.size(), "Locations should be empty");
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

  @Test void addLocationWithoutAccount(){
    try {
      // Add locations without accounts
      addLocations();
    } catch (Exception e) {}

    // Verify that the invalid locations were not added
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");
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
  void testGetLocationsByAccount(){
    // Insert the new locations
    addAccounts();
    addLocations();

    // Use a test account
    Account testAccount = accountsTemplate.get(1);

    // Add a relevant location
    Location relevantLocation = new Location(testAccount, 0.0, 0.0, 9999.0, "Sky Island");
    locationDAO.insert(relevantLocation);

    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Verify the locations
    assertEquals(locationsTemplate.size() + 1, locations.size(), "There should be " + (locationsTemplate.size() + 1) + " locations");

    // Retrieve the test account's locations
    List<Location> accountLocations = locationDAO.getByAccount(testAccount);

    // Verify the locations
    assertEquals(1, accountLocations.size(), "There should be 1 location");
  }

  @Test
  void testGetLocationsByAccountId(){
    // Insert the new locations
    addAccounts();
    addLocations();

    // Use a test account
    Account testAccount = accountsTemplate.get(1);

    // Add a relevant location
    Location relevantLocation = new Location(testAccount, 0.0, 0.0, 9999.0, "Sky Island");
    locationDAO.insert(relevantLocation);

    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Verify the locations
    assertEquals(locationsTemplate.size() + 1, locations.size(), "There should be " + (locationsTemplate.size() + 1) + " locations");

    // Retrieve the test account's locations
    List<Location> accountLocations = locationDAO.getByAccountId(testAccount.getId());

    // Verify the locations
    assertEquals(1, accountLocations.size(), "There should be 1 location");
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
