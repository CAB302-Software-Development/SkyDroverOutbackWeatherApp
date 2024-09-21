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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "dailyForecast")
@Table(name = "dailyForecast", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_id", "timestamp"})
})

/**
 * A model class for the DailyForecast entity.
 */
public class DailyForecast {
  /**
   * The ID of the daily forecast.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter
  private Integer id;

  /**
   * The associated location of the daily forecast.
   */
  @ManyToOne
  @PrimaryKeyJoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Getter
  private Location location;

  /**
   * The timestamp of the daily forecast.
   */
  @Column(name="timestamp", nullable = false)
  @Getter
  private Integer timestamp;

  /**
   * Weather condition as a numeric code. Follow WMO weather interpretation codes.
   */
  @Column(name="weather_code", nullable = false)
  @Getter
  private Integer weather_code;

  /**
   * The maximum temperature at 2m above ground level.
   */
  @Column(name="temperature_2m_max", nullable = false)
  @Getter
  private Double temperature_2m_max;

  /**
   * The minimum temperature at 2m above ground level.
   */
  @Column(name="temperature_2m_min", nullable = false)
  @Getter
  private Double temperature_2m_min;

  /**
   * The maximum apparent temperature.
   */
  @Column(name="apparent_temperature_max", nullable = false)
  @Getter
  private Double apparent_temperature_max;

  /**
   * The minimum apparent temperature.
   */
  @Column(name="apparent_temperature_min", nullable = false)
  @Getter
  private Double apparent_temperature_min;

  /**
   * The time of sunrise.
   */
  @Column(name="sunrise", nullable = false)
  @Getter
  private Integer sunrise;

  /**
   * The time of sunset.
   */
  @Column(name="sunset", nullable = false)
  @Getter
  private Integer sunset;

  /**
   * Number of seconds of daylight per day
   */
  @Column(name="daylight_duration", nullable = false)
  @Getter
  private Double daylight_duration;

  /**
   * 	The number of seconds of sunshine per day is determined by calculating direct normalized
   * 	irradiance exceeding 120 W/m², following the WMO definition.
   * 	Sunshine duration will consistently be less than daylight duration due to dawn and dusk.
   */
  @Column(name="sunshine_duration", nullable = false)
  @Getter
  private Double sunshine_duration;

  /**
   * The maximum UV index.
   */
  @Column(name="uv_index_max")
  @Getter
  private Double uv_index_max;

  /**
   * The maximum UV index under clear sky conditions.
   */
  @Column(name="uv_index_clear_sky_max")
  @Getter
  private Double uv_index_clear_sky_max;

  /**
   * Sum of daily precipitation (including rain, showers and snowfall)
   */
  @Column(name="precipitation_sum")
  @Getter
  private Double precipitation_sum;

  /**
   * Sum of daily rain precipitation
   */
  @Column(name="rain_sum")
  @Getter
  private Double rain_sum;

  /**
   * Sum of daily showers precipitation
   */
  @Column(name="showers_sum")
  @Getter
  private Double showers_sum;

  /**
   * Sum of daily snowfall precipitation
   */
  @Column(name="snowfall_sum")
  @Getter
  private Double snowfall_sum;

  /**
   * The number of hours with rain
   */
  @Column(name="precipitation_hours")
  @Getter
  private Double precipitation_hours;

  /**
   * Maximum wind speed on a day at 10m above ground level
   */
  @Column(name="wind_speed_10m_max")
  @Getter
  private Double wind_speed_10m_max;

  /**
   * Maximum wind gusts on a day at 10m above ground level
   */
  @Column(name="wind_gusts_10m_max")
  @Getter
  private Double wind_gusts_10m_max;

  /**
   * The dominant wind direction at 10m above ground level
   */
  @Column(name="wind_direction_10m_dominant")
  @Getter
  private Double wind_direction_10m_dominant;

  /**
   * Sum of daily shortwave radiation
   */
  @Column(name="shortwave_radiation_sum")
  @Getter
  private Double shortwave_radiation_sum;

  /**
   * Daily sum of ET₀ Reference Evapotranspiration of a well watered grass field
   */
  @Column(name="et0_fao_evapotranspiration")
  @Getter
  private Double et0_fao_evapotranspiration;

