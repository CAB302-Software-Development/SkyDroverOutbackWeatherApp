package DBTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
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

  public void verifyForecasts() {
    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplates.size(), hourlyForecastDAO.getAll().size(),
        "Hourly Forecasts should be " + hourlyForecastsTemplates.size());
  }

  public void verifyLocations() {
    // Verify the locations
    assertEquals(locationsTemplate.size(), locationDAO.getAll().size(),
        "Locations should be " + locationsTemplate.size());
  }

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
    // Verify
    verifyLocations();
    verifyForecasts();

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

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
    // Verify
    verifyLocations();
    verifyForecasts();

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
    // Verify
    verifyLocations();
    verifyForecasts();

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
    // Verify
    verifyLocations();
    verifyForecasts();

    // Try to insert the same hourly forecasts
    assertThrows(Exception.class, this::addHourlyForecasts);

    // Verify
    verifyForecasts();
  }

  @Test
  void testGetForecastByID() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();

    for (HourlyForecast.HourlyForecastBuilder hourlyForecastBuilder : hourlyForecastsTemplates) {
      // Build the hourly forecast
      hourlyForecastBuilder.location(locationsTemplate.get(0));

      HourlyForecast hourlyForecast = hourlyForecastBuilder.build();

      // Insert the hourly forecast
      hourlyForecastDAO.insert(hourlyForecast);

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
    HourlyForecast relevantForecast = hourlyForecastsTemplates.get(0)
        .location(testLocation)
        .build();
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplates.size() + 1, hourlyForecasts.size(),
        "There should be " + (hourlyForecastsTemplates.size() + 1) + " hourly forecasts");

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
    HourlyForecast relevantForecast = hourlyForecastsTemplates.get(0)
        .location(testLocation)
        .timestamp(1725321600)
        .build();
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplates.size() + 1, hourlyForecasts.size(),
        "There should be " + (hourlyForecastsTemplates.size() + 1) + " hourly forecast");

    // Retrieve test location forecasts
    List<HourlyForecast> testForecasts = hourlyForecastDAO.getByLocationId(testLocation.getId());

    // Verify the hourly forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 hourly forecast");
  }

  @Test
  void testGetForecastsForLocationByIdQuery(){
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = hourlyForecastsTemplates.get(0)
        .location(testLocation)
        .build();
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplates.size() + 1, hourlyForecasts.size(),
        "There should be " + (hourlyForecastsTemplates.size() + 1) + " hourly forecast");

    // Retrieve test location forecasts
    List<HourlyForecast> testForecasts = new HourlyForecastDAO.HourlyForecastQuery().whereLocationId(testLocation.getId()).getResults();


    // Verify the hourly forecasts
    assertEquals(1, testForecasts.size(), "There should be 1 hourly forecast");
  }

  @Test
  void testGetForecastsForLocationByIdAndTimestampQuery(){
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Use a test location
    Location testLocation = locationsTemplate.get(1);

    // Add relevant forecast
    HourlyForecast relevantForecast = hourlyForecastsTemplates.get(0)
        .location(testLocation)
        .build();
    hourlyForecastDAO.insert(relevantForecast);

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplates.size() + 1, hourlyForecasts.size(),
        "There should be " + (hourlyForecastsTemplates.size() + 1) + " hourly forecast");

    // Retrieve test location forecasts
    List<HourlyForecast> testForecasts = new HourlyForecastDAO.HourlyForecastQuery().whereLocationId(testLocation.getId()).whereTimestampGE(0).getResults();

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
    HourlyForecast relevantForecast = hourlyForecastsTemplates.get(0)
        .location(testLocation)
        .build();
    hourlyForecastDAO.insert(relevantForecast);

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplates.size() + 1, hourlyForecastDAO.getAll().size(),
        "There should be " + (hourlyForecastsTemplates.size() + 1) + " hourly forecasts");

    // Delete the location
    locationDAO.delete(testLocation);

    // Verify the hourly forecasts
    assertEquals(hourlyForecastsTemplates.size(), hourlyForecastDAO.getAll().size(),
        "There should be " + hourlyForecastsTemplates.size() + " hourly forecasts");
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

    HourlyForecast originalForecast = hourlyForecastsTemplates.get(0)
        .location(testLocation)
        .timestamp(1725321600)
        .build();
    hourlyForecastDAO.insert(originalForecast);

    // Retrieve the hourly forecasts
    hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify that the forecast was added
    assertEquals(1, hourlyForecasts.size(), "There should be 1 hourly forecast");

    HourlyForecast updatedForecast = hourlyForecastsTemplates.get(0)
        .location(testLocation)
        .timestamp(1725321601)
        .build();
    updatedForecast.setId(hourlyForecasts.get(0).getId());
    hourlyForecastDAO.update(updatedForecast);

    // Retrieve the hourly forecasts
    hourlyForecasts = hourlyForecastDAO.getAll();

    // verify that the timestamp was updated
    assertEquals(updatedForecast.getTimestamp(), hourlyForecasts.get(0).getTimestamp());
  }


}
