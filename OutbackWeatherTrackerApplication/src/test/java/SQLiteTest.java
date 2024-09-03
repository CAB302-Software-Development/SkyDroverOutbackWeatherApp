
import static org.junit.jupiter.api.Assertions.assertEquals;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
public class SQLiteTest {

  @BeforeAll
  public static void setUp() {

    // Create the database connection
    Session session = DatabaseConnection.getSession();

    // Verify the connection
    assertTrue(session.isConnected());

    // Verify that the connection is in memory
    assertTrue(
        session.getSessionFactory().getProperties().get("hibernate.connection.url").toString()
            .contains("jdbc:sqlite:memory"));

    session.close();
  }

  @Test
  @Order(1)
  public void testLocationsGetAllEmpty() {
    // Create the database connection
    Session session = DatabaseConnection.getSession();

    // Verify the connection
    assertTrue(session.isConnected());

    // Verify that the connection is in memory
    assertTrue(
        session.getSessionFactory().getProperties().get("hibernate.connection.url").toString()
            .contains("jdbc:sqlite:memory"));

    // Create a new location
    LocationDAO locationDAO = new LocationDAO();

    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Verify the locations
    assertEquals(0, locations.size(), "Locations should be empty");

    session.close();
  }

  @Test
  @Order(2)
  public void testHourlyForecastsGetAllEmpty() {

    // Create a new location
    HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();

    // Retrieve the locations
    List<HourlyForecast> HourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the locations
    assertEquals(0, HourlyForecasts.size(), "Hourly Forecasts should be empty");


  }

  @Test
  @Order(3)
  public void testDailyForecastsGetAllEmpty() {

    // Create a new location
    DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();

    // Retrieve the locations
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the locations
    assertEquals(0, dailyForecasts.size(), "Daily Forecasts should be empty");

  }

  @Test
  @Order(4)
  public void testAddLocations() {
    // Create a new location
    LocationDAO locationDAO = new LocationDAO();
    Location location1 = new Location(153.02333324, -27.467331464, 27.0,"Brisbane"); // brisbane
    Location location2 = new Location(153.06064, -27.58003, 	58.0,"Sunnybank"); // sunnybank
    Location location3 = new Location(153.0246, 	-27.53436, 	48.0,"Moorooka"); // Moorooka
    Location location4 = new Location(	153.10236, 		-27.50578, 	14.0,"Carindale"); // Carindale
    Location location5 = new Location(	152.9, 	-27.5, 		114.0,"Brookfield"); // Brookfield
    Location location6 = new Location(130.9889, 	-25.2406, 		507.0,"Yulara"); // Yulara

    // Insert the new location
    locationDAO.insert(location1);
    locationDAO.insert(location2);
    locationDAO.insert(location3);
    locationDAO.insert(location4);
    locationDAO.insert(location5);
    locationDAO.insert(location6);

    // Retrieve the locations
    List<Location> locations = locationDAO.getAll();

    // Verify the locations
    assertEquals(6, locations.size());

    // Verify that the locations got assigned an ID and that they increased
    int previousId = 0;
    for (Location location : locations) {
      assertEquals(location.getId(), previousId + 1, "Location ID should be increasing by 1");
      previousId = location.getId();
    }
  }

  @Test
  @Order(5)
  public void testGetLocationsByID() {
    // Create a new location
    LocationDAO locationDAO = new LocationDAO();
    Location location1 = locationDAO.getById(1); // brisbane
    Location location2 = locationDAO.getById(2); // sunnybank
    Location location3 = locationDAO.getById(3); // Moorooka
    Location location4 = locationDAO.getById(4); // Carindale
    Location location5 = locationDAO.getById(5); // Brookfield
    Location location6 = locationDAO.getById(6); // Yulara

    // Verify the locations
    assertEquals(153.02333324, location1.getLongitude());
    assertEquals(-27.467331464, location1.getLatitude());
    assertEquals("Brisbane", location1.getName());

    assertEquals(153.06064, location2.getLongitude());
    assertEquals(-27.58003, location2.getLatitude());
    assertEquals("Sunnybank", location2.getName());

    assertEquals(153.0246, location3.getLongitude());
    assertEquals(-27.53436, location3.getLatitude());
    assertEquals("Moorooka", location3.getName());

    assertEquals(153.10236, location4.getLongitude());
    assertEquals(-27.50578, location4.getLatitude());
    assertEquals("Carindale", location4.getName());

    assertEquals(152.9, location5.getLongitude());
    assertEquals(-27.5, location5.getLatitude());
    assertEquals("Brookfield", location5.getName());

    assertEquals(130.9889, location6.getLongitude());
    assertEquals(-25.2406, location6.getLatitude());
    assertEquals("Yulara", location6.getName());

  }


