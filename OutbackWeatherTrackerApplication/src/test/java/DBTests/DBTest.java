package DBTests;

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
  static List<HourlyForecast.HourlyForecastBuilder> hourlyForecastsTemplates = new ArrayList<>();
  static List<DailyForecast.DailyForecastBuilder> dailyForecastsTemplates = new ArrayList<>();
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
    hourlyForecastsTemplates.clear();
    hourlyForecastsTemplates.add(HourlyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725321600).temperature_2m(20.8).relative_humidity_2m(50.0).dew_point_2m(10.0).apparent_temperature(20.0).precipitation(0.0).rain(0.0).showers(0.0).snowfall(0.0).snow_depth(0.0).weather_code(0).pressure_msl(1026.5).surface_pressure(1023.3).cloud_cover(3.0).cloud_cover_low(0.0).cloud_cover_mid(0.0).cloud_cover_high(0.0).visibility(16040.0).et0_fao_evapotranspiration(0.42).vapour_pressure_deficit(1.23).wind_speed_10m(10.8).wind_speed_40m(15.5).wind_speed_80m(17.6).wind_speed_120m(19.4).wind_direction_10m(152.0).wind_direction_40m(152.0).wind_direction_80m(153.0).wind_direction_120m(153.0).wind_gusts_10m(28.4).surface_temperature(25.3).soil_temperature_0_to_10cm(21.3).soil_temperature_10_to_35cm(22.3).soil_temperature_35_to_100cm(21.1).soil_temperature_100_to_300cm(18.9).soil_moisture_0_to_10cm(0.354).soil_moisture_10_to_35cm(0.369).soil_moisture_35_to_100cm(0.386).soil_moisture_100_to_300cm(0.386).is_day(true).sunshine_duration(3600.0).shortwave_radiation(685.0).direct_radiation(596.0).diffuse_radiation(89.0).direct_normal_irradiance(896.4).global_tilted_irradiance(685.0).terrestrial_radiation(893.9).shortwave_radiation_instant(747.4).direct_radiation_instant(650.3).diffuse_radiation_instant(97.1).direct_normal_irradiance_instant(896.4).global_tilted_irradiance_instant(747.4).terrestrial_radiation_instant(975.3));
    hourlyForecastsTemplates.add(HourlyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725325200).temperature_2m(20.8).relative_humidity_2m(50.0).dew_point_2m(10.0).apparent_temperature(20.0).precipitation(0.0).rain(0.0).showers(0.0).snowfall(0.0).snow_depth(0.0).weather_code(0).pressure_msl(1026.5).surface_pressure(1023.3).cloud_cover(3.0).cloud_cover_low(0.0).cloud_cover_mid(0.0).cloud_cover_high(0.0).visibility(16040.0).et0_fao_evapotranspiration(0.42).vapour_pressure_deficit(1.23).wind_speed_10m(10.8).wind_speed_40m(15.5).wind_speed_80m(17.6).wind_speed_120m(19.4).wind_direction_10m(152.0).wind_direction_40m(152.0).wind_direction_80m(153.0).wind_direction_120m(153.0).wind_gusts_10m(28.4).surface_temperature(25.3).soil_temperature_0_to_10cm(21.3).soil_temperature_10_to_35cm(22.3).soil_temperature_35_to_100cm(21.1).soil_temperature_100_to_300cm(18.9).soil_moisture_0_to_10cm(0.354).soil_moisture_10_to_35cm(0.369).soil_moisture_35_to_100cm(0.386).soil_moisture_100_to_300cm(0.386).is_day(true).sunshine_duration(3600.0).shortwave_radiation(685.0).direct_radiation(596.0).diffuse_radiation(89.0).direct_normal_irradiance(896.4).global_tilted_irradiance(685.0).terrestrial_radiation(893.9).shortwave_radiation_instant(747.4).direct_radiation_instant(650.3).diffuse_radiation_instant(97.1).direct_normal_irradiance_instant(896.4).global_tilted_irradiance_instant(747.4).terrestrial_radiation_instant(975.3));
    hourlyForecastsTemplates.add(HourlyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725328800).temperature_2m(20.8).relative_humidity_2m(50.0).dew_point_2m(10.0).apparent_temperature(20.0).precipitation(0.0).rain(0.0).showers(0.0).snowfall(0.0).snow_depth(0.0).weather_code(0).pressure_msl(1026.5).surface_pressure(1023.3).cloud_cover(3.0).cloud_cover_low(0.0).cloud_cover_mid(0.0).cloud_cover_high(0.0).visibility(16040.0).et0_fao_evapotranspiration(0.42).vapour_pressure_deficit(1.23).wind_speed_10m(10.8).wind_speed_40m(15.5).wind_speed_80m(17.6).wind_speed_120m(19.4).wind_direction_10m(152.0).wind_direction_40m(152.0).wind_direction_80m(153.0).wind_direction_120m(153.0).wind_gusts_10m(28.4).surface_temperature(25.3).soil_temperature_0_to_10cm(21.3).soil_temperature_10_to_35cm(22.3).soil_temperature_35_to_100cm(21.1).soil_temperature_100_to_300cm(18.9).soil_moisture_0_to_10cm(0.354).soil_moisture_10_to_35cm(0.369).soil_moisture_35_to_100cm(0.386).soil_moisture_100_to_300cm(0.386).is_day(true).sunshine_duration(3600.0).shortwave_radiation(685.0).direct_radiation(596.0).diffuse_radiation(89.0).direct_normal_irradiance(896.4).global_tilted_irradiance(685.0).terrestrial_radiation(893.9).shortwave_radiation_instant(747.4).direct_radiation_instant(650.3).diffuse_radiation_instant(97.1).direct_normal_irradiance_instant(896.4).global_tilted_irradiance_instant(747.4).terrestrial_radiation_instant(975.3));
    hourlyForecastsTemplates.add(HourlyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725332400).temperature_2m(20.8).relative_humidity_2m(50.0).dew_point_2m(10.0).apparent_temperature(20.0).precipitation(0.0).rain(0.0).showers(0.0).snowfall(0.0).snow_depth(0.0).weather_code(0).pressure_msl(1026.5).surface_pressure(1023.3).cloud_cover(3.0).cloud_cover_low(0.0).cloud_cover_mid(0.0).cloud_cover_high(0.0).visibility(16040.0).et0_fao_evapotranspiration(0.42).vapour_pressure_deficit(1.23).wind_speed_10m(10.8).wind_speed_40m(15.5).wind_speed_80m(17.6).wind_speed_120m(19.4).wind_direction_10m(152.0).wind_direction_40m(152.0).wind_direction_80m(153.0).wind_direction_120m(153.0).wind_gusts_10m(28.4).surface_temperature(25.3).soil_temperature_0_to_10cm(21.3).soil_temperature_10_to_35cm(22.3).soil_temperature_35_to_100cm(21.1).soil_temperature_100_to_300cm(18.9).soil_moisture_0_to_10cm(0.354).soil_moisture_10_to_35cm(0.369).soil_moisture_35_to_100cm(0.386).soil_moisture_100_to_300cm(0.386).is_day(true).sunshine_duration(3600.0).shortwave_radiation(685.0).direct_radiation(596.0).diffuse_radiation(89.0).direct_normal_irradiance(896.4).global_tilted_irradiance(685.0).terrestrial_radiation(893.9).shortwave_radiation_instant(747.4).direct_radiation_instant(650.3).diffuse_radiation_instant(97.1).direct_normal_irradiance_instant(896.4).global_tilted_irradiance_instant(747.4).terrestrial_radiation_instant(975.3));
    hourlyForecastsTemplates.add(HourlyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725336000).temperature_2m(20.8).relative_humidity_2m(50.0).dew_point_2m(10.0).apparent_temperature(20.0).precipitation(0.0).rain(0.0).showers(0.0).snowfall(0.0).snow_depth(0.0).weather_code(0).pressure_msl(1026.5).surface_pressure(1023.3).cloud_cover(3.0).cloud_cover_low(0.0).cloud_cover_mid(0.0).cloud_cover_high(0.0).visibility(16040.0).et0_fao_evapotranspiration(0.42).vapour_pressure_deficit(1.23).wind_speed_10m(10.8).wind_speed_40m(15.5).wind_speed_80m(17.6).wind_speed_120m(19.4).wind_direction_10m(152.0).wind_direction_40m(152.0).wind_direction_80m(153.0).wind_direction_120m(153.0).wind_gusts_10m(28.4).surface_temperature(25.3).soil_temperature_0_to_10cm(21.3).soil_temperature_10_to_35cm(22.3).soil_temperature_35_to_100cm(21.1).soil_temperature_100_to_300cm(18.9).soil_moisture_0_to_10cm(0.354).soil_moisture_10_to_35cm(0.369).soil_moisture_35_to_100cm(0.386).soil_moisture_100_to_300cm(0.386).is_day(true).sunshine_duration(3600.0).shortwave_radiation(685.0).direct_radiation(596.0).diffuse_radiation(89.0).direct_normal_irradiance(896.4).global_tilted_irradiance(685.0).terrestrial_radiation(893.9).shortwave_radiation_instant(747.4).direct_radiation_instant(650.3).diffuse_radiation_instant(97.1).direct_normal_irradiance_instant(896.4).global_tilted_irradiance_instant(747.4).terrestrial_radiation_instant(975.3));


    // Add the daily forecasts to the template
    dailyForecastsTemplates.clear();
    dailyForecastsTemplates.add(DailyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725321600).weather_code(3).temperature_2m_max(24.2).temperature_2m_min(13.9).apparent_temperature_max(23.8).apparent_temperature_min(12.4).sunrise(1725307136).sunset(1725348930).daylight_duration(41834.64).sunshine_duration(31318.34).precipitation_sum(0.0).rain_sum(0.0).snowfall_sum(0.0).precipitation_hours(0.0).wind_speed_10m_max(18.0).wind_gusts_10m_max(45.0).wind_direction_10m_dominant(160.0).shortwave_radiation_sum(20.32).et0_fao_evapotranspiration(4.43));
    dailyForecastsTemplates.add(DailyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725408000).weather_code(3).temperature_2m_max(24.2).temperature_2m_min(13.9).apparent_temperature_max(23.8).apparent_temperature_min(12.4).sunrise(1725307136).sunset(1725348930).daylight_duration(41834.64).sunshine_duration(31318.34).precipitation_sum(0.0).rain_sum(0.0).snowfall_sum(0.0).precipitation_hours(0.0).wind_speed_10m_max(18.0).wind_gusts_10m_max(45.0).wind_direction_10m_dominant(160.0).shortwave_radiation_sum(20.32).et0_fao_evapotranspiration(4.43));
    dailyForecastsTemplates.add(DailyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725494400).weather_code(3).temperature_2m_max(24.2).temperature_2m_min(13.9).apparent_temperature_max(23.8).apparent_temperature_min(12.4).sunrise(1725307136).sunset(1725348930).daylight_duration(41834.64).sunshine_duration(31318.34).precipitation_sum(0.0).rain_sum(0.0).snowfall_sum(0.0).precipitation_hours(0.0).wind_speed_10m_max(18.0).wind_gusts_10m_max(45.0).wind_direction_10m_dominant(160.0).shortwave_radiation_sum(20.32).et0_fao_evapotranspiration(4.43));
    dailyForecastsTemplates.add(DailyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725580800).weather_code(3).temperature_2m_max(24.2).temperature_2m_min(13.9).apparent_temperature_max(23.8).apparent_temperature_min(12.4).sunrise(1725307136).sunset(1725348930).daylight_duration(41834.64).sunshine_duration(31318.34).precipitation_sum(0.0).rain_sum(0.0).snowfall_sum(0.0).precipitation_hours(0.0).wind_speed_10m_max(18.0).wind_gusts_10m_max(45.0).wind_direction_10m_dominant(160.0).shortwave_radiation_sum(20.32).et0_fao_evapotranspiration(4.43));
    dailyForecastsTemplates.add(DailyForecast.builder().location(locationsTemplate.get(0)).timestamp(1725667200).weather_code(3).temperature_2m_max(24.2).temperature_2m_min(13.9).apparent_temperature_max(23.8).apparent_temperature_min(12.4).sunrise(1725307136).sunset(1725348930).daylight_duration(41834.64).sunshine_duration(31318.34).precipitation_sum(0.0).rain_sum(0.0).snowfall_sum(0.0).precipitation_hours(0.0).wind_speed_10m_max(18.0).wind_gusts_10m_max(45.0).wind_direction_10m_dominant(160.0).shortwave_radiation_sum(20.32).et0_fao_evapotranspiration(4.43));
  }

  @AfterEach
  public void cleanup() {
    addTemplates();
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
    for (HourlyForecast.HourlyForecastBuilder hourlyForecastBuilder : hourlyForecastsTemplates) {
      HourlyForecast hourlyForecast = hourlyForecastBuilder.build();
      hourlyForecastDAO.insert(hourlyForecast);
    }
  }

  public void addDailyForecasts() {
    // Insert the new daily forecasts
    for (DailyForecast.DailyForecastBuilder dailyForecastBuilder : dailyForecastsTemplates) {
      DailyForecast dailyForecast = dailyForecastBuilder.build();
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