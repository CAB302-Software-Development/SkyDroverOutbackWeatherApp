import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;

@Execution(SAME_THREAD)
@Timeout(value = 10000, unit = TimeUnit.MILLISECONDS) // no test should take longer than 10 seconds
public class DailyForecastDAOTest extends DBTest {

  public void verifyForecasts() {
    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(),
        "Daily Forecasts should be " + dailyForecastsTemplate.size());
  }

  public void verifyLocations() {
    // Verify the locations
    assertEquals(locationsTemplate.size(), locationDAO.getAll().size(),
        "Locations should be " + locationsTemplate.size());
  }

  @Test
  public void testDailyForecastsGetAllEmpty() {

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(0, dailyForecasts.size(), "Daily Forecasts should be empty");
  }

  @Test
  public void testAddForecasts() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();
    // Verify
    verifyLocations();
    verifyForecasts();

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify that the forecasts got assigned an ID and that they're unique
    List<Integer> seenIds = new ArrayList<>();
    for (DailyForecast dailyForecast : dailyForecasts) {
      assertFalse(seenIds.contains(dailyForecast.getId()), "Daily Forecast ID should be unique");
      seenIds.add(dailyForecast.getId());
    }
  }

  @Test
  public void testAddForecastWithoutLocation() {
    addAccounts();
    try {
      // Insert the daily forecasts without adding valid locations
      addDailyForecasts();
    } catch (Exception e) {
    }

    // Verify that the invalid forecasts were not added
    assertEquals(0, dailyForecastDAO.getAll().size(), "There should be 0 daily forecasts");
  }

  @Test
  void testDeleteForecast() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();
    // Verify
    verifyLocations();
    verifyForecasts();

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Delete the daily forecasts
    for (DailyForecast dailyForecast : dailyForecasts) {
      dailyForecastDAO.delete(dailyForecast);
    }

    // Verify the daily forecasts
    assertEquals(0, dailyForecastDAO.getAll().size(), "Daily Forecasts should be empty");
  }

  @Test
  void testDeleteForecastById() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();
    // Verify
    verifyLocations();
    verifyForecasts();

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Delete the daily forecasts
    for (DailyForecast dailyForecast : dailyForecasts) {
      dailyForecastDAO.delete(dailyForecast.getId());
    }

    // Verify the daily forecasts
    assertEquals(0, dailyForecastDAO.getAll().size(), "Daily Forecasts should be empty");
  }

  @Test
  void testUniqueForecast() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();
    // Verify
    verifyLocations();
    verifyForecasts();

    // Try to insert the same daily forecasts
    assertThrows(Exception.class, this::addDailyForecasts);

    // Verify 
    verifyForecasts();
  }

  @Test
  void testGetForecastByID() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();

    for (DailyForecast dailyForecast : dailyForecastsTemplate) {

      // Retrieve the daily forecast
      DailyForecast dailyForecast_result = dailyForecastDAO.getById(dailyForecast.getId());

      // Verify the daily forecast
      assertEquals(dailyForecast.getId(), dailyForecast_result.getId());
    }
  }

  @Test
  void testGetForecastsForLocation() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    DailyForecast relevantForecast = new DailyForecast(testLocation, 1725321600, 3, 24.2, 13.9,
        23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0,
        18.0, 45.0, 160.0, 20.32, 4.43);
    dailyForecastDAO.insert(relevantForecast);

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size() + 1, dailyForecasts.size(),
        "There should be " + (dailyForecastsTemplate.size() + 1) + " daily forecasts");

    // Retrieve test location forecasts
    List<DailyForecast> testForecasts = dailyForecastDAO.getByLocation(testLocation);

    // Verify the daily forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 daily forecast");
  }

  @Test
  void testGetForecastsForLocationById() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    DailyForecast relevantForecast = new DailyForecast(testLocation, 1725321600, 3, 24.2, 13.9,
        23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0,
        18.0, 45.0, 160.0, 20.32, 4.43);
    dailyForecastDAO.insert(relevantForecast);

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size() + 1, dailyForecasts.size(),
        "There should be " + (dailyForecastsTemplate.size() + 1) + " daily forecast");

    // Retrieve test location forecasts
    List<DailyForecast> testForecasts = dailyForecastDAO.getByLocationId(testLocation.getId());

    // Verify the daily forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 daily forecast");
  }

  @Test
  void testLocationDeleteCascade() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    DailyForecast relevantForecast = new DailyForecast(testLocation, 1725321600, 3, 24.2, 13.9,
        23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0,
        18.0, 45.0, 160.0, 20.32, 4.43);
    dailyForecastDAO.insert(relevantForecast);

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size() + 1, dailyForecastDAO.getAll().size(),
        "There should be " + (dailyForecastsTemplate.size() + 1) + " daily forecasts");

    // Delete the location
    locationDAO.delete(testLocation);

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(),
        "There should be " + dailyForecastsTemplate.size() + " daily forecasts");
  }

  @Test
  void testUpdateForecast() {
    addAccounts();

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(0, dailyForecasts.size(), "Daily Forecasts should be empty");

    // Use a test location
    Location testLocation = locationsTemplate.get(1);
    locationDAO.insert(testLocation);

    DailyForecast originalForecast = new DailyForecast(testLocation, 1725321600, 3, 24.2, 13.9,
        23.8, 12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0,
        18.0, 45.0, 160.0, 20.32, 4.43);
    dailyForecastDAO.insert(originalForecast);

    // Retrieve the daily forecasts
    dailyForecasts = dailyForecastDAO.getAll();

    // Verify that the forecast was added
    assertEquals(1, dailyForecasts.size(), "There should be 1 daily forecast");

    DailyForecast updatedForecast = new DailyForecast(testLocation, 1725321601, 3, 24.2, 13.9, 23.8,
        12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 18.0,
        45.0, 160.0, 20.32, 4.43);
    updatedForecast.setId(dailyForecasts.get(0).getId());
    dailyForecastDAO.update(updatedForecast);

    // Retrieve the daily forecasts
    dailyForecasts = dailyForecastDAO.getAll();

    // verify that the timestamp was updated
    assertEquals(updatedForecast.getTimestamp(), dailyForecasts.get(0).getTimestamp());
  }


}
