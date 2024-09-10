package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.*;

@Entity(name = "dailyForecast")
@Table(name = "dailyForecast", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_id", "timestamp"})
})

public class DailyForecast {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @PrimaryKeyJoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Location location;

  @Column(name="timestamp", nullable = false)
  private Integer timestamp;

  @Column(name="weather_code")
  private Integer weather_code;

  @Column(name="temperature_2m_max", nullable = false)
  private Double temperature_2m_max;

  @Column(name="temperature_2m_min", nullable = false)
  private Double temperature_2m_min;

  @Column(name="apparent_temperature_max", nullable = false)
  private Double apparent_temperature_max;

  @Column(name="apparent_temperature_min", nullable = false)
  private Double apparent_temperature_min;

  @Column(name="sunrise")
  private Integer sunrise;

  @Column(name="sunset")
  private Integer sunset;

  @Column(name="daylight_duration")
  private Double daylight_duration;

  @Column(name="sunshine_duration")
  private Double sunshine_duration;

  @Column(name="uv_index_max")
  private Double uv_index_max;

  @Column(name="uv_index_clear_sky_max")
  private Double uv_index_clear_sky_max;

  @Column(name="precipitation_sum")
  private Double precipitation_sum;

  @Column(name="rain_sum")
  private Double rain_sum;

  @Column(name="showers_sum")
  private Double showers_sum;

  @Column(name="snowfall_sum")
  private Double snowfall_sum;

  @Column(name="precipitation_hours")
  private Double precipitation_hours;

  @Column(name="wind_speed_10m_max")
  private Double wind_speed_10m_max;

  @Column(name="wind_gusts_10m_max")
  private Double wind_gusts_10m_max;

  @Column(name="wind_direction_10m_dominant")
  private Double wind_direction_10m_dominant;

  @Column(name="shortwave_radiation_sum")
  private Double shortwave_radiation_sum;

  @Column(name="et0_fao_evapotranspiration")
  private Double et0_fao_evapotranspiration;

  public DailyForecast() {
  }


  public DailyForecast(Integer id, Location location, Integer timestamp, Integer weather_code,
      Double temperature_2m_max, Double temperature_2m_min, Double apparent_temperature_max,
      Double apparent_temperature_min, Integer sunrise, Integer sunset, Double daylight_duration,
      Double sunshine_duration, Double uv_index_max, Double uv_index_clear_sky_max,
      Double precipitation_sum, Double rain_sum, Double showers_sum, Double snowfall_sum,
      Double precipitation_hours, Double wind_speed_10m_max, Double wind_gusts_10m_max,
      Double wind_direction_10m_dominant, Double shortwave_radiation_sum,
      Double et0_fao_evapotranspiration) {
    this.id = id;
    this.location = location;
    this.timestamp = timestamp;
    this.weather_code = weather_code;
    this.temperature_2m_max = temperature_2m_max;
    this.temperature_2m_min = temperature_2m_min;
    this.apparent_temperature_max = apparent_temperature_max;
    this.apparent_temperature_min = apparent_temperature_min;
    this.sunrise = sunrise;
    this.sunset = sunset;
    this.daylight_duration = daylight_duration;
    this.sunshine_duration = sunshine_duration;
    this.uv_index_max = uv_index_max;
    this.uv_index_clear_sky_max = uv_index_clear_sky_max;
    this.precipitation_sum = precipitation_sum;
    this.rain_sum = rain_sum;
    this.showers_sum = showers_sum;
    this.snowfall_sum = snowfall_sum;
    this.precipitation_hours = precipitation_hours;
    this.wind_speed_10m_max = wind_speed_10m_max;
    this.wind_gusts_10m_max = wind_gusts_10m_max;
    this.wind_direction_10m_dominant = wind_direction_10m_dominant;
    this.shortwave_radiation_sum = shortwave_radiation_sum;
    this.et0_fao_evapotranspiration = et0_fao_evapotranspiration;
  }

