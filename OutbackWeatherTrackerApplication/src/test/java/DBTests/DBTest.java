package DBTests;

import static org.junit.jupiter.api.Assertions.*;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

@Timeout(value = 10000, unit = TimeUnit.MILLISECONDS) // No test should take longer than 10 seconds
abstract class DBTest {
  static List<Location> locationsTemplate = new ArrayList<>();
  static List<HourlyForecast> hourlyForecastsTemplate = new ArrayList<>();
  static List<DailyForecast> dailyForecastsTemplate = new ArrayList<>();
  static List<Account> accountsTemplate = new ArrayList<>();
  static AccountDAO accountDAO = new AccountDAO();
  static HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
  static DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();
  static LocationDAO locationDAO = new LocationDAO();

  private static Object defaultBuilder(Random random,Object builder){
    // I dont like reflection but its necessary to avoid a 200 line method
    Method[] methods = builder.getClass().getMethods();
    for (Method method : methods) {
      // If the method returns the builder, it is most likely a setter
      if (method.getReturnType() != builder.getClass()) continue;

      // Get the parameter type
      Class<?>[] parameterTypes = method.getParameterTypes();

      // The method should have only one parameter otherwise it is not a setter
      if (parameterTypes.length != 1) continue;

      // If the parameter is an int, set it to a random value
      try {
      if (parameterTypes[0] == Integer.class){
        builder = method.invoke(builder, random.nextInt(100)); // 0 - 100
      }
      // If the parameter is a double, set it to a random value
      else if (parameterTypes[0] == Double.class){
        builder = method.invoke(builder, random.nextDouble()+random.nextInt(99)); // 0 - 100
      }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
    return builder;
  }

  public static HourlyForecast.HourlyForecastBuilder defaultHourlyForecastBuilder(Random random){
    HourlyForecast.HourlyForecastBuilder builder =  HourlyForecast.builder();
    builder = (HourlyForecast.HourlyForecastBuilder) defaultBuilder(random,builder);
    builder.timestamp(random.nextInt(24*100000) * (60*60)); // 100000 different days, a collision is unlikely
    return builder;
  }

  public static HourlyForecast.HourlyForecastBuilder defaultHourlyForecastBuilder(){
    return defaultHourlyForecastBuilder(new Random(0));
  }

  public static DailyForecast.DailyForecastBuilder defaultDailyForecastBuilder(Random random){
    DailyForecast.DailyForecastBuilder builder =  DailyForecast.builder();
    builder = (DailyForecast.DailyForecastBuilder) defaultBuilder(random,builder);

    builder.timestamp(random.nextInt(100000) * (60*60*24)); // 100000 different days, a collision is unlikely
    return builder;
  }

  public static DailyForecast.DailyForecastBuilder defaultDailyForecastBuilder(){
    return defaultDailyForecastBuilder(new Random(0));
  }


    @BeforeAll
  public static void addTemplates(){
    // Create the database connection
    Session session = DatabaseConnection.getSession();

    // Verify the connection
    assertTrue(session.isConnected());

    // Verify that the connection is in memory
    assertTrue(
        session.getSessionFactory().getProperties().get("hibernate.connection.url").toString()
            .contains("jdbc:h2:mem:db1"));
    session.close();

    // Add the accounts to the template
    accountsTemplate.clear();
    accountsTemplate.add(Account.builder()
        .username("test1")
        .email("test1@gmail.com")
        .password("password")
        .id(UUID.randomUUID().toString())
        .build()
    );
    accountsTemplate.add(Account.builder()
        .username("test2")
        .email("test2@gmail.com")
        .password("password")
        .preferCelsius(false)
        .id(UUID.randomUUID().toString())
        .build()
    );
    accountsTemplate.add(Account.builder()
        .username("test3")
        .email("test3@gmail.com")
        .password("password")
        .id(UUID.randomUUID().toString())
        .build()
    );

    // Add the locations to the template
    locationsTemplate.clear();
    locationsTemplate.add(new Location(accountsTemplate.get(0), 153.02333324, -27.467331464, 27.0,
        "Brisbane")); // brisbane
    locationsTemplate.add(new Location(accountsTemplate.get(0), 153.06064, -27.58003, 58.0,
        "Sunnybank")); // sunnybank
    locationsTemplate.add(
        new Location(accountsTemplate.get(0), 153.0246, -27.53436, 48.0, "Moorooka")); // Moorooka
    locationsTemplate.add(new Location(accountsTemplate.get(0), 153.10236, -27.50578, 14.0,
        "Carindale")); // Carindale
    locationsTemplate.add(
        new Location(accountsTemplate.get(0), 152.9, -27.5, 114.0, "Brookfield")); // Brookfield
    locationsTemplate.add(
        new Location(accountsTemplate.get(0), 130.9889, -25.2406, 507.0, "Yulara")); // Yulara

    // Add the hourly forecasts to the template
    hourlyForecastsTemplate.clear();
    // To ensure consistent results between test runs
    Random random = new Random(123456789);
    for (int i = 0; i < 10; i++) {
      hourlyForecastsTemplate.add(
          defaultHourlyForecastBuilder(random).location(locationsTemplate.get(random.nextInt(locationsTemplate.size()))).build());
    }
    // Add the daily forecasts to the template
    dailyForecastsTemplate.clear();
    // To ensure consistent results between test runs
    random = new Random(123456789);
    for (int i = 0; i < 10; i++) {
      dailyForecastsTemplate.add(
          defaultDailyForecastBuilder(random).location(locationsTemplate.get(random.nextInt(locationsTemplate.size()))).build());
    }
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
      locationDAO.delete(location);
    }

    // Verify the locations
    assertEquals(0, locationDAO.getAll().size(), "Locations should be empty");

    // Retrieve the accounts
    List<Account> accounts = accountDAO.getAll();

    // Delete the accounts
    for (Account account : accounts) {
      accountDAO.delete(account.getId());
    }

    // Verify the accounts
    assertEquals(0, accountDAO.getAll().size(), "Accounts should be empty");

    session.close();
  }


  public void addAccounts() {
    // Insert the new accounts
    for (Account account : accountsTemplate) {
      accountDAO.insert(account);
    }
  }

  public void addHourlyForecasts() {
    // Insert the new hourly forecasts
    for (HourlyForecast hourlyForecast : hourlyForecastsTemplate) {
      hourlyForecastDAO.insert(hourlyForecast);
    }
  }

  public void addDailyForecasts() {
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

}