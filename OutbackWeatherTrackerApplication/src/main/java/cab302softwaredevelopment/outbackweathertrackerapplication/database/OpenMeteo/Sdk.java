package cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO.DailyForecastQuery;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO.HourlyForecastQuery;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast.DailyForecastBuilder;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast.HourlyForecastBuilder;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A rough Software Development Kit for the OpenMeteo API.
 */
public class Sdk {

  static List<URI> apiURLs;

  static {
    try {
      apiURLs = new ArrayList<>(Arrays.asList(new URI("http://cyphix.ddns.net:8000/"),new URI("http://api.open-meteo.com/")));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  static URI apiURL = apiURLs.getFirst();
  public Sdk() {
  }

  private static void rotateApiHost() {
    URI oldApiHost = apiURLs.getFirst();
    apiURLs.removeFirst();
    apiURLs.add(oldApiHost);
    apiURL = apiURLs.getFirst();
  }

  public List<DailyForecastBuilder> getDailyForecastBuilders(Location location, int futureDays, int pastDays){
    double longitude = location.getLongitude();
    double latitude = location.getLatitude();
    double elevation = location.getElevation();

    // Test that the server may exist before trying to connect
    try {
      InetAddress.getByName(apiURL.getHost());
    } catch (UnknownHostException e) {
      // The server does not exist
      rotateApiHost();
    }

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(
            apiURL
                + "v1/forecast?"
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

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (ConnectException e) {
      // Unable to connect to the server
      rotateApiHost();
      return getDailyForecastBuilders(location, futureDays, pastDays);
    } catch(HttpConnectTimeoutException e){
      // Timeout
      rotateApiHost();
      return getDailyForecastBuilders(location, futureDays, pastDays);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    if (response.statusCode() != 200) {
      // Server returned an error
      rotateApiHost();
      return getDailyForecastBuilders(location, futureDays, pastDays);
    }

    // parse as json
    JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();


    JsonObject daily = jsonObject.getAsJsonObject("daily");
    // replace all NULL values from sub-arrays with -1
    for (String key : daily.keySet()) {
      if (!daily.get(key).isJsonArray()) continue;
      for (int i = 0; i < daily.getAsJsonArray(key).size(); i++) {
        if (daily.getAsJsonArray(key).get(i).isJsonNull()) {
          daily.getAsJsonArray(key).set(i, new JsonPrimitive(Double.NaN));
        }
      }
    }

    // time represents the number of samples
    int totalEntries = daily.getAsJsonArray("time").size();
    List<DailyForecast.DailyForecastBuilder> dailyForecasts = new ArrayList<>();
    for (int i = 0; i < totalEntries; i++) {
      DailyForecast.DailyForecastBuilder dailyForecast = DailyForecast.builder()
          .location(location)
          .timestamp(daily.getAsJsonArray("time").get(i).getAsInt())
          .weather_code(daily.getAsJsonArray("weather_code").get(i).getAsInt())
          .temperature_2m_max(daily.getAsJsonArray("temperature_2m_max").get(i).getAsDouble())
          .temperature_2m_min(daily.getAsJsonArray("temperature_2m_min").get(i).getAsDouble())
          .apparent_temperature_max(daily.getAsJsonArray("apparent_temperature_max").get(i).getAsDouble())
          .apparent_temperature_min(daily.getAsJsonArray("apparent_temperature_min").get(i).getAsDouble())
          .sunrise(daily.getAsJsonArray("sunrise").get(i).getAsInt())
          .sunset(daily.getAsJsonArray("sunset").get(i).getAsInt())
          .daylight_duration(daily.getAsJsonArray("daylight_duration").get(i).getAsDouble())
          .sunshine_duration(daily.getAsJsonArray("sunshine_duration").get(i).getAsDouble())
          .uv_index_max(daily.getAsJsonArray("uv_index_max").get(i).getAsDouble())
          .uv_index_clear_sky_max(daily.getAsJsonArray("uv_index_clear_sky_max").get(i).getAsDouble())
          .precipitation_sum(daily.getAsJsonArray("precipitation_sum").get(i).getAsDouble())
          .rain_sum(daily.getAsJsonArray("rain_sum").get(i).getAsDouble())
          .showers_sum(daily.getAsJsonArray("showers_sum").get(i).getAsDouble())
          .snowfall_sum(daily.getAsJsonArray("snowfall_sum").get(i).getAsDouble())
          .precipitation_hours(daily.getAsJsonArray("precipitation_hours").get(i).getAsDouble())
          .wind_speed_10m_max(daily.getAsJsonArray("wind_speed_10m_max").get(i).getAsDouble())
          .wind_gusts_10m_max(daily.getAsJsonArray("wind_gusts_10m_max").get(i).getAsDouble())
          .wind_direction_10m_dominant(daily.getAsJsonArray("wind_direction_10m_dominant").get(i).getAsDouble())
          .shortwave_radiation_sum(daily.getAsJsonArray("shortwave_radiation_sum").get(i).getAsDouble())
          .et0_fao_evapotranspiration(daily.getAsJsonArray("et0_fao_evapotranspiration").get(i).getAsDouble());
      dailyForecasts.add(dailyForecast);
    }
    return dailyForecasts;
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
    List<DailyForecast.DailyForecastBuilder> dailyForecastBuilders = getDailyForecastBuilders(location, futureDays, pastDays);
    List<DailyForecast> dailyForecasts = new ArrayList<>();
    for (DailyForecast.DailyForecastBuilder dailyForecastBuilder : dailyForecastBuilders) {
      dailyForecasts.add(dailyForecastBuilder.build());
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
    int minimumTimestamp = Collections.min(forecasts,
            Comparator.comparing(DailyForecast::getTimestamp))
        .getTimestamp();

    int maximumTimestamp = Collections.max(forecasts,
            Comparator.comparing(DailyForecast::getTimestamp))
        .getTimestamp();

    List<DailyForecast> existingForecasts = new DailyForecastQuery()
        .whereTimestampGE(minimumTimestamp)
        .whereTimestampLE(maximumTimestamp)
        .whereLocation(location)
        .getResults();

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
   * @return A list of HourlyForecastBuilders representing the forecasts.
   */
  public List<HourlyForecastBuilder> getHourlyForecastBuilders(Location location, int futureDays, int pastDays) {
    double longitude = location.getLongitude();
    double latitude = location.getLatitude();
    double elevation = location.getElevation();

    // Test that the server may exist before trying to connect
    try {
      InetAddress.getByName(apiURL.getHost());
    } catch (UnknownHostException e) {
      // The server does not exist
      rotateApiHost();
    }

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(
            apiURL
                + "v1/forecast?"
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

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (ConnectException e) {
      // Unable to connect to the server
      // Get the next host to try
      rotateApiHost();
      return getHourlyForecastBuilders(location, futureDays, pastDays);
    } catch(HttpConnectTimeoutException e){
      // Timeout
      rotateApiHost();
      return getHourlyForecastBuilders(location, futureDays, pastDays);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    if (response.statusCode() != 200) {
      // Server returned an error
      rotateApiHost();
      return getHourlyForecastBuilders(location, futureDays, pastDays);
    }

    // parse as json
    JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

    JsonObject hourly = jsonObject.getAsJsonObject("hourly");
    // replace all NULL values from sub-arrays with -1
    for (String key : hourly.keySet()) {
      if (!hourly.get(key).isJsonArray()) continue;
      for (int i = 0; i < hourly.getAsJsonArray(key).size(); i++) {
        if (hourly.getAsJsonArray(key).get(i).isJsonNull()) {
          hourly.getAsJsonArray(key).set(i, new JsonPrimitive(Double.NaN));
        }
      }
    }

    // Time represents the number of samples
    int totalEntries = hourly.getAsJsonArray("time").size();
    List<HourlyForecast.HourlyForecastBuilder> hourlyForecasts = new ArrayList<>();
    for (int i = 0; i < totalEntries; i++) {
      HourlyForecast.HourlyForecastBuilder hourlyForecast = HourlyForecast.builder()
          .location(location)
          .timestamp(hourly.getAsJsonArray("time").get(i).getAsInt())
          .temperature_2m(hourly.getAsJsonArray("temperature_2m").get(i).getAsDouble())
          .relative_humidity_2m(hourly.getAsJsonArray("relative_humidity_2m").get(i).getAsDouble())
          .dew_point_2m(hourly.getAsJsonArray("dew_point_2m").get(i).getAsDouble())
          .apparent_temperature(hourly.getAsJsonArray("apparent_temperature").get(i).getAsDouble())
          .precipitation(hourly.getAsJsonArray("precipitation").get(i).getAsDouble())
          .rain(hourly.getAsJsonArray("rain").get(i).getAsDouble())
          .showers(hourly.getAsJsonArray("showers").get(i).getAsDouble())
          .snowfall(hourly.getAsJsonArray("snowfall").get(i).getAsDouble())
          .snow_depth(hourly.getAsJsonArray("snow_depth").get(i).getAsDouble())
          .weather_code(hourly.getAsJsonArray("weather_code").get(i).getAsInt())
          .pressure_msl(hourly.getAsJsonArray("pressure_msl").get(i).getAsDouble())
          .surface_pressure(hourly.getAsJsonArray("surface_pressure").get(i).getAsDouble())
          .cloud_cover(hourly.getAsJsonArray("cloud_cover").get(i).getAsDouble())
          .cloud_cover_low(hourly.getAsJsonArray("cloud_cover_low").get(i).getAsDouble())
          .cloud_cover_mid(hourly.getAsJsonArray("cloud_cover_mid").get(i).getAsDouble())
          .cloud_cover_high(hourly.getAsJsonArray("cloud_cover_high").get(i).getAsDouble())
          .visibility(hourly.getAsJsonArray("visibility").get(i).getAsDouble())
          .et0_fao_evapotranspiration(hourly.getAsJsonArray("et0_fao_evapotranspiration").get(i).getAsDouble())
          .vapour_pressure_deficit(hourly.getAsJsonArray("vapour_pressure_deficit").get(i).getAsDouble())
          .wind_speed_10m(hourly.getAsJsonArray("wind_speed_10m").get(i).getAsDouble())
          .wind_speed_40m(hourly.getAsJsonArray("wind_speed_40m").get(i).getAsDouble())
          .wind_speed_80m(hourly.getAsJsonArray("wind_speed_80m").get(i).getAsDouble())
          .wind_speed_120m(hourly.getAsJsonArray("wind_speed_120m").get(i).getAsDouble())
          .wind_direction_10m(hourly.getAsJsonArray("wind_direction_10m").get(i).getAsDouble())
          .wind_direction_40m(hourly.getAsJsonArray("wind_direction_40m").get(i).getAsDouble())
          .wind_direction_80m(hourly.getAsJsonArray("wind_direction_80m").get(i).getAsDouble())
          .wind_direction_120m(hourly.getAsJsonArray("wind_direction_120m").get(i).getAsDouble())
          .wind_gusts_10m(hourly.getAsJsonArray("wind_gusts_10m").get(i).getAsDouble())
          .surface_temperature(hourly.getAsJsonArray("surface_temperature").get(i).getAsDouble())
          .soil_temperature_0_to_10cm(hourly.getAsJsonArray("soil_temperature_0_to_10cm").get(i).getAsDouble())
          .soil_temperature_10_to_35cm(hourly.getAsJsonArray("soil_temperature_10_to_35cm").get(i).getAsDouble())
          .soil_temperature_35_to_100cm(hourly.getAsJsonArray("soil_temperature_35_to_100cm").get(i).getAsDouble())
          .soil_temperature_100_to_300cm(hourly.getAsJsonArray("soil_temperature_100_to_300cm").get(i).getAsDouble())
          .soil_moisture_0_to_10cm(hourly.getAsJsonArray("soil_moisture_0_to_10cm").get(i).getAsDouble())
          .soil_moisture_10_to_35cm(hourly.getAsJsonArray("soil_moisture_10_to_35cm").get(i).getAsDouble())
          .soil_moisture_35_to_100cm(hourly.getAsJsonArray("soil_moisture_35_to_100cm").get(i).getAsDouble())
          .soil_moisture_100_to_300cm(hourly.getAsJsonArray("soil_moisture_100_to_300cm").get(i).getAsDouble())
          .is_day(hourly.getAsJsonArray("is_day").get(i).getAsInt()==1)
          .sunshine_duration(hourly.getAsJsonArray("sunshine_duration").get(i).getAsDouble())
          .shortwave_radiation(hourly.getAsJsonArray("shortwave_radiation").get(i).getAsDouble())
          .direct_radiation(hourly.getAsJsonArray("direct_radiation").get(i).getAsDouble())
          .diffuse_radiation(hourly.getAsJsonArray("diffuse_radiation").get(i).getAsDouble())
          .direct_normal_irradiance(hourly.getAsJsonArray("direct_normal_irradiance").get(i).getAsDouble())
          .global_tilted_irradiance(hourly.getAsJsonArray("global_tilted_irradiance").get(i).getAsDouble())
          .terrestrial_radiation(hourly.getAsJsonArray("terrestrial_radiation").get(i).getAsDouble())
          .shortwave_radiation_instant(hourly.getAsJsonArray("shortwave_radiation_instant").get(i).getAsDouble())
          .direct_radiation_instant(hourly.getAsJsonArray("direct_radiation_instant").get(i).getAsDouble())
          .diffuse_radiation_instant(hourly.getAsJsonArray("diffuse_radiation_instant").get(i).getAsDouble())
          .direct_normal_irradiance_instant(hourly.getAsJsonArray("direct_normal_irradiance_instant").get(i).getAsDouble())
          .global_tilted_irradiance_instant(hourly.getAsJsonArray("global_tilted_irradiance_instant").get(i).getAsDouble())
          .terrestrial_radiation_instant(hourly.getAsJsonArray("terrestrial_radiation_instant").get(i).getAsDouble());

      hourlyForecasts.add(hourlyForecast);
    }
    return hourlyForecasts;
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
    List<HourlyForecast.HourlyForecastBuilder> hourlyForecastBuilders = getHourlyForecastBuilders(location, futureDays, pastDays);
    List<HourlyForecast> hourlyForecasts = new ArrayList<>();
    for (HourlyForecast.HourlyForecastBuilder hourlyForecastBuilder : hourlyForecastBuilders) {
      hourlyForecasts.add(hourlyForecastBuilder.build());
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
    int minimumTimestamp = Collections.min(forecasts,
            Comparator.comparing(HourlyForecast::getTimestamp))
        .getTimestamp();

    int maximumTimestamp = Collections.max(forecasts,
            Comparator.comparing(HourlyForecast::getTimestamp))
        .getTimestamp();

    List<HourlyForecast> existingForecasts = new HourlyForecastQuery()
        .whereTimestampGE(minimumTimestamp)
        .whereTimestampLE(maximumTimestamp)
        .whereLocation(location)
        .getResults();

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