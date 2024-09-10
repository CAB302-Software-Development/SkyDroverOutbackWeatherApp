import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

@Execution(SAME_THREAD)
public class HourlyForecastTest {
  static HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
  static LocationDAO locationDAO = new LocationDAO();
  static List<Location> locationsTemplate = new ArrayList<>();
  static List<HourlyForecast> hourlyForecastsTemplate = new ArrayList<>();

  public void addForecasts() {
    // Insert the new hourly forecasts
    for (HourlyForecast hourlyForecast : hourlyForecastsTemplate) {
      hourlyForecastDAO.insert(hourlyForecast);
    }
  }
  public void addLocations() {
    // Insert the new locations
    for (Location location : locationsTemplate) {
      locationDAO.insert(location);
    }
  }

  @BeforeAll
  public static void testHourlyForecastsGetAllEmpty() {
    // Create the database connection
    Session session = DatabaseConnection.getSession();

    // Verify the connection
    assertTrue(session.isConnected());

    // Verify that the connection is in memory
    assertTrue(
        session.getSessionFactory().getProperties().get("hibernate.connection.url").toString()
            .contains("jdbc:h2:mem:db1"));

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecasts.size(), "Hourly Forecasts should be empty");

    // Add the test location

    locationsTemplate.add(new Location(153.02333324, -27.467331464, 27.0,"Brisbane")); // brisbane
    locationsTemplate.add(new Location(153.06064, -27.58003, 	58.0,"Sunnybank")); // sunnybank
    locationsTemplate.add(new Location(153.0246, 	-27.53436, 	48.0,"Moorooka")); // Moorooka
    locationsTemplate.add(new Location(	153.10236, 		-27.50578, 	14.0,"Carindale")); // Carindale
    locationsTemplate.add(new Location(	152.9, 	-27.5, 		114.0,"Brookfield")); // Brookfield
    locationsTemplate.add(new Location(130.9889, 	-25.2406, 		507.0,"Yulara")); // Yulara