  public DailyForecast() {
  }

  /**
   * @param id The ID of the daily forecast.
   * @param location The location of the daily forecast.
   * @param timestamp The timestamp of the daily forecast.
   * @param weather_code The weather code of the daily forecast.
   * @param temperature_2m_max The maximum temperature at 2m above ground level.
   * @param temperature_2m_min The minimum temperature at 2m above ground level.
   * @param apparent_temperature_max The maximum apparent temperature.
   * @param apparent_temperature_min The minimum apparent temperature.
   * @param sunrise The time of sunrise.
   * @param sunset The time of sunset.
   * @param daylight_duration Number of seconds of daylight per day
   * @param sunshine_duration The number of seconds of sunshine per day is determined by calculating
   *                         direct normalized irradiance exceeding 120 W/m², following the WMO
   *                         definition. Sunshine duration will consistently be less than daylight
   *                         duration due to dawn and dusk.
   * @param uv_index_max The maximum UV index. (null if not available)
   * @param uv_index_clear_sky_max The maximum UV index under clear sky conditions.
   *                              (null if not available)
   * @param precipitation_sum Sum of daily precipitation (including rain, showers and snowfall)
   *                         (null if not available)
   * @param rain_sum Sum of daily rain precipitation (null if not available)
   * @param showers_sum Sum of daily showers precipitation (null if not available)
   * @param snowfall_sum Sum of daily snowfall precipitation (null if not available)
   * @param precipitation_hours The number of hours with rain (null if not available)
   * @param wind_speed_10m_max Maximum wind speed on a day at 10m above ground level
   *                          (null if not available)
   * @param wind_gusts_10m_max Maximum wind gusts on a day at 10m above ground level
   *                          (null if not available)
   * @param wind_direction_10m_dominant The dominant wind direction at 10m above ground level
   *                                   (null if not available)
   * @param shortwave_radiation_sum Sum of daily shortwave radiation (null if not available)
   * @param et0_fao_evapotranspiration 	Daily sum of ET₀ Reference Evapotranspiration of a well
   *                                   watered grass field (null if not available)
   */
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

  /**
   * @param location The location of the daily forecast.
   * @param timestamp The timestamp of the daily forecast.
   * @param weather_code The weather code of the daily forecast.
   * @param temperature_2m_max The maximum temperature at 2m above ground level.
   * @param temperature_2m_min The minimum temperature at 2m above ground level.
   * @param apparent_temperature_max The maximum apparent temperature.
   * @param apparent_temperature_min The minimum apparent temperature.
   * @param sunrise The time of sunrise.
   * @param sunset The time of sunset.
   * @param daylight_duration Number of seconds of daylight per day
   * @param sunshine_duration The number of seconds of sunshine per day is determined by calculating
   *                         direct normalized irradiance exceeding 120 W/m², following the WMO
   *                         definition. Sunshine duration will consistently be less than daylight
   *                         duration due to dawn and dusk.
   * @param uv_index_max The maximum UV index. (null if not available)
   * @param uv_index_clear_sky_max The maximum UV index under clear sky conditions.
   *                              (null if not available)
   * @param precipitation_sum Sum of daily precipitation (including rain, showers and snowfall)
   *                         (null if not available)
   * @param rain_sum Sum of daily rain precipitation (null if not available)
   * @param showers_sum Sum of daily showers precipitation (null if not available)
   * @param snowfall_sum Sum of daily snowfall precipitation (null if not available)
   * @param precipitation_hours The number of hours with rain (null if not available)
   * @param wind_speed_10m_max Maximum wind speed on a day at 10m above ground level
   *                          (null if not available)
   * @param wind_gusts_10m_max Maximum wind gusts on a day at 10m above ground level
   *                          (null if not available)
   * @param wind_direction_10m_dominant The dominant wind direction at 10m above ground level
   *                                   (null if not available)
   * @param shortwave_radiation_sum Sum of daily shortwave radiation (null if not available)
   * @param et0_fao_evapotranspiration 	Daily sum of ET₀ Reference Evapotranspiration of a well
   *                                   watered grass field (null if not available)
   */
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

