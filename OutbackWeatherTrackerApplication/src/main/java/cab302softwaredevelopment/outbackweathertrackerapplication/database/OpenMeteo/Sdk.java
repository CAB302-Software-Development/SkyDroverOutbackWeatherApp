package cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO.DailyForecastQuery;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO.HourlyForecastQuery;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A rough Software Development Kit for the OpenMeteo API.
 */
public class Sdk {

  private HttpClient client = HttpClient.newHttpClient();

  public Sdk() {
  }

  /**
   * Retrieves the daily forecasts for a location from the OpenMeteo API.
   *
   * @param location The location to retrieve the daily forecast for.
   * @param futureDays The number of days in the future to retrieve the forecast for.
   * @param pastDays The number of days in the past to retrieve the forecast for.
   * @return A list of DailyForecast objects representing the forecast.
   */
  public List<DailyForecast> getDailyForecast(Location location, int futureDays, int pastDays) {
    double longitude = location.getLongitude();
    double latitude = location.getLatitude();
    double elevation = location.getElevation();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(
            "https://api.open-meteo.com/v1/forecast?"
                + "latitude=" + latitude
                + "&longitude=" + longitude
                + "&elevation=" + elevation
                + "&daily=weather_code"
                + ",temperature_2m_max"
                + ",temperature_2m_min"
                + ",apparent_temperature_max"
                + ",apparent_temperature_min"
                + ",sunrise"
                + ",sunset"
                + ",daylight_duration"
                + ",sunshine_duration"
                + ",uv_index_max"
                + ",uv_index_clear_sky_max"
                + ",precipitation_sum"
                + ",rain_sum"
                + ",showers_sum"
                + ",snowfall_sum"
                + ",precipitation_hours"
                + ",wind_speed_10m_max"
                + ",wind_gusts_10m_max"
                + ",wind_direction_10m_dominant"
                + ",shortwave_radiation_sum"
                + ",et0_fao_evapotranspiration"
                + "&models=bom_access_global"
                + "&forecast_days=" + futureDays
                + "&past_days=" + pastDays
                + "&timeformat=unixtime"))
        .header("accept", "application/json")
        .build();

    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    // parse as json
    JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();


    JsonObject daily = jsonObject.getAsJsonObject("daily");
    // replace all NULL values from sub-arrays with -1
    for (String key : daily.keySet()) {
      if (daily.get(key).isJsonArray()) {
        for (int i = 0; i < daily.getAsJsonArray(key).size(); i++) {
          if (daily.getAsJsonArray(key).get(i).isJsonNull()) {
            daily.getAsJsonArray(key).set(i, new JsonPrimitive(Double.NaN));
          }
        }
      }
    }

