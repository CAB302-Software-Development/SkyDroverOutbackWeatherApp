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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity(name = "dailyForecast")
@Table(name = "dailyForecast", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_account_id","location_latitude","location_longitude","location_elevation", "timestamp"})
})
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Builder
@EqualsAndHashCode
/**
 * A model class for the DailyForecast entity.
 */
public class DailyForecast {
  /**
   * The ID of the daily forecast.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter
  private Integer id;

  /**
   * The associated location of the daily forecast.
   */
  @ManyToOne
  @PrimaryKeyJoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  @EqualsAndHashCode.Exclude
  private Location location;

  /**
   * The timestamp of the daily forecast.
   */
  @Column(name="timestamp", nullable = false)
  private Integer timestamp;

  /**
   * Weather condition as a numeric code. Follow WMO weather interpretation codes.
   */
  @Column(name="weather_code", nullable = false)
  private Integer weather_code;

  /**
   * The maximum temperature at 2m above ground level.
   */
  @Column(name="temperature_2m_max", nullable = false)
  private Double temperature_2m_max;

  /**
   * The minimum temperature at 2m above ground level.
   */
  @Column(name="temperature_2m_min", nullable = false)
  private Double temperature_2m_min;

  /**
   * The maximum apparent temperature.
   */
  @Column(name="apparent_temperature_max", nullable = false)
  private Double apparent_temperature_max;

  /**
   * The minimum apparent temperature.
   */
  @Column(name="apparent_temperature_min", nullable = false)
  private Double apparent_temperature_min;

  /**
   * The time of sunrise.
   */
  @Column(name="sunrise", nullable = false)
  private Integer sunrise;

  /**
   * The time of sunset.
   */
  @Column(name="sunset", nullable = false)
  private Integer sunset;

  /**
   * Number of seconds of daylight per day
   */
  @Column(name="daylight_duration", nullable = false)
  private Double daylight_duration;

  /**
   * 	The number of seconds of sunshine per day is determined by calculating direct normalized
   * 	irradiance exceeding 120 W/m², following the WMO definition.
   * 	Sunshine duration will consistently be less than daylight duration due to dawn and dusk.
   */
  @Column(name="sunshine_duration", nullable = false)
  private Double sunshine_duration;

  /**
   * The maximum UV index.
   */
  @Column(name="uv_index_max")
  private Double uv_index_max;

  /**
   * The maximum UV index under clear sky conditions.
   */
  @Column(name="uv_index_clear_sky_max")
  private Double uv_index_clear_sky_max;

  /**
   * Sum of daily precipitation (including rain, showers and snowfall)
   */
  @Column(name="precipitation_sum")
  private Double precipitation_sum;

  /**
   * Sum of daily rain precipitation
   */
  @Column(name="rain_sum")
  private Double rain_sum;

  /**
   * Sum of daily showers precipitation
   */
  @Column(name="showers_sum")
  private Double showers_sum;

  /**
   * Sum of daily snowfall precipitation
   */
  @Column(name="snowfall_sum")
  private Double snowfall_sum;

  /**
   * The number of hours with rain
   */
  @Column(name="precipitation_hours")
  private Double precipitation_hours;

  /**
   * Maximum wind speed on a day at 10m above ground level
   */
  @Column(name="wind_speed_10m_max")
  private Double wind_speed_10m_max;

  /**
   * Maximum wind gusts on a day at 10m above ground level
   */
  @Column(name="wind_gusts_10m_max")
  private Double wind_gusts_10m_max;

  /**
   * The dominant wind direction at 10m above ground level
   */
  @Column(name="wind_direction_10m_dominant")
  private Double wind_direction_10m_dominant;

  /**
   * Sum of daily shortwave radiation
   */
  @Column(name="shortwave_radiation_sum")
  private Double shortwave_radiation_sum;

  /**
   * Daily sum of ET₀ Reference Evapotranspiration of a well watered grass field
   */
  @Column(name="et0_fao_evapotranspiration")
  private Double et0_fao_evapotranspiration;

  public DailyForecast() {
  }
}

