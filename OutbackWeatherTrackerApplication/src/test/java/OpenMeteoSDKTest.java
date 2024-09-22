import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ_WRITE;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
@Execution(SAME_THREAD)
@Timeout(value = 100000, unit = TimeUnit.MILLISECONDS) // no test should take longer than 100 seconds
public class OpenMeteoSDKTest extends DBTest {

  private int getCurrentDayTimestamp() {
    long currentTimestamp = Instant.now().getEpochSecond();
    return Math.round(currentTimestamp / 86400f) * 86400;
  }

  @Test
  public void testHourlyForecastsGetAllEmpty() {
    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecasts.size(), "Hourly Forecasts should be empty");
  }

  @Test
  public void testDailyForecastsGetAllEmpty(){
    // Retrieve the daily forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(0, hourlyForecasts.size(), "Daily Forecasts should be empty");
  }

  @Nested
  public class GetForecastTests{
    @ParameterizedTest
    @CsvSource({
        "10, 0",
        "0,  10",
        "5,  5",
        "1,  1",
        "0,  0",
    })
    void testGetHourlyForecasts(int futureDays, int pastDays){
      // Insert the locations
      addAccounts();
      addLocations();

      // Use the sdk to get hourly forecasts for the locations
      Sdk sdk = new Sdk();

      locationsTemplate.forEach(location -> {
        List<HourlyForecast> forecasts = sdk.getHourlyForecast(location,futureDays,pastDays);
        assertEquals((futureDays + pastDays) * 24, forecasts.size());
      });
    }
    @ParameterizedTest
    @CsvSource({
        "10, 0",
        "0,  10",
        "5,  5",
        "1,  1",
        "0,  0",
    })
    void testGetDailyForecasts(int futureDays, int pastDays){
      // Insert the locations
      addAccounts();
      addLocations();

      // Use the sdk to get daily forecasts for the locations
      Sdk sdk = new Sdk();

        locationsTemplate.forEach(location -> {
          List<DailyForecast> forecasts = sdk.getDailyForecast(location,futureDays,pastDays);
          assertEquals((futureDays + pastDays), forecasts.size());
        });
    }
  }

  @Test
  void testAddHourlyForecasts() {
    // Insert the locations
    addAccounts();
    addLocations();

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be empty");

    // Use the sdk to update the hourly forecasts
    Sdk sdk = new Sdk();
    for (Location location : locationsTemplate) {
      sdk.updateHourlyForecast(location, 10, 0);
    }

    // Verify the hourly forecasts
    assertEquals(locationsTemplate.size() * 10 * 24, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be full");
  }

  @Test
  void testAddDailyForecasts() {
    // Insert the locations
    addAccounts();
    addLocations();

    // Verify the daily forecasts
    assertEquals(0, dailyForecastDAO.getAll().size(), "Daily Forecasts should be empty");

    // Use the sdk to update the daily forecasts
    Sdk sdk = new Sdk();
    for (Location location : locationsTemplate) {
      sdk.updateDailyForecast(location, 10, 0);
    }

    // Verify the daily forecasts
    assertEquals(locationsTemplate.size() * 10, dailyForecastDAO.getAll().size(), "Daily Forecasts should be full");
  }

  @Test
  void testUpdateHourlyForecasts() {
    // Insert the locations
    addAccounts();
    addLocations();

    // Verify the hourly forecasts
    assertEquals(0, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be empty");

    // Use the sdk to update the hourly forecasts
    Sdk sdk = new Sdk();
    locationsTemplate.parallelStream().forEach(location -> sdk.updateHourlyForecast(location, 10, 0));

    // Verify the hourly forecasts
    assertEquals(locationsTemplate.size() * 10 * 24, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be full");

    // Update all the forecasts to have a have a temperature of the sun (15000000.0)
    new HourlyForecastDAO.HourlyForecastQuery().getResults().parallelStream().forEach(forecast -> {
      HourlyForecast updatedForecast = new HourlyForecast(forecast.getId(),forecast.getLocation(),
          forecast.getTimestamp(), 15000000.0, 50.0, 10.0,
          20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 1026.5, 1023.3, 3.0, 0.0, 0.0, 0.0, 16040.0, 0.42, 1.23,
          10.8, 15.5, 17.6, 19.4, 152.0, 152.0, 153.0, 153.0, 28.4, 25.3, 21.3, 22.3, 21.1, 18.9,
          0.354, 0.369, 0.386, 0.386, true, 3600.0, 685.0, 596.0, 89.0, 896.4, 685.0, 893.9, 747.4,
          650.3, 97.1, 896.4, 747.4, 975.3);
      hourlyForecastDAO.update(updatedForecast);
    });

    // Verify that all the forecasts have been updated
    new HourlyForecastDAO.HourlyForecastQuery().getResults().forEach(forecast -> {
      assertEquals(15000000.0, forecast.getTemperature_2m());
    });

    // Update the hourly forecasts from the sdk
    locationsTemplate.stream().parallel().forEach(location -> sdk.updateHourlyForecast(location, 10, 0));

    // Verify that no forecasts have been added
    assertEquals(locationsTemplate.size() * 10 * 24, hourlyForecastDAO.getAll().size(), "Hourly Forecasts should be full");

    // Verify that all the forecasts have been updated
    new HourlyForecastDAO.HourlyForecastQuery().getResults().forEach(forecast -> {
      assertNotEquals(15000000.0, forecast.getTemperature_2m());
    });
  }

  @Test
  void testUpdateDailyForecasts() {
    // Insert the locations
    addAccounts();
    addLocations();

    // Verify the daily forecasts
    assertEquals(0, dailyForecastDAO.getAll().size(), "Daily Forecasts should be empty");

    // Use the sdk to update the daily forecasts
    Sdk sdk = new Sdk();
    for (Location location1 : locationsTemplate) {
      sdk.updateDailyForecast(location1, 10, 0);
    }

    // Verify the daily forecasts
    assertEquals(locationsTemplate.size() * 10, dailyForecastDAO.getAll().size(), "Daily Forecasts should be full");

    // Update all the forecasts to have a have a temperature of the sun (15000000.0)
    new DailyForecastDAO.DailyForecastQuery().getResults().forEach(forecast -> {
      DailyForecast updatedForecast = new DailyForecast(forecast.getId(),forecast.getLocation(), forecast.getTimestamp(), 3, 15000000.0, 13.9, 23.8,
          12.4, 1725307136, 1725348930, 41834.64, 31318.34, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 18.0,
          45.0, 160.0, 20.32, 4.43);
      dailyForecastDAO.update(updatedForecast);
    });

    // Verify that all the forecasts have been updated
    new DailyForecastDAO.DailyForecastQuery().getResults().forEach(forecast -> {
      assertEquals(15000000.0, forecast.getTemperature_2m_max());
    });

    // Update the daily forecasts from the sdk
    locationsTemplate.stream().parallel().forEach(location -> sdk.updateDailyForecast(location, 10, 0));

    // Verify that no forecasts have been added
    assertEquals(locationsTemplate.size() * 10, dailyForecastDAO.getAll().size(), "Daily Forecasts should be full");

    // Verify that all the forecasts have been updated
    new DailyForecastDAO.DailyForecastQuery().getResults().forEach(forecast -> {
      assertNotEquals(15000000.0, forecast.getTemperature_2m_max());
    });
  }

}