    // time represents the number of samples
    int totalEntries = daily.getAsJsonArray("time").size();
    List<DailyForecast> dailyForecasts = new ArrayList<>();
    for (int i = 0; i < totalEntries; i++) {
      DailyForecast dailyForecast = new DailyForecast(
          location,
          daily.getAsJsonArray("time").get(i).getAsInt(),
          daily.getAsJsonArray("weather_code").get(i).getAsInt(),
          daily.getAsJsonArray("temperature_2m_max").get(i).getAsDouble(),
          daily.getAsJsonArray("temperature_2m_min").get(i).getAsDouble(),
          daily.getAsJsonArray("apparent_temperature_max").get(i).getAsDouble(),
          daily.getAsJsonArray("apparent_temperature_min").get(i).getAsDouble(),
          daily.getAsJsonArray("sunrise").get(i).getAsInt(),
          daily.getAsJsonArray("sunset").get(i).getAsInt(),
          daily.getAsJsonArray("daylight_duration").get(i).getAsDouble(),
          daily.getAsJsonArray("sunshine_duration").get(i).getAsDouble(),
          daily.getAsJsonArray("uv_index_max").get(i).getAsDouble(),
          daily.getAsJsonArray("uv_index_clear_sky_max").get(i).getAsDouble(),
          daily.getAsJsonArray("precipitation_sum").get(i).getAsDouble(),
          daily.getAsJsonArray("rain_sum").get(i).getAsDouble(),
          daily.getAsJsonArray("showers_sum").get(i).getAsDouble(),
          daily.getAsJsonArray("snowfall_sum").get(i).getAsDouble(),
          daily.getAsJsonArray("precipitation_hours").get(i).getAsDouble(),
          daily.getAsJsonArray("wind_speed_10m_max").get(i).getAsDouble(),
          daily.getAsJsonArray("wind_gusts_10m_max").get(i).getAsDouble(),
          daily.getAsJsonArray("wind_direction_10m_dominant").get(i).getAsDouble(),
          daily.getAsJsonArray("shortwave_radiation_sum").get(i).getAsDouble(),
          daily.getAsJsonArray("et0_fao_evapotranspiration").get(i).getAsDouble()
      );
      dailyForecasts.add(dailyForecast);
    }
    return dailyForecasts;
  }

  /**
   * Retrieves the daily forecasts for a location from the
   * OpenMeteo API and stores them in the database.
   * This method will update any existing forecasts in the
   * database with the same location and timestamp.
   *
   * @param location The location to retrieve the daily forecast for.
   * @param futureDays The number of days in the future to retrieve the forecast for.
   * @param pastDays The number of days in the past to retrieve the forecast for.
   */
  public void updateDailyForecast(Location location, int futureDays, int pastDays) {
    List<DailyForecast> forecasts = getDailyForecast(location, futureDays, pastDays);
    DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();
    int minimumTimestamp = Collections.max(forecasts,
            Comparator.comparing(DailyForecast::getTimestamp))
        .getTimestamp();

    int maximumTimestamp = Collections.min(forecasts,
            Comparator.comparing(DailyForecast::getTimestamp))
        .getTimestamp();

    List<DailyForecast> existingForecasts = new DailyForecastQuery()
        .whereTimestampGE(minimumTimestamp)
        .whereTimestampLE(maximumTimestamp)
        .whereLocation(location)
        .getResults();

    // TODO: fix this garbage
    // Delete all daily forecasts with the same location and a timestamp within range
    for (DailyForecast forecast : existingForecasts) {
      dailyForecastDAO.delete(forecast);
    }

    // Insert all hourly forecasts
    for (DailyForecast forecast : forecasts) {
      dailyForecastDAO.insert(forecast);
    }
  }

  /**
   * Retrieves the hourly forecasts for a location from the OpenMeteo API.
   *
   * @param location The location to retrieve the hourly forecast for.
   * @param futureDays The number of days in the future to retrieve the forecast for.
   * @param pastDays The number of days in the past to retrieve the forecast for.
   * @return A list of HourlyForecast objects representing the forecast.
   */
  public List<HourlyForecast> getHourlyForecast(Location location, int futureDays, int pastDays) {
    double longitude = location.getLongitude();
    double latitude = location.getLatitude();
    double elevation = location.getElevation();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(
            "https://api.open-meteo.com/v1/forecast?"
                + "latitude=" + latitude
                + "&longitude=" + longitude
                + "&elevation=" + elevation
                + "&hourly=temperature_2m"
                + ",relative_humidity_2m"
                + ",dew_point_2m"
                + ",apparent_temperature"
                + ",precipitation"
                + ",rain,showers"
                + ",snowfall"
                + ",snow_depth"
                + ",weather_code"
                + ",pressure_msl"
                + ",surface_pressure"
                + ",cloud_cover"
                + ",cloud_cover_low"
                + ",cloud_cover_mid"
                + ",cloud_cover_high"
                + ",visibility"
                + ",et0_fao_evapotranspiration"
                + ",vapour_pressure_deficit"
                + ",wind_speed_10m"
                + ",wind_speed_40m"
                + ",wind_speed_80m"
                + ",wind_speed_120m"
                + ",wind_direction_10m"
                + ",wind_direction_40m"
                + ",wind_direction_80m"
                + ",wind_direction_120m"
                + ",wind_gusts_10m"
                + ",surface_temperature"
                + ",soil_temperature_0_to_10cm"
                + ",soil_temperature_10_to_35cm"
                + ",soil_temperature_35_to_100cm"
                + ",soil_temperature_100_to_300cm"
                + ",soil_moisture_0_to_10cm"
                + ",soil_moisture_10_to_35cm"
                + ",soil_moisture_35_to_100cm"
                + ",soil_moisture_100_to_300cm"
                + ",is_day"
                + ",sunshine_duration"
                + ",shortwave_radiation"
                + ",direct_radiation"
                + ",diffuse_radiation"
                + ",direct_normal_irradiance"
                + ",global_tilted_irradiance"
                + ",terrestrial_radiation"
                + ",shortwave_radiation_instant"
                + ",direct_radiation_instant"
                + ",diffuse_radiation_instant"
                + ",direct_normal_irradiance_instant"
                + ",global_tilted_irradiance_instant"
                + ",terrestrial_radiation_instant"
                + "&models=bom_access_global"
                + "&forecast_days=" + futureDays
                + "&past_days=" + pastDays
                + "&timeformat=unixtime"))
        .header("accept", "application/json")
        .build();

    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    // parse as json
    JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

    JsonObject hourly = jsonObject.getAsJsonObject("hourly");
    // replace all NULL values from sub-arrays with -1
    for (String key : hourly.keySet()) {
      if (hourly.get(key).isJsonArray()) {
        for (int i = 0; i < hourly.getAsJsonArray(key).size(); i++) {
          if (hourly.getAsJsonArray(key).get(i).isJsonNull()) {
            hourly.getAsJsonArray(key).set(i, new JsonPrimitive(Double.NaN));
          }
        }
      }
    }

    // time represents the number of samples
    int totalEntries = hourly.getAsJsonArray("time").size();
    List<HourlyForecast> hourlyForecasts = new ArrayList<>();
    for (int i = 0; i < totalEntries; i++) {
      HourlyForecast hourlyForecast = new HourlyForecast(
          location,
          hourly.getAsJsonArray("time").get(i).getAsInt(),
          hourly.getAsJsonArray("temperature_2m").get(i).getAsDouble(),
          hourly.getAsJsonArray("relative_humidity_2m").get(i).getAsDouble(),
          hourly.getAsJsonArray("dew_point_2m").get(i).getAsDouble(),
          hourly.getAsJsonArray("apparent_temperature").get(i).getAsDouble(),
          hourly.getAsJsonArray("precipitation").get(i).getAsDouble(),
          hourly.getAsJsonArray("rain").get(i).getAsDouble(),
          hourly.getAsJsonArray("showers").get(i).getAsDouble(),
          hourly.getAsJsonArray("snowfall").get(i).getAsDouble(),
          hourly.getAsJsonArray("snow_depth").get(i).getAsDouble(),
          hourly.getAsJsonArray("weather_code").get(i).getAsInt(),
          hourly.getAsJsonArray("pressure_msl").get(i).getAsDouble(),
          hourly.getAsJsonArray("surface_pressure").get(i).getAsDouble(),
          hourly.getAsJsonArray("cloud_cover").get(i).getAsDouble(),
          hourly.getAsJsonArray("cloud_cover_low").get(i).getAsDouble(),
          hourly.getAsJsonArray("cloud_cover_mid").get(i).getAsDouble(),
          hourly.getAsJsonArray("cloud_cover_high").get(i).getAsDouble(),
          hourly.getAsJsonArray("visibility").get(i).getAsDouble(),
          hourly.getAsJsonArray("et0_fao_evapotranspiration").get(i).getAsDouble(),
          hourly.getAsJsonArray("vapour_pressure_deficit").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_speed_10m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_speed_40m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_speed_80m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_speed_120m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_direction_10m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_direction_40m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_direction_80m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_direction_120m").get(i).getAsDouble(),
          hourly.getAsJsonArray("wind_gusts_10m").get(i).getAsDouble(),
          hourly.getAsJsonArray("surface_temperature").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_temperature_0_to_10cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_temperature_10_to_35cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_temperature_35_to_100cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_temperature_100_to_300cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_moisture_0_to_10cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_moisture_10_to_35cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_moisture_35_to_100cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("soil_moisture_100_to_300cm").get(i).getAsDouble(),
          hourly.getAsJsonArray("is_day").get(i).getAsInt()==1,
          hourly.getAsJsonArray("sunshine_duration").get(i).getAsDouble(),
          hourly.getAsJsonArray("shortwave_radiation").get(i).getAsDouble(),
          hourly.getAsJsonArray("direct_radiation").get(i).getAsDouble(),
          hourly.getAsJsonArray("diffuse_radiation").get(i).getAsDouble(),
          hourly.getAsJsonArray("direct_normal_irradiance").get(i).getAsDouble(),
          hourly.getAsJsonArray("global_tilted_irradiance").get(i).getAsDouble(),
          hourly.getAsJsonArray("terrestrial_radiation").get(i).getAsDouble(),
          hourly.getAsJsonArray("shortwave_radiation_instant").get(i).getAsDouble(),
          hourly.getAsJsonArray("direct_radiation_instant").get(i).getAsDouble(),
          hourly.getAsJsonArray("diffuse_radiation_instant").get(i).getAsDouble(),
          hourly.getAsJsonArray("direct_normal_irradiance_instant").get(i).getAsDouble(),
          hourly.getAsJsonArray("global_tilted_irradiance_instant").get(i).getAsDouble(),
          hourly.getAsJsonArray("terrestrial_radiation_instant").get(i).getAsDouble()
      );
      hourlyForecasts.add(hourlyForecast);
    }
    return hourlyForecasts;
  }

  /**
   * Retrieves the hourly forecasts for a location from the
   * OpenMeteo API and stores them in the database.
   * This method will update any existing forecasts in the
   * database with the same location and timestamp.
   *
   * @param location The location to retrieve the hourly forecast for.
   * @param futureDays The number of days in the future to retrieve the forecast for.
   * @param pastDays The number of days in the past to retrieve the forecast for.
   */
  public void updateHourlyForecast(Location location, int futureDays, int pastDays) {
    List<HourlyForecast> forecasts = getHourlyForecast(location, futureDays, pastDays);
    HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
    int minimumTimestamp = Collections.max(forecasts,
        Comparator.comparing(HourlyForecast::getTimestamp))
        .getTimestamp();

    int maximumTimestamp = Collections.min(forecasts,
        Comparator.comparing(HourlyForecast::getTimestamp))
        .getTimestamp();

    List<HourlyForecast> existingForecasts = new HourlyForecastQuery()
        .whereTimestampGE(minimumTimestamp)
        .whereTimestampLE(maximumTimestamp)
        .whereLocation(location)
        .getResults();

    // TODO: fix this garbage
    // Delete all hourly forecasts with the same location and a timestamp within range
    for (HourlyForecast forecast : existingForecasts) {
      hourlyForecastDAO.delete(forecast);
    }

    // Insert all hourly forecasts
    for (HourlyForecast forecast : forecasts) {
      hourlyForecastDAO.insert(forecast);
    }
  }
}