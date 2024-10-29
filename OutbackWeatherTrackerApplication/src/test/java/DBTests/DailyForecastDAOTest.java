package DBTests;

import static org.junit.jupiter.api.Assertions.*;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO.DailyForecastQuery;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
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
public class DailyForecastDAOTest extends DBTest {

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

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecasts.size(),
        "There should be " + dailyForecastsTemplate.size() + " daily forecasts");

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

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(),
        "There should be " + dailyForecastsTemplate.size() + " daily forecasts");

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

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(),
        "There should be " + dailyForecastsTemplate.size() + " daily forecasts");

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

    // Try to insert the same daily forecasts
    // assert that an exception is thrown
    assertThrows(Exception.class, this::addDailyForecasts);

    // Verify the daily forecasts
    assertEquals(dailyForecastsTemplate.size(), dailyForecastDAO.getAll().size(),
        "There should be " + dailyForecastsTemplate.size() + " daily forecasts");
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

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    for (DailyForecast dailyForecast : dailyForecasts) {
      assertNotNull(dailyForecast.getLocation(), "Location should not be null");
      List<DailyForecast> forecastsByLocation = dailyForecastDAO.getByLocation(dailyForecast.getLocation());
      for (DailyForecast locationForecast : forecastsByLocation) {
        assertEquals(dailyForecast.getLocation().getId(), locationForecast.getLocation().getId());
      }
    }
  }

  @Test
  void testGetForecastsForLocationById() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();

    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    for (DailyForecast dailyForecast : dailyForecasts) {
      assertNotNull(dailyForecast.getLocation(), "Location should not be null");
      List<DailyForecast> forecastsByLocation = dailyForecastDAO.getByLocationId(dailyForecast.getLocation().getId());
      for (DailyForecast locationForecast : forecastsByLocation) {
        assertEquals(dailyForecast.getLocation().getId(), locationForecast.getLocation().getId());
      }
    }
  }

  @Test
  void testLocationDeleteCascade() {
    // Insert the new daily forecasts
    addAccounts();
    addLocations();
    addDailyForecasts();

    for (Location location : locationDAO.getAll()) {
      // Get the daily forecasts for the location
      List<DailyForecast> dailyForecasts = dailyForecastDAO.getByLocation(location);

      int forecastCount = dailyForecastDAO.getAll().size();

      // Delete the location
      locationDAO.delete(location);

      // Verify that the daily forecasts were deleted
      assertEquals(forecastCount - dailyForecasts.size(), dailyForecastDAO.getAll().size());
    }
  }

  @Test
  void testUpdateForecast() {
    addAccounts();
    addLocations();
    addDailyForecasts();
    // Retrieve the daily forecasts
    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();

    // Verify the daily forecasts
    assertNotEquals(0, dailyForecasts.size(), "Daily Forecasts should not be empty");

    for (DailyForecast dailyForecast : dailyForecasts) {
      // Update the forecast
      DailyForecast updatedForecast = defaultDailyForecastBuilder()
          .location(dailyForecast.getLocation())
          .timestamp(dailyForecast.getTimestamp() + 1)
          .id(dailyForecast.getId())
          .build();
      dailyForecastDAO.update(updatedForecast);

      // Retrieve the daily forecast
      DailyForecast dailyForecastResult = dailyForecastDAO.getById(dailyForecast.getId());

      // Verify the daily forecast
      assertEquals(updatedForecast.getTimestamp(), dailyForecastResult.getTimestamp());
    }
  }

  @Nested
  public class DailyForecastQueryTests {
    @Test
    void testGetDailyForecastById() {
      testQueryFilterEquals(
          DailyForecastQuery::whereId,
          DailyForecast::getId
      );
    }

    @Test
    void testGetDailyForecastByLocation() {
      testQueryFilterEquals(
          DailyForecastQuery::whereLocation,
          DailyForecast::getLocation
      );
    }


    @Test
    void testGetDailyForecastByTimestamp() {
      testQueryFilterEquals(
          DailyForecastQuery::whereTimestamp,
          DailyForecast::getTimestamp
      );
    }

    @Test
    void testGetDailyForecastsByTimestampLE() {
      testQueryFilterLE(
          DailyForecastQuery::whereTimestampLE,
          DailyForecast::getTimestamp
      );
    }

    @Test
    void testGetDailyForecastsByTimestampGE() {
      testQueryFilterGE(
          DailyForecastQuery::whereTimestampGE,
          DailyForecast::getTimestamp
      );
    }

    @Test
    void testGetDailyForecastsByTimestampLEGE() {
      testQueryFilterLEGE(
          "timestamp",
          DailyForecast::getTimestamp
      );
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestGetDailyForecastsDesc() {
      addAccounts();
      addLocations();
      addDailyForecasts();
      return Arrays.stream(DailyForecast.class.getDeclaredFields())
          .filter(field -> Comparable.class.isAssignableFrom(field.getType()))
          .map(field -> DynamicTest.dynamicTest("Field: " + field.getName(),
              () -> testGetDailyForecastsOrdered(field, false)));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestGetDailyForecastsAsc() {
      addAccounts();
      addLocations();
      addDailyForecasts();
      return Arrays.stream(DailyForecast.class.getDeclaredFields())
          .filter(field -> Comparable.class.isAssignableFrom(field.getType()))
          .map(field -> DynamicTest.dynamicTest("Field: " + field.getName(),
              () -> testGetDailyForecastsOrdered(field, true)));
    }

    void testGetDailyForecastsOrdered(Field field, boolean ascending) {
      // Make the field accessible for testing purposes
      field.setAccessible(true);

      // Retrieve the daily forecast sorted in the appropriate order
      DailyForecastQuery query = new DailyForecastQuery();
      if (ascending) {
        query.addOrderAsc(field.getName());
      } else {
        query.addOrderDesc(field.getName());
      }
      List<DailyForecast> dailyForecasts = query.getResults();

      // Verify the daily forecast is not empty
      assert !dailyForecasts.isEmpty();

      // Check if the list is sorted in descending order
      for (int i = 0; i < dailyForecasts.size() - 1; i++) {
        try {
          Comparable<Object> current = (Comparable<Object>) field.get(dailyForecasts.get(i));
          Comparable<Object> next = (Comparable<Object>) field.get(dailyForecasts.get(i + 1));
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
        BiConsumer<DailyForecastQuery, T> conditionSetter,
        Function<DailyForecast, T> fieldGetter
    ) {
      // Insert the new daily forecasts
      addAccounts();
      addLocations();
      addDailyForecasts();

      for (DailyForecast dailyForecast : dailyForecastDAO.getAll()) {
        DailyForecastQuery query = new DailyForecastQuery();

        // Apply the condition to the query using the field value from dailyForecast
        conditionSetter.accept(query, fieldGetter.apply(dailyForecast));

        List<DailyForecast> dailyForecasts = query.getResults();

        assert !dailyForecasts.isEmpty();
        for (DailyForecast forecast : dailyForecasts) {
          assertEquals(fieldGetter.apply(dailyForecast), fieldGetter.apply(forecast));
        }
      }
    }

    <T extends Comparable<T>> void testQueryFilterLE(
        BiConsumer<DailyForecastQuery, T> conditionSetter,
        Function<DailyForecast, T> fieldGetter
    ) {
      // Insert the new daily forecasts
      addAccounts();
      addLocations();
      addDailyForecasts();

      for (DailyForecast dailyForecast : dailyForecastDAO.getAll()) {
        DailyForecastQuery query = new DailyForecastQuery();

        // Apply the condition to the query using the field value from dailyForecast
        conditionSetter.accept(query, fieldGetter.apply(dailyForecast));

        List<DailyForecast> dailyForecasts = query.getResults();

        assert !dailyForecasts.isEmpty();
        for (DailyForecast forecast : dailyForecasts) {
          assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(dailyForecast)) <= 0);
        }
      }
    }

    <T extends Comparable<T>> void testQueryFilterGE(
        BiConsumer<DailyForecastQuery, T> conditionSetter,
        Function<DailyForecast, T> fieldGetter
    ) {
      // Insert the new daily forecasts
      addAccounts();
      addLocations();
      addDailyForecasts();

      for (DailyForecast dailyForecast : dailyForecastDAO.getAll()) {
        DailyForecastQuery query = new DailyForecastQuery();

        // Apply the condition to the query using the field value from dailyForecast
        conditionSetter.accept(query, fieldGetter.apply(dailyForecast));

        List<DailyForecast> dailyForecasts = query.getResults();

        assert !dailyForecasts.isEmpty();
        for (DailyForecast forecast : dailyForecasts) {
          assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(dailyForecast)) >= 0);
        }
      }
    }

    <T extends Comparable<T>> void testQueryFilterLEGE( // This also tests predicate functionality
        String fieldName,
        Function<DailyForecast, T> fieldGetter
    ) {
      // Insert the new daily forecasts
      addAccounts();
      addLocations();
      addDailyForecasts();

      for (DailyForecast dailyForecast1 : dailyForecastDAO.getAll()) {
        for (DailyForecast dailyForecast2 : dailyForecastDAO.getAll()) {
          DailyForecastQuery query = new DailyForecastQuery();

          // Apply the condition to the query using the field value from dailyForecast
          query.whereFieldLE(fieldName, fieldGetter.apply(dailyForecast1));
          query.whereFieldGE(fieldName, fieldGetter.apply(dailyForecast2));

          List<DailyForecast> dailyForecasts = query.getResults();

          // If the first field is less than the second field, the result should be empty
          if (fieldGetter.apply(dailyForecast1).compareTo(fieldGetter.apply(dailyForecast2)) < 0) {
            assert dailyForecasts.isEmpty();
          } else {
            assert !dailyForecasts.isEmpty();
            // Check that the result is within the bounds
            for (DailyForecast forecast : dailyForecasts) {
              assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(dailyForecast1)) <= 0);
              assertTrue(fieldGetter.apply(forecast).compareTo(fieldGetter.apply(dailyForecast2)) >= 0);
            }
          }
        }
      }
    }
  }
}
