import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

@Execution(SAME_THREAD)
public class DailyForecastTest {
  static DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();
  static LocationDAO locationDAO = new LocationDAO();
  static List<Location> locationsTemplate = new ArrayList<>();
  static List<DailyForecast> dailyForecastsTemplate = new ArrayList<>();

  public void addForecasts() {
    // Insert the new daily forecasts
    for (DailyForecast dailyForecast : dailyForecastsTemplate) {
      dailyForecastDAO.insert(dailyForecast);
    }
  }
  public void addLocations() {
    // Insert the new locations
    for (Location location : locationsTemplate) {
      locationDAO.insert(location);
    }
  }

  @BeforeAll
  public static void testDailyForecastsGetAllEmpty() {
    // Create the database connection
    Session session = DatabaseConnection.getSession();

    // Verify the connection
    assertTrue(session.isConnected());

    // Verify that the connection is in memory
    assertTrue(
        session.getSessionFactory().getProperties().get("hibernate.connection.url").toString()
            .contains("jdbc:h2:mem:db1"));
    session.close();

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(0, dailyForecasts.size(), "Daily Forecasts should be empty");

    // Add the test location

    locationsTemplate.add(new Location(153.02333324, -27.467331464, 27.0,"Brisbane")); // brisbane
    locationsTemplate.add(new Location(153.06064, -27.58003, 	58.0,"Sunnybank")); // sunnybank
    locationsTemplate.add(new Location(153.0246, 	-27.53436, 	48.0,"Moorooka")); // Moorooka
    locationsTemplate.add(new Location(	153.10236, 		-27.50578, 	14.0,"Carindale")); // Carindale
    locationsTemplate.add(new Location(	152.9, 	-27.5, 		114.0,"Brookfield")); // Brookfield
    locationsTemplate.add(new Location(130.9889, 	-25.2406, 		507.0,"Yulara")); // Yulara

    dailyForecastsTemplate.add(new DailyForecast(locationsTemplate.get(0),1725321600, 3, 24.2, 13.9, 23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 18.0, 45.0, 160.0, 20.32, 4.43));
    dailyForecastsTemplate.add(new DailyForecast(locationsTemplate.get(0),1725408000, 3, 21.0, 14.2, 19.8, 14.3, 1725393469, 1725435357, 41928.18, 29210.48, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 11.5, 32.4, 132.0, 15.46, 2.85));
    dailyForecastsTemplate.add(new DailyForecast(locationsTemplate.get(0),1725494400, 3, 22.6, 15.3, 22.8, 15.7, 1725479803, 1725521784, 42021.79, 38157.5, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 12.2, 28.1, 59.0, 16.95, 3.19));
    dailyForecastsTemplate.add(new DailyForecast(locationsTemplate.get(0),1725580800, 3, 24.0, 14.6, 24.3, 15.5, 1725566136, 1725608211, 42115.42, 38910.98, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 13.3, 34.6, 18.0, 17.46, 3.5));
    dailyForecastsTemplate.add(new DailyForecast(locationsTemplate.get(0),1725667200, 3, 25.2, 15.1, 26.0, 16.2, 1725652469, 1725694638, 42209.01, 39056.32, null, null, 0.0, null, 0.0, null, 0.0, 14.8, 31.0, 1.0, 20.09, 3.95));


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
    session.close();
    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Delete the daily forecasts
    for (DailyForecast dailyForecast : dailyForecasts) {
      dailyForecastDAO.delete(dailyForecast.getId());
    }

    // Verify the daily forecasts
    assertEquals(0, dailyForecastDAO.getAll().size(), "Daily Forecasts should be empty");

    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Delete the locations
    for (Location location : locations) {
      locationDAO.delete(location.getId());
    }

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");

  }

  @Test
  public void testAddForecasts() {
    // Insert the new daily forecasts
    addLocations();
    addForecasts();

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecasts.size(), "There should be "+dailyForecastsTemplate.size()+" daily forecasts");

