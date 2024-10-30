package DBTests;

import static org.junit.jupiter.api.Assertions.*;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO.HourlyForecastQuery;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;

@Timeout(value = 10000, unit = TimeUnit.MILLISECONDS) // No test should take longer than 10 seconds
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

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      assertNotNull(hourlyForecast.getLocation(), "Location should not be null");
      List<HourlyForecast> forecastsByLocation = hourlyForecastDAO.getByLocation(hourlyForecast.getLocation());
      for (HourlyForecast locationForecast : forecastsByLocation) {
        assertEquals(hourlyForecast.getLocation().getId(), locationForecast.getLocation().getId());
      }
    }
  }

  @Test
  void testGetForecastsForLocationById() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      assertNotNull(hourlyForecast.getLocation(), "Location should not be null");
      List<HourlyForecast> forecastsByLocation = hourlyForecastDAO.getByLocationId(hourlyForecast.getLocation().getId());
      for (HourlyForecast locationForecast : forecastsByLocation) {
        assertEquals(hourlyForecast.getLocation().getId(), locationForecast.getLocation().getId());
      }
    }
  }

  @Test
  void testLocationDeleteCascade() {
    // Insert the new hourly forecasts
    addAccounts();
    addLocations();
    addHourlyForecasts();

    for (Location location : locationDAO.getAll()) {
      // Get the hourly forecasts for the location
      List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getByLocation(location);

      int forecastCount = hourlyForecastDAO.getAll().size();

      // Delete the location
      locationDAO.delete(location);

      // Verify that the hourly forecasts were deleted
      assertEquals(forecastCount - hourlyForecasts.size(), hourlyForecastDAO.getAll().size());
    }
  }

  @Test
  void testUpdateForecast() {
    addAccounts();
    addLocations();
    addHourlyForecasts();
    // Retrieve the hourly forecasts
    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();

    // Verify the hourly forecasts
    assertNotEquals(0, hourlyForecasts.size(), "Hourly Forecasts should not be empty");

    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      // Update the forecast
      HourlyForecast updatedForecast = defaultHourlyForecastBuilder()
          .location(hourlyForecast.getLocation())
          .timestamp(hourlyForecast.getTimestamp() + 1)
          .id(hourlyForecast.getId())
          .build();
      hourlyForecastDAO.update(updatedForecast);

      // Retrieve the hourly forecast
      HourlyForecast hourlyForecastResult = hourlyForecastDAO.getById(hourlyForecast.getId());

      // Verify the hourly forecast
      assertEquals(updatedForecast.getTimestamp(), hourlyForecastResult.getTimestamp());
    }
  }

  @Nested
  public class HourlyForecastQueryTests {
    @Test
    void testGetHourlyForecastById() {
      testQueryFilterEquals(
          HourlyForecastQuery::whereId,
          HourlyForecast::getId
      );
    }

    @Test
    void testGetHourlyForecastByLocation() {
      testQueryFilterEquals(
          HourlyForecastQuery::whereLocation,
          HourlyForecast::getLocation
      );
    }


    @Test
    void testGetHourlyForecastByTimestamp() {
      testQueryFilterEquals(
          HourlyForecastQuery::whereTimestamp,
          HourlyForecast::getTimestamp
      );
    }

    @Test
    void testGetHourlyForecastsByTimestampLE() {
      testQueryFilterLE(
          HourlyForecastQuery::whereTimestampLE,
          HourlyForecast::getTimestamp
      );
    }

    @Test
    void testGetHourlyForecastsByTimestampGE() {
      testQueryFilterGE(
          HourlyForecastQuery::whereTimestampGE,
          HourlyForecast::getTimestamp
      );
    }

    @Test
    void testGetHourlyForecastsByTimestampLEGE() {
      testQueryFilterLEGE(
          "timestamp",
          HourlyForecast::getTimestamp
      );
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestGetHourlyForecastsDesc() {
      addAccounts();
      addLocations();
      addHourlyForecasts();
      return Arrays.stream(HourlyForecast.class.getDeclaredFields())
          .filter(field -> Comparable.class.isAssignableFrom(field.getType()))
          .map(field -> DynamicTest.dynamicTest("Field: " + field.getName(),
              () -> testGetHourlyForecastsOrdered(field, false)));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestGetHourlyForecastsAsc() {
      addAccounts();
      addLocations();
      addHourlyForecasts();
      return Arrays.stream(HourlyForecast.class.getDeclaredFields())
          .filter(field -> Comparable.class.isAssignableFrom(field.getType()))
          .map(field -> DynamicTest.dynamicTest("Field: " + field.getName(),
              () -> testGetHourlyForecastsOrdered(field, true)));
    }

    void testGetHourlyForecastsOrdered(Field field, boolean ascending) {
      // Make the field accessible for testing purposes
      field.setAccessible(true);

      // Retrieve the hourly forecast sorted in the appropriate order
      HourlyForecastQuery query = new HourlyForecastQuery();
      if (ascending) {
        query.addOrderAsc(field.getName());
      } else {
        query.addOrderDesc(field.getName());
      }
      List<HourlyForecast> hourlyForecasts = query.getResults();

      // Verify the hourly forecast is not empty
      assert !hourlyForecasts.isEmpty();

      // Check if the list is sorted in descending order
      for (int i = 0; i < hourlyForecasts.size() - 1; i++) {
        try {
          Comparable<Object> current = (Comparable<Object>) field.get(hourlyForecasts.get(i));
          Comparable<Object> next = (Comparable<Object>) field.get(hourlyForecasts.get(i + 1));
          if (ascending) {
            assertTrue(current.compareTo(next) <= 0);
          } else {
            assertTrue(current.compareTo(next) >= 0);
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }

    }
    <T> void testQueryFilterEquals(
        BiConsumer<HourlyForecastQuery, T> conditionSetter,
        Function<HourlyForecast, T> fieldGetter
    ) {
      // Insert the new hourly forecasts
      addAccounts();
      addLocations();
      addHourlyForecasts();

      for (HourlyForecast hourlyForecast : hourlyForecastDAO.getAll()) {
        HourlyForecastQuery query = new HourlyForecastQuery();

        // Apply the condition to the query using the field value from hourlyForecast
        conditionSetter.accept(query, fieldGetter.apply(hourlyForecast));

        List<HourlyForecast> hourlyForecasts = query.getResults();

        assert !hourlyForecasts.isEmpty();
        for (HourlyForecast forecast : hourlyForecasts) {
          assertEquals(fieldGetter.apply(hourlyForecast), fieldGetter.apply(forecast));
        }
      }
    }

    <T extends Comparable<T>> void testQueryFilterLE(
        BiConsumer<HourlyForecastQuery, T> conditionSetter,
        Function<HourlyForecast, T> fieldGetter
    ) {
      // Insert the new hourly forecasts
      addAccounts();
      addLocations();
      addHourlyForecasts();

      for (HourlyForecast hourlyForecast : hourlyForecastDAO.getAll()) {
        HourlyForecastQuery query = new HourlyForecastQuery();

        // Apply the condition to the query using the field value from hourlyForecast
        conditionSetter.accept(query, fieldGetter.apply(hourlyForecast));

        List<HourlyForecast> hourlyForecasts = query.getResults();

        assert !hourlyForecasts.isEmpty();
        for (HourlyForecast forecast : hourlyForecasts) {
          assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(hourlyForecast)) <= 0);
        }
      }
    }

    <T extends Comparable<T>> void testQueryFilterGE(
        BiConsumer<HourlyForecastQuery, T> conditionSetter,
        Function<HourlyForecast, T> fieldGetter
    ) {
      // Insert the new hourly forecasts
      addAccounts();
      addLocations();
      addHourlyForecasts();

      for (HourlyForecast hourlyForecast : hourlyForecastDAO.getAll()) {
        HourlyForecastQuery query = new HourlyForecastQuery();

        // Apply the condition to the query using the field value from hourlyForecast
        conditionSetter.accept(query, fieldGetter.apply(hourlyForecast));

        List<HourlyForecast> hourlyForecasts = query.getResults();

        assert !hourlyForecasts.isEmpty();
        for (HourlyForecast forecast : hourlyForecasts) {
          assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(hourlyForecast)) >= 0);
        }
      }
    }

    <T extends Comparable<T>> void testQueryFilterLEGE( // This also tests predicate functionality
        String fieldName,
        Function<HourlyForecast, T> fieldGetter
    ) {
      // Insert the new hourly forecasts
      addAccounts();
      addLocations();
      addHourlyForecasts();

      for (HourlyForecast hourlyForecast1 : hourlyForecastDAO.getAll()) {
        for (HourlyForecast hourlyForecast2 : hourlyForecastDAO.getAll()) {
          HourlyForecastQuery query = new HourlyForecastQuery();

          // Apply the condition to the query using the field value from hourlyForecast
          query.whereFieldLE(fieldName, fieldGetter.apply(hourlyForecast1));
          query.whereFieldGE(fieldName, fieldGetter.apply(hourlyForecast2));

          List<HourlyForecast> hourlyForecasts = query.getResults();

          // If the first field is less than the second field, the result should be empty
          if (fieldGetter.apply(hourlyForecast1).compareTo(fieldGetter.apply(hourlyForecast2)) < 0) {
            assert hourlyForecasts.isEmpty();
          } else {
            assert !hourlyForecasts.isEmpty();
            // Check that the result is within the bounds
            for (HourlyForecast forecast : hourlyForecasts) {
              assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(hourlyForecast1)) <= 0);
              assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(hourlyForecast2)) >= 0);
            }
          }
        }
      }
    }
  }
}
