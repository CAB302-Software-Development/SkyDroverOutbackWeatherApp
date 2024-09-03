package cab302softwaredevelopment.outbackweathertrackerapplication.database.DAO;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HourlyForecastDAO {

  private Connection connection;

  public HourlyForecastDAO() {
    connection = DatabaseConnection.getInstance();
  }

  public void createTable() {
    try {
      Statement createTable = connection.createStatement();
      createTable.execute(
          "CREATE TABLE IF NOT EXISTS hourlyForecasts ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
              + "location_id INTEGER REFERENCES locations(id) ON DELETE CASCADE, "
              + "timestamp INT, "
              + "temperature_2m DOUBLE, "
              + "relative_humidity_2m DOUBLE, "
              + "dew_point_2m DOUBLE, "
              + "apparent_temperature DOUBLE, "
              + "precipitation DOUBLE, "
              + "rain DOUBLE, "
              + "showers DOUBLE, "
              + "snowfall DOUBLE, "
              + "snow_depth DOUBLE, "
              + "weather_code INT, "
              + "pressure_msl DOUBLE, "
              + "surface_pressure DOUBLE, "
              + "cloud_cover DOUBLE, "
              + "cloud_cover_low DOUBLE, "
              + "cloud_cover_mid DOUBLE, "
              + "cloud_cover_high DOUBLE, "
              + "visibility DOUBLE, "
              + "et0_fao_evapotranspiration DOUBLE, "
              + "vapour_pressure_deficit DOUBLE, "
              + "wind_speed_10m DOUBLE, "
              + "wind_speed_40m DOUBLE, "
              + "wind_speed_80m DOUBLE, "
              + "wind_speed_120m DOUBLE, "
              + "wind_direction_10m DOUBLE, "
              + "wind_direction_40m DOUBLE, "
              + "wind_direction_80m DOUBLE, "
              + "wind_direction_120m DOUBLE, "
              + "wind_gusts_10m DOUBLE, "
              + "surface_temperature DOUBLE, "
              + "soil_temperature_0_to_10cm DOUBLE, "
              + "soil_temperature_10_to_35cm DOUBLE, "
              + "soil_temperature_35_to_100cm DOUBLE, "
              + "soil_temperature_100_to_300cm DOUBLE, "
              + "soil_moisture_0_to_10cm DOUBLE, "
              + "soil_moisture_10_to_35cm DOUBLE, "
              + "soil_moisture_35_to_100cm DOUBLE, "
              + "soil_moisture_100_to_300cm DOUBLE, "
              + "is_day BOOLEAN, "
              + "sunshine_duration DOUBLE, "
              + "shortwave_radiation DOUBLE, "
              + "direct_radiation DOUBLE, "
              + "diffuse_radiation DOUBLE, "
              + "direct_normal_irradiance DOUBLE, "
              + "global_tilted_irradiance DOUBLE, "
              + "terrestrial_radiation DOUBLE, "
              + "shortwave_radiation_instant DOUBLE, "
              + "direct_radiation_instant DOUBLE, "
              + "diffuse_radiation_instant DOUBLE, "
              + "direct_normal_irradiance_instant DOUBLE, "
              + "global_tilted_irradiance_instant DOUBLE, "
              + "terrestrial_radiation_instant DOUBLE, "
              + "UNIQUE (location_id, timestamp) ON CONFLICT REPLACE"
              + ")"
      );
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public void insert(HourlyForecast hourlyForecast) {
    try {
      PreparedStatement insertHourlyForecast = connection.prepareStatement(
          "INSERT INTO hourlyForecasts (location_id, timestamp, temperature_2m, relative_humidity_2m, dew_point_2m, apparent_temperature, precipitation, rain, showers, snowfall, snow_depth, weather_code, pressure_msl, surface_pressure, cloud_cover, cloud_cover_low, cloud_cover_mid, cloud_cover_high, visibility, et0_fao_evapotranspiration, vapour_pressure_deficit, wind_speed_10m, wind_speed_40m, wind_speed_80m, wind_speed_120m, wind_direction_10m, wind_direction_40m, wind_direction_80m, wind_direction_120m, wind_gusts_10m, surface_temperature, soil_temperature_0_to_10cm, soil_temperature_10_to_35cm, soil_temperature_35_to_100cm, soil_temperature_100_to_300cm, soil_moisture_0_to_10cm, soil_moisture_10_to_35cm, soil_moisture_35_to_100cm, soil_moisture_100_to_300cm, is_day, sunshine_duration, shortwave_radiation, direct_radiation, diffuse_radiation, direct_normal_irradiance, global_tilted_irradiance, terrestrial_radiation, shortwave_radiation_instant, direct_radiation_instant, diffuse_radiation_instant, direct_normal_irradiance_instant, global_tilted_irradiance_instant, terrestrial_radiation_instant) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
      );
      insertHourlyForecast.setInt(1, hourlyForecast.getLocation_id());
      insertHourlyForecast.setInt(2, hourlyForecast.getTimestamp());
      insertHourlyForecast.setDouble(3, hourlyForecast.getTemperature_2m());
      insertHourlyForecast.setDouble(4, hourlyForecast.getRelative_humidity_2m());
      insertHourlyForecast.setDouble(5, hourlyForecast.getDew_point_2m());
      insertHourlyForecast.setDouble(6, hourlyForecast.getApparent_temperature());
      insertHourlyForecast.setDouble(7, hourlyForecast.getPrecipitation());
      insertHourlyForecast.setDouble(8, hourlyForecast.getRain());
      insertHourlyForecast.setDouble(9, hourlyForecast.getShowers());
      insertHourlyForecast.setDouble(10, hourlyForecast.getSnowfall());
      insertHourlyForecast.setDouble(11, hourlyForecast.getSnow_depth());
      insertHourlyForecast.setInt(12, hourlyForecast.getWeather_code());
      insertHourlyForecast.setDouble(13, hourlyForecast.getPressure_msl());
      insertHourlyForecast.setDouble(14, hourlyForecast.getSurface_pressure());
      insertHourlyForecast.setDouble(15, hourlyForecast.getCloud_cover());
      insertHourlyForecast.setDouble(16, hourlyForecast.getCloud_cover_low());
      insertHourlyForecast.setDouble(17, hourlyForecast.getCloud_cover_mid());
      insertHourlyForecast.setDouble(18, hourlyForecast.getCloud_cover_high());
      insertHourlyForecast.setDouble(19, hourlyForecast.getVisibility());
      insertHourlyForecast.setDouble(20, hourlyForecast.getEt0_fao_evapotranspiration());
      insertHourlyForecast.setDouble(21, hourlyForecast.getVapour_pressure_deficit());
      insertHourlyForecast.setDouble(22, hourlyForecast.getWind_speed_10m());
      insertHourlyForecast.setDouble(23, hourlyForecast.getWind_speed_40m());
      insertHourlyForecast.setDouble(24, hourlyForecast.getWind_speed_80m());
      insertHourlyForecast.setDouble(25, hourlyForecast.getWind_speed_120m());
      insertHourlyForecast.setDouble(26, hourlyForecast.getWind_direction_10m());
      insertHourlyForecast.setDouble(27, hourlyForecast.getWind_direction_40m());
      insertHourlyForecast.setDouble(28, hourlyForecast.getWind_direction_80m());
      insertHourlyForecast.setDouble(29, hourlyForecast.getWind_direction_120m());
      insertHourlyForecast.setDouble(30, hourlyForecast.getWind_gusts_10m());
      insertHourlyForecast.setDouble(31, hourlyForecast.getSurface_temperature());
      insertHourlyForecast.setDouble(32, hourlyForecast.getSoil_temperature_0_to_10cm());
      insertHourlyForecast.setDouble(33, hourlyForecast.getSoil_temperature_10_to_35cm());
      insertHourlyForecast.setDouble(34, hourlyForecast.getSoil_temperature_35_to_100cm());
      insertHourlyForecast.setDouble(35, hourlyForecast.getSoil_temperature_100_to_300cm());
      insertHourlyForecast.setDouble(36, hourlyForecast.getSoil_moisture_0_to_10cm());
      insertHourlyForecast.setDouble(37, hourlyForecast.getSoil_moisture_10_to_35cm());
      insertHourlyForecast.setDouble(38, hourlyForecast.getSoil_moisture_35_to_100cm());
      insertHourlyForecast.setDouble(39, hourlyForecast.getSoil_moisture_100_to_300cm());
      insertHourlyForecast.setBoolean(40, hourlyForecast.getIs_day());
      insertHourlyForecast.setDouble(41, hourlyForecast.getSunshine_duration());
      insertHourlyForecast.setDouble(42, hourlyForecast.getShortwave_radiation());
      insertHourlyForecast.setDouble(43, hourlyForecast.getDirect_radiation());
      insertHourlyForecast.setDouble(44, hourlyForecast.getDiffuse_radiation());
      insertHourlyForecast.setDouble(45, hourlyForecast.getDirect_normal_irradiance());
      insertHourlyForecast.setDouble(46, hourlyForecast.getGlobal_tilted_irradiance());
      insertHourlyForecast.setDouble(47, hourlyForecast.getTerrestrial_radiation());
      insertHourlyForecast.setDouble(48, hourlyForecast.getShortwave_radiation_instant());
      insertHourlyForecast.setDouble(49, hourlyForecast.getDirect_radiation_instant());
      insertHourlyForecast.setDouble(50, hourlyForecast.getDiffuse_radiation_instant());
      insertHourlyForecast.setDouble(51, hourlyForecast.getDirect_normal_irradiance_instant());
      insertHourlyForecast.setDouble(52, hourlyForecast.getGlobal_tilted_irradiance_instant());
      insertHourlyForecast.setDouble(53, hourlyForecast.getTerrestrial_radiation_instant());

      insertHourlyForecast.execute();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public void delete(int id) {
    try {
      PreparedStatement deleteHourlyForecast = connection.prepareStatement(
          "DELETE FROM hourlyForecasts WHERE id = ?");
      deleteHourlyForecast.setInt(1, id);
      deleteHourlyForecast.execute();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public List<HourlyForecast> getAll() {
    List<HourlyForecast> hourlyForecasts = new ArrayList<>();
    try {
      Statement getAll = connection.createStatement();
      ResultSet rs = getAll.executeQuery("SELECT * FROM hourlyForecasts");
      while (rs.next()) {
        hourlyForecasts.add(
            mapResultSet(rs)
        );
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return hourlyForecasts;
  }

  public List<HourlyForecast> getAll(int location_id) {
    List<HourlyForecast> hourlyForecasts = new ArrayList<>();
    try {
      Statement getAll = connection.createStatement();
      ResultSet rs = getAll.executeQuery(
          "SELECT * FROM hourlyForecasts WHERE location_id = " + location_id);
      while (rs.next()) {
        hourlyForecasts.add(
            mapResultSet(rs)
        );
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return hourlyForecasts;
  }

  public HourlyForecast getById(int id) {
    try {
      PreparedStatement getHourlyForecast = connection.prepareStatement(
          "SELECT * FROM hourlyForecasts WHERE id = ?");
      getHourlyForecast.setInt(1, id);
      ResultSet rs = getHourlyForecast.executeQuery();
      if (rs.next()) {
        return mapResultSet(rs);
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return null;
  }

  private HourlyForecast mapResultSet(ResultSet rs) throws SQLException {
    return new HourlyForecast(
        rs.getInt("location_id"),
        rs.getInt("timestamp"),
        rs.getDouble("temperature_2m"),
        rs.getDouble("relative_humidity_2m"),
        rs.getDouble("dew_point_2m"),
        rs.getDouble("apparent_temperature"),
        rs.getDouble("precipitation"),
        rs.getDouble("rain"),
        rs.getDouble("showers"),
        rs.getDouble("snowfall"),
        rs.getDouble("snow_depth"),
        rs.getInt("weather_code"),
        rs.getDouble("pressure_msl"),
        rs.getDouble("surface_pressure"),
        rs.getDouble("cloud_cover"),
        rs.getDouble("cloud_cover_low"),
        rs.getDouble("cloud_cover_mid"),
        rs.getDouble("cloud_cover_high"),
        rs.getDouble("visibility"),
        rs.getDouble("et0_fao_evapotranspiration"),
        rs.getDouble("vapour_pressure_deficit"),
        rs.getDouble("wind_speed_10m"),
        rs.getDouble("wind_speed_40m"),
        rs.getDouble("wind_speed_80m"),
        rs.getDouble("wind_speed_120m"),
        rs.getDouble("wind_direction_10m"),
        rs.getDouble("wind_direction_40m"),
        rs.getDouble("wind_direction_80m"),
        rs.getDouble("wind_direction_120m"),
        rs.getDouble("wind_gusts_10m"),
        rs.getDouble("surface_temperature"),
        rs.getDouble("soil_temperature_0_to_10cm"),
        rs.getDouble("soil_temperature_10_to_35cm"),
        rs.getDouble("soil_temperature_35_to_100cm"),
        rs.getDouble("soil_temperature_100_to_300cm"),
        rs.getDouble("soil_moisture_0_to_10cm"),
        rs.getDouble("soil_moisture_10_to_35cm"),
        rs.getDouble("soil_moisture_35_to_100cm"),
        rs.getDouble("soil_moisture_100_to_300cm"),
        rs.getBoolean("is_day"),
        rs.getDouble("sunshine_duration"),
        rs.getDouble("shortwave_radiation"),
        rs.getDouble("direct_radiation"),
        rs.getDouble("diffuse_radiation"),
        rs.getDouble("direct_normal_irradiance"),
        rs.getDouble("global_tilted_irradiance"),
        rs.getDouble("terrestrial_radiation"),
        rs.getDouble("shortwave_radiation_instant"),
        rs.getDouble("direct_radiation_instant"),
        rs.getDouble("diffuse_radiation_instant"),
        rs.getDouble("direct_normal_irradiance_instant"),
        rs.getDouble("global_tilted_irradiance_instant"),
        rs.getDouble("terrestrial_radiation_instant")
    );
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }
}