    // Verify that the forecasts got assigned an ID and that they're unique
    List<Integer> seenIds = new ArrayList<>();
    for (DailyForecast dailyForecast : dailyForecasts) {
      assertFalse(seenIds.contains(dailyForecast.getId()), "Daily Forecast ID should be unique");
      seenIds.add(dailyForecast.getId());
    }
  }

  @Test
  public void testAddForecastWithoutLocation() {
    Location fakeLocation = new Location(0.0, 0.0, 0.0,"Null Island");
    DailyForecast invalidForecast = new DailyForecast(fakeLocation,1725321600, 3, 24.2, 13.9, 23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 18.0, 45.0, 160.0, 20.32, 4.43);

    try{
      // Insert the new daily forecast
      dailyForecastDAO.insert(invalidForecast);
    } catch (Exception e) {}

    // Verify that the invalid forecast was not added
    assertEquals(0, dailyForecastDAO.getAll().size(), "There should be 0 daily forecasts");
  }

  @Test void testDeleteForecast() {
    // Insert the new daily forecasts
    addLocations();
    addForecasts();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(), "There should be "+dailyForecastsTemplate.size()+" daily forecasts");

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Delete the daily forecasts
    for (DailyForecast dailyForecast : dailyForecasts) {
      dailyForecastDAO.delete(dailyForecast);
    }

    // Verify the daily forecasts
    assertEquals(0, dailyForecastDAO.getAll().size(), "Daily Forecasts should be empty");
  }

  @Test void testUniqueForecast(){
    // Insert the new daily forecasts
    addLocations();
    addForecasts();

    // Try to insert the same daily forecasts
    addForecasts();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(), "There should be "+dailyForecastsTemplate.size()+" daily forecasts");
  }

  @Test void testGetForecastByID() {
    // Insert the new daily forecasts
    addLocations();
    addForecasts();

    for (DailyForecast dailyForecast : dailyForecastsTemplate) {

      // Retrieve the daily forecast
      DailyForecast dailyForecast_result = dailyForecastDAO.getById(dailyForecast.getId());

      // Verify the daily forecast
      assertEquals(dailyForecast.getId(), dailyForecast_result.getId());
    }
  }

  @Test void testGetForecastsForLocation() {
    // Insert the new daily forecasts
    addLocations();
    addForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    DailyForecast relevantForecast = new DailyForecast(testLocation,1725321600, 3, 24.2, 13.9, 23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 18.0, 45.0, 160.0, 20.32, 4.43);
    dailyForecastDAO.insert(relevantForecast);

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size()+1, dailyForecasts.size(), "There should be "+(dailyForecastsTemplate.size()+1)+" daily forecast");

    // Retrieve test location forecasts
    List<DailyForecast> testForecasts = dailyForecastDAO.getByLocation(testLocation);

    // Verify the daily forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 daily forecast");
  }

  @Test void testGetForecastsForLocationById() {
    // Insert the new daily forecasts
    addLocations();
    addForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    DailyForecast relevantForecast = new DailyForecast(testLocation,1725321600, 3, 24.2, 13.9, 23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 18.0, 45.0, 160.0, 20.32, 4.43);
    dailyForecastDAO.insert(relevantForecast);

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size()+1, dailyForecasts.size(), "There should be "+(dailyForecastsTemplate.size()+1)+" daily forecast");

    // Retrieve test location forecasts
    List<DailyForecast> testForecasts = dailyForecastDAO.getByLocationId(testLocation.getId());

    // Verify the daily forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 daily forecast");
  }

  @Test void testLocationDeleteCascade() {
    // Insert the new daily forecasts
    addLocations();
    addForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    DailyForecast relevantForecast = new DailyForecast(testLocation,1725321600, 3, 24.2, 13.9, 23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 18.0, 45.0, 160.0, 20.32, 4.43);
    dailyForecastDAO.insert(relevantForecast);

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size()+1, dailyForecastDAO.getAll().size(), "There should be "+(dailyForecastsTemplate.size()+1)+" daily forecasts");

    // Delete the location
    locationDAO.delete(testLocation);

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(), "There should be "+dailyForecastsTemplate.size()+" daily forecasts");

  }


}
