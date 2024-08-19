package cab302softwaredevelopment.outbackweathertrackerapplication.database.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;

public class DailyForecastDAO {

  private Connection connection;

  public DailyForecastDAO() {
    connection = DatabaseConnection.getInstance();
  }

  public void createTable() {
    try {
      Statement createTable = connection.createStatement();
      createTable.execute(
          "CREATE TABLE IF NOT EXISTS dailyForecasts ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
              + "location_id INTEGER REFERENCES locations(id) ON DELETE CASCADE, "
              + "timestamp INT NOT NULL, "
              + "weather_code INT, "
              + "temperature_2m_max DOUBLE NOT NULL, "
              + "temperature_2m_min DOUBLE NOT NULL, "
              + "apparent_temperature_max DOUBLE NOT NULL, "
              + "apparent_temperature_min DOUBLE NOT NULL, "
              + "sunrise INT, "
              + "sunset INT, "
              + "daylight_duration DOUBLE, "
              + "sunshine_duration DOUBLE, "
              + "uv_index_max DOUBLE, "
              + "uv_index_clear_sky_max DOUBLE, "
              + "precipitation_sum DOUBLE, "
              + "rain_sum DOUBLE, "
              + "showers_sum DOUBLE, "
              + "snowfall_sum DOUBLE, "
              + "precipitation_hours DOUBLE, "
              + "wind_speed_10m_max DOUBLE, "
              + "wind_gusts_10m_max DOUBLE, "
              + "wind_direction_10m_dominant DOUBLE, "
              + "shortwave_radiation_sum DOUBLE, "
              + "et0_fao_evapotranspiration DOUBLE,"
              + "UNIQUE (location_id, timestamp) ON CONFLICT REPLACE"
              + ")"
      );
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public void insert(DailyForecast dailyForecast) {
    try {
      PreparedStatement insertDailyForecast = connection.prepareStatement(
          "INSERT INTO dailyForecasts (location_id, timestamp, weather_code, temperature_2m_max, "
              + "temperature_2m_min, apparent_temperature_max, apparent_temperature_min, sunrise, "
              + "sunset, daylight_duration, sunshine_duration, uv_index_max, uv_index_clear_sky_max, "
              + "precipitation_sum, rain_sum, showers_sum, snowfall_sum, precipitation_hours, "
              + "wind_speed_10m_max, wind_gusts_10m_max, wind_direction_10m_dominant, "
              + "shortwave_radiation_sum, et0_fao_evapotranspiration) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
      );
      insertDailyForecast.setInt(1, dailyForecast.getLocation_id());
      insertDailyForecast.setInt(2, dailyForecast.getTimestamp());
      insertDailyForecast.setInt(3, dailyForecast.getWeather_code());
      insertDailyForecast.setDouble(4, dailyForecast.getTemperature_2m_max());
      insertDailyForecast.setDouble(5, dailyForecast.getTemperature_2m_min());
      insertDailyForecast.setDouble(6, dailyForecast.getApparent_temperature_max());
      insertDailyForecast.setDouble(7, dailyForecast.getApparent_temperature_min());
      insertDailyForecast.setInt(8, dailyForecast.getSunrise());
      insertDailyForecast.setInt(9, dailyForecast.getSunset());
      insertDailyForecast.setDouble(10, dailyForecast.getDaylight_duration());
      insertDailyForecast.setDouble(11, dailyForecast.getSunshine_duration());
      insertDailyForecast.setDouble(12, dailyForecast.getUv_index_max());
      insertDailyForecast.setDouble(13, dailyForecast.getUv_index_clear_sky_max());
      insertDailyForecast.setDouble(14, dailyForecast.getPrecipitation_sum());
      insertDailyForecast.setDouble(15, dailyForecast.getRain_sum());
      insertDailyForecast.setDouble(16, dailyForecast.getShowers_sum());
      insertDailyForecast.setDouble(17, dailyForecast.getSnowfall_sum());
      insertDailyForecast.setDouble(18, dailyForecast.getPrecipitation_hours());
      insertDailyForecast.setDouble(19, dailyForecast.getWind_speed_10m_max());
      insertDailyForecast.setDouble(20, dailyForecast.getWind_gusts_10m_max());
      insertDailyForecast.setDouble(21, dailyForecast.getWind_direction_10m_dominant());
      insertDailyForecast.setDouble(22, dailyForecast.getShortwave_radiation_sum());
      insertDailyForecast.setDouble(23, dailyForecast.getEt0_fao_evapotranspiration());

      insertDailyForecast.execute();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public void delete(int id) {
    try {
      PreparedStatement deleteDailyForecast = connection.prepareStatement("DELETE FROM dailyForecasts WHERE id = ?");
      deleteDailyForecast.setInt(1, id);
      deleteDailyForecast.execute();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public List<DailyForecast> getAll() {
    List<DailyForecast>dailyForecasts = new ArrayList<>();
    try {
      Statement getAll = connection.createStatement();
      ResultSet rs = getAll.executeQuery("SELECT * FROM dailyForecasts");
      while (rs.next()) {
        dailyForecasts.add(
            new DailyForecast(
                rs.getInt("id"),
                rs.getInt("location_id"),
                rs.getInt("timestamp"),
                rs.getInt("weather_code"),
                rs.getDouble("temperature_2m_max"),
                rs.getDouble("temperature_2m_min"),
                rs.getDouble("apparent_temperature_max"),
                rs.getDouble("apparent_temperature_min"),
                rs.getInt("sunrise"),
                rs.getInt("sunset"),
                rs.getDouble("daylight_duration"),
                rs.getDouble("sunshine_duration"),
                rs.getDouble("uv_index_max"),
                rs.getDouble("uv_index_clear_sky_max"),
                rs.getDouble("precipitation_sum"),
                rs.getDouble("rain_sum"),
                rs.getDouble("showers_sum"),
                rs.getDouble("snowfall_sum"),
                rs.getDouble("precipitation_hours"),
                rs.getDouble("wind_speed_10m_max"),
                rs.getDouble("wind_gusts_10m_max"),
                rs.getDouble("wind_direction_10m_dominant"),
                rs.getDouble("shortwave_radiation_sum"),
                rs.getDouble("et0_fao_evapotranspiration")
            )
        );
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return dailyForecasts;
  }

  public List<DailyForecast> getAll(int location_id) {
    List<DailyForecast>dailyForecasts = new ArrayList<>();
    try {
      Statement getAll = connection.createStatement();
      ResultSet rs = getAll.executeQuery("SELECT * FROM dailyForecasts WHERE location_id = " + location_id);
      while (rs.next()) {
        dailyForecasts.add(
            new DailyForecast(
                rs.getInt("id"),
                rs.getInt("location_id"),
                rs.getInt("timestamp"),
                rs.getInt("weather_code"),
                rs.getDouble("temperature_2m_max"),
                rs.getDouble("temperature_2m_min"),
                rs.getDouble("apparent_temperature_max"),
                rs.getDouble("apparent_temperature_min"),
                rs.getInt("sunrise"),
                rs.getInt("sunset"),
                rs.getDouble("daylight_duration"),
                rs.getDouble("sunshine_duration"),
                rs.getDouble("uv_index_max"),
                rs.getDouble("uv_index_clear_sky_max"),
                rs.getDouble("precipitation_sum"),
                rs.getDouble("rain_sum"),
                rs.getDouble("showers_sum"),
                rs.getDouble("snowfall_sum"),
                rs.getDouble("precipitation_hours"),
                rs.getDouble("wind_speed_10m_max"),
                rs.getDouble("wind_gusts_10m_max"),
                rs.getDouble("wind_direction_10m_dominant"),
                rs.getDouble("shortwave_radiation_sum"),
                rs.getDouble("et0_fao_evapotranspiration")
            )
        );
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return dailyForecasts;
  }

  public DailyForecast getById(int id) {
    try {
      PreparedStatement getDailyForecast = connection.prepareStatement("SELECT * FROM dailyForecasts WHERE id = ?");
      getDailyForecast.setInt(1, id);
      ResultSet rs = getDailyForecast.executeQuery();
      if (rs.next()) {
        return new DailyForecast(
            rs.getInt("id"),
            rs.getInt("location_id"),
            rs.getInt("timestamp"),
            rs.getInt("weather_code"),
            rs.getDouble("temperature_2m_max"),
            rs.getDouble("temperature_2m_min"),
            rs.getDouble("apparent_temperature_max"),
            rs.getDouble("apparent_temperature_min"),
            rs.getInt("sunrise"),
            rs.getInt("sunset"),
            rs.getDouble("daylight_duration"),
            rs.getDouble("sunshine_duration"),
            rs.getDouble("uv_index_max"),
            rs.getDouble("uv_index_clear_sky_max"),
            rs.getDouble("precipitation_sum"),
            rs.getDouble("rain_sum"),
            rs.getDouble("showers_sum"),
            rs.getDouble("snowfall_sum"),
            rs.getDouble("precipitation_hours"),
            rs.getDouble("wind_speed_10m_max"),
            rs.getDouble("wind_gusts_10m_max"),
            rs.getDouble("wind_direction_10m_dominant"),
            rs.getDouble("shortwave_radiation_sum"),
            rs.getDouble("et0_fao_evapotranspiration")
        );
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return null;
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }
}