    hourlyForecastsTemplate.add(new HourlyForecast(locationsTemplate.get(0), 1725321600,20.8,50.0,10.0,20.0,0.0,0.0,0.0,0.0,0.0,0,1026.5,1023.3,3.0,0.0,0.0,0.0,16040.0,0.42,1.23,10.8,15.5,17.6,19.4,152.0,152.0,153.0,153.0,28.4,25.3,21.3,22.3,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,685.0,596.0,89.0,896.4,685.0,893.9,747.4,650.3,97.1,896.4,747.4,975.3));
    hourlyForecastsTemplate.add(new HourlyForecast(locationsTemplate.get(0), 1725325200,21.8,45.0,9.4,22.1,0.0,0.0,0.0,0.0,0.0,0,1026.6,1023.4,0.0,0.0,0.0,0.0,16580.0,0.52,1.44,8.3,11.9,13.7,14.8,159.0,160.0,159.0,159.0,25.6,28.6,20.3,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,821.0,719.0,102.0,935.8,821.0,1033.0,856.5,750.1,106.4,935.8,856.5,1077.7));
    hourlyForecastsTemplate.add(new HourlyForecast(locationsTemplate.get(0), 1725328800,22.7,42.0,9.2,23.3,0.0,0.0,0.0,0.0,0.0,0,1025.5,1022.3,0.0,0.0,0.0,0.0,17100.0,0.57,1.6,7.9,11.2,12.6,13.7,167.0,165.0,163.0,165.0,25.9,31.0,21.5,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,879.0,773.0,106.0,948.1,879.0,1096.2,882.9,776.4,106.5,948.1,882.9,1101.1));
    hourlyForecastsTemplate.add(new HourlyForecast(locationsTemplate.get(0), 1725332400,23.6,37.0,8.0,23.8,0.0,0.0,0.0,0.0,0.0,0,1024.7,1021.5,0.0,0.0,0.0,0.0,17440.0,0.57,1.83,7.9,11.2,13.0,14.4,164.0,164.0,164.0,164.0,28.1,32.1,22.6,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,864.0,757.0,107.0,943.0,864.0,1079.3,835.7,732.2,103.5,943.0,835.7,1044.0));
    hourlyForecastsTemplate.add(new HourlyForecast(locationsTemplate.get(0), 1725336000,24.2,31.0,6.0,23.1,0.0,0.0,0.0,0.0,0.0,0,1024.2,1021.0,0.0,0.0,0.0,0.0,17640.0,0.53,2.08,9.4,13.3,15.5,16.2,157.0,156.0,155.0,156.0,32.8,31.6,23.6,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,777.0,675.0,102.0,923.0,777.0,983.3,719.3,624.9,94.4,923.0,719.3,910.3));

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

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Delete the hourly forecasts
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      hourlyForecastDAO.delete(hourlyForecast);
    }

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be empty");

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
  public void testAddForecasts() {
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecasts.size(), "There should be "+hourlyForecastsTemplate.size()+" hourly forecasts");

    // Verify that the forecasts got assigned an ID and that they're unique
    List<Integer> seenIds = new ArrayList<>();
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      assertFalse(seenIds.contains(hourlyForecast.getId()), "Hourly Forecast ID should be unique");
      seenIds.add(hourlyForecast.getId());
    }
  }

  @Test
  public void testAddForecastWithoutLocation() {
    Location fakeLocation = new Location(0.0, 0.0, 0.0,"Null Island");
    HourlyForecast invalidForecast = new HourlyForecast(fakeLocation, 1725321600,20.8,50.0,10.0,20.0,0.0,0.0,0.0,0.0,0.0,0,1026.5,1023.3,3.0,0.0,0.0,0.0,16040.0,0.42,1.23,10.8,15.5,17.6,19.4,152.0,152.0,153.0,153.0,28.4,25.3,21.3,22.3,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,685.0,596.0,89.0,896.4,685.0,893.9,747.4,650.3,97.1,896.4,747.4,975.3);

    try{
      // Insert the new hourly forecast
      hourlyForecastDAO.insert(invalidForecast);
    } catch (Exception e) {}

    // Verify that the invalid forecast was not added
    assertEquals(0, hourlyForecastDAO.getAll().size(), "There should be 0 hourly forecasts");
  }

  @Test void testDeleteForecast() {
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(), "There should be "+hourlyForecastsTemplate.size()+" hourly forecasts");

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Delete the hourly forecasts
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      hourlyForecastDAO.delete(hourlyForecast);
    }

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be empty");
  }

  @Test void testDeleteForecastById(){
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(), "There should be "+hourlyForecastsTemplate.size()+" hourly forecasts");

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Delete the hourly forecasts
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      hourlyForecastDAO.delete(hourlyForecast.getId());
    }

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be empty");
  }

  @Test void testUniqueForecast(){
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    // Try to insert the same hourly forecasts
    addForecasts();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(), "There should be "+hourlyForecastsTemplate.size()+" hourly forecasts");
  }

  @Test void testGetForecastByID() {
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    for (HourlyForecast hourlyForecast : hourlyForecastsTemplate) {

      // Retrieve the hourly forecast
      HourlyForecast hourlyForecast_result = hourlyForecastDAO.getById(hourlyForecast.getId());

      // Verify the hourly forecast
      assertEquals(hourlyForecast.getId(), hourlyForecast_result.getId());
    }
  }

  @Test void testGetForecastsForLocation() {
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = new HourlyForecast(testLocation, 1725321600,20.8,50.0,10.0,20.0,0.0,0.0,0.0,0.0,0.0,0,1026.5,1023.3,3.0,0.0,0.0,0.0,16040.0,0.42,1.23,10.8,15.5,17.6,19.4,152.0,152.0,153.0,153.0,28.4,25.3,21.3,22.3,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,685.0,596.0,89.0,896.4,685.0,893.9,747.4,650.3,97.1,896.4,747.4,975.3);
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size()+1, hourlyForecasts.size(), "There should be "+(hourlyForecastsTemplate.size()+1)+" hourly forecast");

    // Retrieve test location forecasts
    List<HourlyForecast> testForecasts = hourlyForecastDAO.getByLocation(testLocation);

    // Verify the hourly forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 hourly forecast");
  }

  @Test void testGetForecastsForLocationById() {
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = new HourlyForecast(testLocation, 1725321600,20.8,50.0,10.0,20.0,0.0,0.0,0.0,0.0,0.0,0,1026.5,1023.3,3.0,0.0,0.0,0.0,16040.0,0.42,1.23,10.8,15.5,17.6,19.4,152.0,152.0,153.0,153.0,28.4,25.3,21.3,22.3,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,685.0,596.0,89.0,896.4,685.0,893.9,747.4,650.3,97.1,896.4,747.4,975.3);
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size()+1, hourlyForecasts.size(), "There should be "+(hourlyForecastsTemplate.size()+1)+" hourly forecast");

    // Retrieve test location forecasts
    List<HourlyForecast> testForecasts = hourlyForecastDAO.getByLocationId(testLocation.getId());

    // Verify the hourly forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 hourly forecast");
  }

  @Test void testLocationDeleteCascade() {
    // Insert the new hourly forecasts
    addLocations();
    addForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = new HourlyForecast(testLocation, 1725321600,20.8,50.0,10.0,20.0,0.0,0.0,0.0,0.0,0.0,0,1026.5,1023.3,3.0,0.0,0.0,0.0,16040.0,0.42,1.23,10.8,15.5,17.6,19.4,152.0,152.0,153.0,153.0,28.4,25.3,21.3,22.3,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,685.0,596.0,89.0,896.4,685.0,893.9,747.4,650.3,97.1,896.4,747.4,975.3);
    hourlyForecastDAO.insert(relevantForecast);

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size()+1, hourlyForecastDAO.getAll().size(), "There should be "+(hourlyForecastsTemplate.size()+1)+" hourly forecasts");

    // Delete the location
    locationDAO.delete(testLocation);

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(), "There should be "+hourlyForecastsTemplate.size()+" hourly forecasts");

  }


}
