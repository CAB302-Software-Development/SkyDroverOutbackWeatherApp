import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;

@Execution(SAME_THREAD)
@Timeout(value = 10000, unit = TimeUnit.MILLISECONDS) // no test should take longer than 10 seconds
public class HourlyForecastDAOTest extends DBTest {

  @Test
  public void testHourlyForecastsGetAllEmpty() {
    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecasts.size(), "Hourly Forecasts should be empty");
  }

  @Test
  public void testAddForecasts() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecasts.size(),
        "There should be " + hourlyForecastsTemplate.size() + " hourly forecasts");

    // Verify that the forecasts got assigned an ID and that they're unique
    List<Integer> seenIds = new ArrayList<>();
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      assertFalse(seenIds.contains(hourlyForecast.getId()), "Hourly Forecast ID should be unique");
      seenIds.add(hourlyForecast.getId());
    }
  }

  @Test
  public void testAddForecastWithoutLocation() {
    addAccounts();

    try {
      // Insert the hourly forecasts without adding valid locations
      addHourlyForecasts();
    } catch (Exception e) {
    }

    // Verify that the invalid forecasts were not added
    assertEquals(0, hourlyForecastDAO.getAll().size(), "There should be 0 hourly forecasts");
  }

  @Test
  void testDeleteForecast() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(),
        "There should be " + hourlyForecastsTemplate.size() + " hourly forecasts");

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Delete the hourly forecasts
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      hourlyForecastDAO.delete(hourlyForecast);
    }

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be empty");
  }

  @Test
  void testDeleteForecastById() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(),
        "There should be " + hourlyForecastsTemplate.size() + " hourly forecasts");

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Delete the hourly forecasts
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      hourlyForecastDAO.delete(hourlyForecast.getId());
    }

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be empty");
  }

  @Test
  void testUniqueForecast() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Try to insert the same hourly forecasts
    // assert that an exception is thrown
    assertThrows(Exception.class, this::addHourlyForecasts);

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(),
        "There should be " + hourlyForecastsTemplate.size() + " hourly forecasts");
  }

  @Test
  void testGetForecastByID() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    for (HourlyForecast hourlyForecast : hourlyForecastsTemplate) {

      // Retrieve the hourly forecast
      HourlyForecast hourlyForecast_result = hourlyForecastDAO.getById(hourlyForecast.getId());

      // Verify the hourly forecast
      assertEquals(hourlyForecast.getId(), hourlyForecast_result.getId());
    }
  }

  @Test
  void testGetForecastsForLocation() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = new HourlyForecast(testLocation, 1725321600, 20.8, 50.0, 10.0,
        20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 1026.5, 1023.3, 3.0, 0.0, 0.0, 0.0, 16040.0, 0.42, 1.23,
        10.8, 15.5, 17.6, 19.4, 152.0, 152.0, 153.0, 153.0, 28.4, 25.3, 21.3, 22.3, 21.1, 18.9,
        0.354, 0.369, 0.386, 0.386, true, 3600.0, 685.0, 596.0, 89.0, 896.4, 685.0, 893.9, 747.4,
        650.3, 97.1, 896.4, 747.4, 975.3);
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size() + 1, hourlyForecasts.size(),
        "There should be " + (hourlyForecastsTemplate.size() + 1) + " hourly forecast");

    // Retrieve test location forecasts
    List<HourlyForecast> testForecasts = hourlyForecastDAO.getByLocation(testLocation);

    // Verify the hourly forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 hourly forecast");
  }

  @Test
  void testGetForecastsForLocationById() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = new HourlyForecast(testLocation, 1725321600, 20.8, 50.0, 10.0,
        20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 1026.5, 1023.3, 3.0, 0.0, 0.0, 0.0, 16040.0, 0.42, 1.23,
        10.8, 15.5, 17.6, 19.4, 152.0, 152.0, 153.0, 153.0, 28.4, 25.3, 21.3, 22.3, 21.1, 18.9,
        0.354, 0.369, 0.386, 0.386, true, 3600.0, 685.0, 596.0, 89.0, 896.4, 685.0, 893.9, 747.4,
        650.3, 97.1, 896.4, 747.4, 975.3);
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size() + 1, hourlyForecasts.size(),
        "There should be " + (hourlyForecastsTemplate.size() + 1) + " hourly forecast");

    // Retrieve test location forecasts
    List<HourlyForecast> testForecasts = hourlyForecastDAO.getByLocationId(testLocation.getId());

    // Verify the hourly forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 hourly forecast");
  }

  @Test
  void testLocationDeleteCascade() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = new HourlyForecast(testLocation, 1725321600, 20.8, 50.0, 10.0,
        20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 1026.5, 1023.3, 3.0, 0.0, 0.0, 0.0, 16040.0, 0.42, 1.23,
        10.8, 15.5, 17.6, 19.4, 152.0, 152.0, 153.0, 153.0, 28.4, 25.3, 21.3, 22.3, 21.1, 18.9,
        0.354, 0.369, 0.386, 0.386, true, 3600.0, 685.0, 596.0, 89.0, 896.4, 685.0, 893.9, 747.4,
        650.3, 97.1, 896.4, 747.4, 975.3);
    hourlyForecastDAO.insert(relevantForecast);

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size() + 1, hourlyForecastDAO.getAll().size(),
        "There should be " + (hourlyForecastsTemplate.size() + 1) + " hourly forecasts");

    // Delete the location
    locationDAO.delete(testLocation);

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplate.size(), hourlyForecastDAO.getAll().size(),
        "There should be " + hourlyForecastsTemplate.size() + " hourly forecasts");

  }

  @Test
  void testUpdateForecast() {
    addAccounts();
    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecasts.size(), "Hourly Forecasts should be empty");

    // Use a test location
    Location testLocation = locationsTemplate.get(1);
    locationDAO.insert(testLocation);

    HourlyForecast originalForecast = new HourlyForecast(testLocation, 1725321600, 20.8, 50.0, 10.0,
        20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 1026.5, 1023.3, 3.0, 0.0, 0.0, 0.0, 16040.0, 0.42, 1.23,
        10.8, 15.5, 17.6, 19.4, 152.0, 152.0, 153.0, 153.0, 28.4, 25.3, 21.3, 22.3, 21.1, 18.9,
        0.354, 0.369, 0.386, 0.386, true, 3600.0, 685.0, 596.0, 89.0, 896.4, 685.0, 893.9, 747.4,
        650.3, 97.1, 896.4, 747.4, 975.3);
    hourlyForecastDAO.insert(originalForecast);

    // Retrieve the hourly forecasts
    hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify that the forecast was added
    assertEquals(1, hourlyForecasts.size(), "There should be 1 hourly forecast");

    HourlyForecast updatedForecast = new HourlyForecast(testLocation, 1725321601, 20.8, 50.0, 10.0,
        20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 1026.5, 1023.3, 3.0, 0.0, 0.0, 0.0, 16040.0, 0.42, 1.23,
        10.8, 15.5, 17.6, 19.4, 152.0, 152.0, 153.0, 153.0, 28.4, 25.3, 21.3, 22.3, 21.1, 18.9,
        0.354, 0.369, 0.386, 0.386, true, 3600.0, 685.0, 596.0, 89.0, 896.4, 685.0, 893.9, 747.4,
        650.3, 97.1, 896.4, 747.4, 975.3);
    updatedForecast.setId(hourlyForecasts.get(0).getId());
    hourlyForecastDAO.update(updatedForecast);

    // Retrieve the hourly forecasts
    hourlyForecasts = hourlyForecastDAO.getAll();

    // verify that the timestamp was updated
    assertEquals(updatedForecast.getTimestamp(), hourlyForecasts.get(0).getTimestamp());
  }


}
