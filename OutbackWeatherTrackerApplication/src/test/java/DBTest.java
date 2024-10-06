import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;

@Execution(SAME_THREAD)
@Timeout(value = 10000, unit = TimeUnit.MILLISECONDS) // no test should take longer than 10 seconds
abstract class DBTest {
  static List<Location> locationsTemplate = new ArrayList<>();
  static List<HourlyForecast> hourlyForecastsTemplate = new ArrayList<>();
  static List<DailyForecast> dailyForecastsTemplate = new ArrayList<>();
  static List<Account> accountsTemplate = new ArrayList<>();
  static AccountDAO accountDAO = new AccountDAO();
  static HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
  static DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();
  static LocationDAO locationDAO = new LocationDAO();

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
    accountsTemplate.add(Account.builder().email("test1@gmail.com").password("password").build());
    accountsTemplate.add(Account.builder().email("test2@gmail.com").password("password").build());
    accountsTemplate.add(Account.builder().email("test3@gmail.com").password("password").build());

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
    hourlyForecastsTemplate.add(
        new HourlyForecast(locationsTemplate.get(0), 1725321600, 20.8, 50.0, 10.0, 20.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0, 1026.5, 1023.3, 3.0, 0.0, 0.0, 0.0, 16040.0, 0.42, 1.23, 10.8, 15.5,
            17.6, 19.4, 152.0, 152.0, 153.0, 153.0, 28.4, 25.3, 21.3, 22.3, 21.1, 18.9, 0.354,
            0.369, 0.386, 0.386, true, 3600.0, 685.0, 596.0, 89.0, 896.4, 685.0, 893.9, 747.4,
            650.3, 97.1, 896.4, 747.4, 975.3));
    hourlyForecastsTemplate.add(
        new HourlyForecast(locationsTemplate.get(0), 1725325200, 21.8, 45.0, 9.4, 22.1, 0.0, 0.0,
            0.0, 0.0, 0.0, 0, 1026.6, 1023.4, 0.0, 0.0, 0.0, 0.0, 16580.0, 0.52, 1.44, 8.3, 11.9,
            13.7, 14.8, 159.0, 160.0, 159.0, 159.0, 25.6, 28.6, 20.3, 21.9, 21.1, 18.9, 0.354,
            0.369, 0.386, 0.386, true, 3600.0, 821.0, 719.0, 102.0, 935.8, 821.0, 1033.0, 856.5,
            750.1, 106.4, 935.8, 856.5, 1077.7));
    hourlyForecastsTemplate.add(
        new HourlyForecast(locationsTemplate.get(0), 1725328800, 22.7, 42.0, 9.2, 23.3, 0.0, 0.0,
            0.0, 0.0, 0.0, 0, 1025.5, 1022.3, 0.0, 0.0, 0.0, 0.0, 17100.0, 0.57, 1.6, 7.9, 11.2,
            12.6, 13.7, 167.0, 165.0, 163.0, 165.0, 25.9, 31.0, 21.5, 21.9, 21.1, 18.9, 0.354,
            0.369, 0.386, 0.386, true, 3600.0, 879.0, 773.0, 106.0, 948.1, 879.0, 1096.2, 882.9,
            776.4, 106.5, 948.1, 882.9, 1101.1));
    hourlyForecastsTemplate.add(
        new HourlyForecast(locationsTemplate.get(0), 1725332400, 23.6, 37.0, 8.0, 23.8, 0.0, 0.0,
            0.0, 0.0, 0.0, 0, 1024.7, 1021.5, 0.0, 0.0, 0.0, 0.0, 17440.0, 0.57, 1.83, 7.9, 11.2,
            13.0, 14.4, 164.0, 164.0, 164.0, 164.0, 28.1, 32.1, 22.6, 21.9, 21.1, 18.9, 0.354,
            0.369, 0.386, 0.386, true, 3600.0, 864.0, 757.0, 107.0, 943.0, 864.0, 1079.3, 835.7,
            732.2, 103.5, 943.0, 835.7, 1044.0));
    hourlyForecastsTemplate.add(
        new HourlyForecast(locationsTemplate.get(0), 1725336000, 24.2, 31.0, 6.0, 23.1, 0.0, 0.0,
            0.0, 0.0, 0.0, 0, 1024.2, 1021.0, 0.0, 0.0, 0.0, 0.0, 17640.0, 0.53, 2.08, 9.4, 13.3,
            15.5, 16.2, 157.0, 156.0, 155.0, 156.0, 32.8, 31.6, 23.6, 21.9, 21.1, 18.9, 0.354,
            0.369, 0.386, 0.386, true, 3600.0, 777.0, 675.0, 102.0, 923.0, 777.0, 983.3, 719.3,
            624.9, 94.4, 923.0, 719.3, 910.3));

    // Add the daily forecasts to the template
    dailyForecastsTemplate.clear();
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