  @Test
  @Order(6)
  public void testAddHourlyForecast() {
    // Create a new location
    LocationDAO locationDAO = new LocationDAO();
    Location location1 = locationDAO.getById(1); // brisbane
    // Create a new hourly forecast
    HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();

    // Insert a new hourly forecast for location 1 (Brisbane)
    hourlyForecastDAO.insert(new HourlyForecast(location1, 1725321600,20.8,50.0,10.0,20.0,0.0,0.0,0.0,0.0,0.0,0,1026.5,1023.3,3.0,0.0,0.0,0.0,16040.0,0.42,1.23,10.8,15.5,17.6,19.4,152.0,152.0,153.0,153.0,28.4,25.3,21.3,22.3,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,685.0,596.0,89.0,896.4,685.0,893.9,747.4,650.3,97.1,896.4,747.4,975.3));
    hourlyForecastDAO.insert(new HourlyForecast(location1, 1725325200,21.8,45.0,9.4,22.1,0.0,0.0,0.0,0.0,0.0,0,1026.6,1023.4,0.0,0.0,0.0,0.0,16580.0,0.52,1.44,8.3,11.9,13.7,14.8,159.0,160.0,159.0,159.0,25.6,28.6,20.3,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,821.0,719.0,102.0,935.8,821.0,1033.0,856.5,750.1,106.4,935.8,856.5,1077.7));
    hourlyForecastDAO.insert(new HourlyForecast(location1, 1725328800,22.7,42.0,9.2,23.3,0.0,0.0,0.0,0.0,0.0,0,1025.5,1022.3,0.0,0.0,0.0,0.0,17100.0,0.57,1.6,7.9,11.2,12.6,13.7,167.0,165.0,163.0,165.0,25.9,31.0,21.5,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,879.0,773.0,106.0,948.1,879.0,1096.2,882.9,776.4,106.5,948.1,882.9,1101.1));
    hourlyForecastDAO.insert(new HourlyForecast(location1, 1725332400,23.6,37.0,8.0,23.8,0.0,0.0,0.0,0.0,0.0,0,1024.7,1021.5,0.0,0.0,0.0,0.0,17440.0,0.57,1.83,7.9,11.2,13.0,14.4,164.0,164.0,164.0,164.0,28.1,32.1,22.6,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,864.0,757.0,107.0,943.0,864.0,1079.3,835.7,732.2,103.5,943.0,835.7,1044.0));
    hourlyForecastDAO.insert(new HourlyForecast(location1, 1725336000,24.2,31.0,6.0,23.1,0.0,0.0,0.0,0.0,0.0,0,1024.2,1021.0,0.0,0.0,0.0,0.0,17640.0,0.53,2.08,9.4,13.3,15.5,16.2,157.0,156.0,155.0,156.0,32.8,31.6,23.6,21.9,21.1,18.9,0.354,0.369,0.386,0.386,true,3600.0,777.0,675.0,102.0,923.0,777.0,983.3,719.3,624.9,94.4,923.0,719.3,910.3));

    // Retrieve the hourly forecasts

    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(5, hourlyForecasts.size());

    // Verify that the hourly forecasts got assigned an ID and that they increased

    int previousId = 0;
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      assertEquals(hourlyForecast.getId(), previousId + 1, "Hourly Forecast ID should be increasing by 1");
      previousId = hourlyForecast.getId();
    }
  }
}
