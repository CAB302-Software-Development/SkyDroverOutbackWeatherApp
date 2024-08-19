package cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.sql.Connection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DAO.*;
import java.net.URI;
import java.net.http.*;

public class Sdk {

  private Connection connection;
  private HttpClient client = HttpClient.newHttpClient();

  public Sdk() {
    connection = DatabaseConnection.getInstance();
  }

  public void updateDailyForecast(Location location) {
    double longitude = location.getLongitude();
    double latitude = location.getLatitude();
    double elevation = location.getElevation();

    int days = 10;
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
                + "&forecast_days=" + days
                + "&timeformat=unixtime"))
        .header("accept", "application/json")
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
      // parse as json
      JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

      System.out.println(response.body());
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

      DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();

      for (int i = 0; i < days; i++) {
        DailyForecast dailyForecast = new DailyForecast(
            location.getId(),
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
        dailyForecastDAO.insert(dailyForecast);
      }

    } catch (Exception ex) {
      System.err.println(ex);
    }
  }
}