  public DailyForecast(Location location, Integer timestamp, Integer weather_code,
      Double temperature_2m_max, Double temperature_2m_min, Double apparent_temperature_max,
      Double apparent_temperature_min, Integer sunrise, Integer sunset, Double daylight_duration,
      Double sunshine_duration, Double uv_index_max, Double uv_index_clear_sky_max,
      Double precipitation_sum, Double rain_sum, Double showers_sum, Double snowfall_sum,
      Double precipitation_hours, Double wind_speed_10m_max, Double wind_gusts_10m_max,
      Double wind_direction_10m_dominant, Double shortwave_radiation_sum,
      Double et0_fao_evapotranspiration) {
    this.location = location;
    this.timestamp = timestamp;
    this.weather_code = weather_code;
    this.temperature_2m_max = temperature_2m_max;
    this.temperature_2m_min = temperature_2m_min;
    this.apparent_temperature_max = apparent_temperature_max;
    this.apparent_temperature_min = apparent_temperature_min;
    this.sunrise = sunrise;
    this.sunset = sunset;
    this.daylight_duration = daylight_duration;
    this.sunshine_duration = sunshine_duration;
    this.uv_index_max = uv_index_max;
    this.uv_index_clear_sky_max = uv_index_clear_sky_max;
    this.precipitation_sum = precipitation_sum;
    this.rain_sum = rain_sum;
    this.showers_sum = showers_sum;
    this.snowfall_sum = snowfall_sum;
    this.precipitation_hours = precipitation_hours;
    this.wind_speed_10m_max = wind_speed_10m_max;
    this.wind_gusts_10m_max = wind_gusts_10m_max;
    this.wind_direction_10m_dominant = wind_direction_10m_dominant;
    this.shortwave_radiation_sum = shortwave_radiation_sum;
    this.et0_fao_evapotranspiration = et0_fao_evapotranspiration;
  }


  public Integer getId() {
    return id;
  }

  public Location getLocation() {
    return location;
  }

  public Integer getTimestamp() {
    return timestamp;
  }

  public Integer getWeather_code() {
    return weather_code;
  }

  public Double getTemperature_2m_max() {
    return temperature_2m_max;
  }

  public Double getTemperature_2m_min() {
    return temperature_2m_min;
  }

  public Double getApparent_temperature_max() {
    return apparent_temperature_max;
  }

  public Double getApparent_temperature_min() {
    return apparent_temperature_min;
  }

  public Integer getSunrise() {
    return sunrise;
  }

  public Integer getSunset() {
    return sunset;
  }

  public Double getDaylight_duration() {
    return daylight_duration;
  }

  public Double getSunshine_duration() {
    return sunshine_duration;
  }

  public Double getUv_index_max() {
    return uv_index_max;
  }

  public Double getUv_index_clear_sky_max() {
    return uv_index_clear_sky_max;
  }

  public Double getPrecipitation_sum() {
    return precipitation_sum;
  }

  public Double getRain_sum() {
    return rain_sum;
  }

  public Double getShowers_sum() {
    return showers_sum;
  }

  public Double getSnowfall_sum() {
    return snowfall_sum;
  }

  public Double getPrecipitation_hours() {
    return precipitation_hours;
  }

  public Double getWind_speed_10m_max() {
    return wind_speed_10m_max;
  }

  public Double getWind_gusts_10m_max() {
    return wind_gusts_10m_max;
  }

  public Double getWind_direction_10m_dominant() {
    return wind_direction_10m_dominant;
  }

  public Double getShortwave_radiation_sum() {
    return shortwave_radiation_sum;
  }

  public Double getEt0_fao_evapotranspiration() {
    return et0_fao_evapotranspiration;
  }


  @Override
  public String toString() {
    return "DailyForecast{" +
        "id=" + id +
        ", location=" + location +
        ", timestamp=" + timestamp +
        ", weather_code=" + weather_code +
        ", temperature_2m_max=" + temperature_2m_max +
        ", temperature_2m_min=" + temperature_2m_min +
        ", apparent_temperature_max=" + apparent_temperature_max +
        ", apparent_temperature_min=" + apparent_temperature_min +
        ", sunrise=" + sunrise +
        ", sunset=" + sunset +
        ", daylight_duration=" + daylight_duration +
        ", sunshine_duration=" + sunshine_duration +
        ", uv_index_max=" + uv_index_max +
        ", uv_index_clear_sky_max=" + uv_index_clear_sky_max +
        ", precipitation_sum=" + precipitation_sum +
        ", rain_sum=" + rain_sum +
        ", showers_sum=" + showers_sum +
        ", snowfall_sum=" + snowfall_sum +
        ", precipitation_hours=" + precipitation_hours +
        ", wind_speed_10m_max=" + wind_speed_10m_max +
        ", wind_gusts_10m_max=" + wind_gusts_10m_max +
        ", wind_direction_10m_dominant=" + wind_direction_10m_dominant +
        ", shortwave_radiation_sum=" + shortwave_radiation_sum +
        ", et0_fao_evapotranspiration=" + et0_fao_evapotranspiration +
        '}';
  }
}

