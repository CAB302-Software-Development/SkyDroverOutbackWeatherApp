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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


/**
 * A model class for the HourlyForecast entity.
 */
@Getter
@Entity(name = "HourlyForecast")
@Table(name = "hourlyForecast", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_account_id","location_latitude","location_longitude","location_elevation", "timestamp"})
})
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class HourlyForecast {
  /**
   * The ID of the hourly forecast.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter
  private Integer id;

  /**
   * The associated location of the hourly forecast.
   */
  @ManyToOne
  @PrimaryKeyJoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Location location;

  /**
   * The timestamp of the hourly forecast.
   */
  @Column(name="timestamp")
  private Integer timestamp;

  /**
   * The temperature at 2m above ground level.
   */
  @Column(name="temperature_2m")
  private Double temperature_2m;

  /**
   * The relative humidity at 2m above ground level.
   */
  @Column(name="relative_humidity_2m")
  private Double relative_humidity_2m;

  /**
   * 	Dew point temperature at 2 meters above ground
   */
  @Column(name="dew_point_2m")
  private Double dew_point_2m;

  /**
   * 	Apparent temperature is the perceived feels-like temperature combining wind chill factor,
   * 	relative humidity and solar radiation
   */
  @Column(name="apparent_temperature")
  private Double apparent_temperature;

  /**
   * 	Total precipitation (rain, showers, snow) sum of the preceding hour
   */
  @Column(name="precipitation")
  private Double precipitation;

  /**
   * 	Rain from large scale weather systems of the preceding hour in millimeter
   */
  @Column(name="rain")
  private Double rain;

  /**
   * Showers from convective precipitation in millimeters from the preceding hour
   */
  @Column(name="showers")
  private Double showers;

  /**
   * Snowfall amount of the preceding hour in centimeters. For the water equivalent in millimeter,
   * divide by 7. E.g. 7 cm snow = 10 mm precipitation water equivalent
   */
  @Column(name="snowfall")
  private Double snowfall;

  /**
   * Snow depth on the ground
   */
  @Column(name="snow_depth")
  private Double snow_depth;

  /**
   * Weather condition as a numeric code. Follow WMO weather interpretation codes.
   */
  @Column(name="weather_code")
  private Integer weather_code;

  /**
   * 	Atmospheric air pressure reduced to mean sea level (msl) or
   * 	Typically pressure on mean sea level is used in meteorology.
   */
  @Column(name="pressure_msl")
  private Double pressure_msl;

  /**
   * 	Pressure at surface. Typically pressure on mean sea level is used in meteorology.
   * 	Surface pressure gets lower with increasing elevation.
   */
  @Column(name="surface_pressure")
  private Double surface_pressure;

  /**
   * 	Total cloud cover as an area fraction in the range [0, 100]
   */
  @Column(name="cloud_cover")
  private Double cloud_cover;

  /**
   * 	Low level clouds and fog up to 3 km altitude in the range [0, 100]
   */
  @Column(name="cloud_cover_low")
  private Double cloud_cover_low;

  /**
   * 	Mid level clouds from 3 to 8 km altitude in the range [0, 100]
   */
  @Column(name="cloud_cover_mid")
  private Double cloud_cover_mid;

  /**
   * 	High level clouds from 8 km altitude in the range [0, 100]
   */
  @Column(name="cloud_cover_high")
  private Double cloud_cover_high;

  /**
   * 	Viewing distance in meters. Influenced by low clouds, humidity and aerosols.
   */
  @Column(name="visibility")
  private Double visibility;

  /**
   * 	ET₀ Reference Evapotranspiration of a well watered grass field.
   * 	Based on FAO-56 Penman-Monteith equations ET₀ is calculated from temperature,
   * 	wind speed, humidity and solar radiation. Unlimited soil water is assumed.
   * 	ET₀ is commonly used to estimate the required irrigation for plants.
   */
  @Column(name="et0_fao_evapotranspiration")
  private Double et0_fao_evapotranspiration;

  /**
   * Vapor Pressure Deificit (VPD) in kilopascal (kPa). For high VPD (>1.6),
   * water transpiration of plants increases. For low VPD (<0.4), transpiration decreases
   */
  @Column(name="vapour_pressure_deficit")
  private Double vapour_pressure_deficit;

  /**
   *   Wind speed at 10 meters above ground level in m/s
   */
  @Column(name="wind_speed_10m")
  private Double wind_speed_10m;

  /**
   * 	Wind speed at 40 meters above ground level in m/s
   */
  @Column(name="wind_speed_40m")
  private Double wind_speed_40m;

  /**
   * 	Wind speed at 80 meters above ground level in m/s
   */
  @Column(name="wind_speed_80m")
  private Double wind_speed_80m;

  /**
   *  Wind speed at 120 meters above ground level in m/s
   */
  @Column(name="wind_speed_120m")
  private Double wind_speed_120m;

  /**
   * 	Wind direction at 10 meters above ground level in degrees
   */
  @Column(name="wind_direction_10m")
  private Double wind_direction_10m;

  /**
   * 	Wind direction at 40 meters above ground level in degrees
   */
  @Column(name="wind_direction_40m")
  private Double wind_direction_40m;

  /**
   * 	Wind direction at 80 meters above ground level in degrees
   */
  @Column(name="wind_direction_80m")
  private Double wind_direction_80m;

  /**
   * 	Wind direction at 120 meters above ground level in degrees
   */
  @Column(name="wind_direction_120m")
  private Double wind_direction_120m;

  /**
   * 	Gusts at 10 meters above ground as a maximum of the preceding hour in m/s
   */
  @Column(name="wind_gusts_10m")
  private Double wind_gusts_10m;

  /**
   * 	Surface temperature in degrees Celsius
   */
  @Column(name="surface_temperature")
  private Double surface_temperature;

  /**
   * 	Average soil temperature at 0 to 10 cm depth in degrees Celsius
   */
  @Column(name="soil_temperature_0_to_10cm")
  private Double soil_temperature_0_to_10cm;

  /**
   * 	Average soil temperature at 10 to 35 cm depth in degrees Celsius
   */
  @Column(name="soil_temperature_10_to_35cm")
  private Double soil_temperature_10_to_35cm;

  /**
   * 	Average soil temperature at 35 to 100 cm depth in degrees Celsius
   */
  @Column(name="soil_temperature_35_to_100cm")
  private Double soil_temperature_35_to_100cm;

  /**
   * 	Average soil temperature at 100 to 300 cm depth in degrees Celsius
   */
  @Column(name="soil_temperature_100_to_300cm")
  private Double soil_temperature_100_to_300cm;

  /**
   * 	Average soil water content as volumetric mixing ratio at 0 to 10 cm depth in m³/m³
   */
  @Column(name="soil_moisture_0_to_10cm")
  private Double soil_moisture_0_to_10cm;

  /**
   * 	Average soil water content as volumetric mixing ratio at 10 to 35 cm depth in m³/m³
   */
  @Column(name="soil_moisture_10_to_35cm")
  private Double soil_moisture_10_to_35cm;

  /**
   * 	Average soil water content as volumetric mixing ratio at 35 to 100 cm depth in m³/m³
   */
  @Column(name="soil_moisture_35_to_100cm")
  private Double soil_moisture_35_to_100cm;

  /**
   * 	Average soil water content as volumetric mixing ratio at 100 to 300 cm depth in m³/m³
   */
  @Column(name="soil_moisture_100_to_300cm")
  private Double soil_moisture_100_to_300cm;

  /**
   * 	Whether it is day or night
   */
  @Column(name="is_day")
  private boolean is_day;

  /**
   * Number of seconds of sunshine of the preceding hour per hour calculated by direct normalized
   * irradiance exceeding 120 W/m², following the WMO definition.
   */
  @Column(name="sunshine_duration")
  private Double sunshine_duration;

  /**
   *  Shortwave solar radiation as average of the preceding hour. This is equal to the total global horizontal irradiation
   */
  @Column(name="shortwave_radiation")
  private Double shortwave_radiation;

  /**
   * 	Direct solar radiation as average of the preceding hour on the horizontal plane
   */
  @Column(name="direct_radiation")
  private Double direct_radiation;

  /**
   * 	Diffuse solar radiation as average of the preceding hour.
   */
  @Column(name="diffuse_radiation")
  private Double diffuse_radiation;

  /**
   *	Direct solar radiation as average of the preceding hour on the normal plane
   */
  @Column(name="direct_normal_irradiance")
  private Double direct_normal_irradiance;

  /**
   * 	Global solar radiation on a tilted plane as average of the preceding hour
   */
  @Column(name="global_tilted_irradiance")
  private Double global_tilted_irradiance;

  /**
   * 	Total radiation received on a tilted pane as average of the preceding hour.
   * 	The calculation is assuming a fixed albedo of 20% and in isotropic sky
   */
  @Column(name="terrestrial_radiation")
  private Double terrestrial_radiation;

  /**
   * 	Shortwave solar radiation as an instantaneous value
   */
  @Column(name="shortwave_radiation_instant")
  private Double shortwave_radiation_instant;

  /**
   * 	Direct solar radiation as an instantaneous value on the horizontal plane
   */
  @Column(name="direct_radiation_instant")
  private Double direct_radiation_instant;

  /**
   * 	Diffuse solar radiation as an instantaneous value
   */
  @Column(name="diffuse_radiation_instant")
  private Double diffuse_radiation_instant;

  /**
   * 	Direct solar radiation as an instantaneous value on the normal plane
   */
  @Column(name="direct_normal_irradiance_instant")
  private Double direct_normal_irradiance_instant;

  /**
   * 	Global solar radiation on a tilted plane as an instantaneous value
   */
  @Column(name="global_tilted_irradiance_instant")
  private Double global_tilted_irradiance_instant;

  /**
   * 	Total radiation received on a tilted pane as an instantaneous value.
   * 	The calculation is assuming a fixed albedo of 20% and in isotropic sky
   */
  @Column(name="terrestrial_radiation_instant")
  private Double terrestrial_radiation_instant;

  /**
   * Default constructor for HourlyForecast
   */
  public HourlyForecast() {
  }

  /**
   * Constructor for HourlyForecast
   * @param location Location
   * @param timestamp Integer
   * @param temperature_2m Double
   * @param relative_humidity_2m Double
   * @param dew_point_2m Double
   * @param apparent_temperature Double
   * @param precipitation Double
   * @param rain Double
   * @param showers Double
   * @param snowfall Double
   * @param snow_depth Double
   * @param weather_code Integer
   * @param pressure_msl Double
   * @param surface_pressure Double
   * @param cloud_cover Double
   * @param cloud_cover_low Double
   * @param cloud_cover_mid Double
   * @param cloud_cover_high Double
   * @param visibility Double
   * @param et0_fao_evapotranspiration Double
   * @param vapour_pressure_deficit Double
   * @param wind_speed_10m Double
   * @param wind_speed_40m Double
   * @param wind_speed_80m Double
   * @param wind_speed_120m Double
   * @param wind_direction_10m Double
   * @param wind_direction_40m Double
   * @param wind_direction_80m Double
   * @param wind_direction_120m Double
   * @param wind_gusts_10m Double
   * @param surface_temperature Double
   * @param soil_temperature_0_to_10cm Double
   * @param soil_temperature_10_to_35cm Double
   * @param soil_temperature_35_to_100cm Double
   * @param soil_temperature_100_to_300cm Double
   * @param soil_moisture_0_to_10cm Double
   * @param soil_moisture_10_to_35cm Double
   * @param soil_moisture_35_to_100cm Double
   * @param soil_moisture_100_to_300cm Double
   * @param is_day boolean
   * @param sunshine_duration Double
   * @param shortwave_radiation Double
   * @param direct_radiation Double
   * @param diffuse_radiation Double
   * @param direct_normal_irradiance Double
   * @param global_tilted_irradiance Double
   * @param terrestrial_radiation Double
   */
  public HourlyForecast(Location location, Integer timestamp, Double temperature_2m,
      Double relative_humidity_2m, Double dew_point_2m, Double apparent_temperature,
      Double precipitation, Double rain, Double showers, Double snowfall, Double snow_depth,
      Integer weather_code, Double pressure_msl, Double surface_pressure, Double cloud_cover,
      Double cloud_cover_low, Double cloud_cover_mid, Double cloud_cover_high, Double visibility,
      Double et0_fao_evapotranspiration, Double vapour_pressure_deficit, Double wind_speed_10m,
      Double wind_speed_40m, Double wind_speed_80m, Double wind_speed_120m,
      Double wind_direction_10m, Double wind_direction_40m, Double wind_direction_80m,
      Double wind_direction_120m, Double wind_gusts_10m, Double surface_temperature,
      Double soil_temperature_0_to_10cm, Double soil_temperature_10_to_35cm,
      Double soil_temperature_35_to_100cm, Double soil_temperature_100_to_300cm,
      Double soil_moisture_0_to_10cm, Double soil_moisture_10_to_35cm,
      Double soil_moisture_35_to_100cm, Double soil_moisture_100_to_300cm, boolean is_day,
      Double sunshine_duration, Double shortwave_radiation, Double direct_radiation,
      Double diffuse_radiation, Double direct_normal_irradiance, Double global_tilted_irradiance,
      Double terrestrial_radiation, Double shortwave_radiation_instant,
      Double direct_radiation_instant, Double diffuse_radiation_instant,
      Double direct_normal_irradiance_instant, Double global_tilted_irradiance_instant,
      Double terrestrial_radiation_instant) {
    this.location = location;
    this.timestamp = timestamp;
    this.temperature_2m = temperature_2m;
    this.relative_humidity_2m = relative_humidity_2m;
    this.dew_point_2m = dew_point_2m;
    this.apparent_temperature = apparent_temperature;
    this.precipitation = precipitation;
    this.rain = rain;
    this.showers = showers;
    this.snowfall = snowfall;
    this.snow_depth = snow_depth;
    this.weather_code = weather_code;
    this.pressure_msl = pressure_msl;
    this.surface_pressure = surface_pressure;
    this.cloud_cover = cloud_cover;
    this.cloud_cover_low = cloud_cover_low;
    this.cloud_cover_mid = cloud_cover_mid;
    this.cloud_cover_high = cloud_cover_high;
    this.visibility = visibility;
    this.et0_fao_evapotranspiration = et0_fao_evapotranspiration;
    this.vapour_pressure_deficit = vapour_pressure_deficit;
    this.wind_speed_10m = wind_speed_10m;
    this.wind_speed_40m = wind_speed_40m;
    this.wind_speed_80m = wind_speed_80m;
    this.wind_speed_120m = wind_speed_120m;
    this.wind_direction_10m = wind_direction_10m;
    this.wind_direction_40m = wind_direction_40m;
    this.wind_direction_80m = wind_direction_80m;
    this.wind_direction_120m = wind_direction_120m;
    this.wind_gusts_10m = wind_gusts_10m;
    this.surface_temperature = surface_temperature;
    this.soil_temperature_0_to_10cm = soil_temperature_0_to_10cm;
    this.soil_temperature_10_to_35cm = soil_temperature_10_to_35cm;
    this.soil_temperature_35_to_100cm = soil_temperature_35_to_100cm;
    this.soil_temperature_100_to_300cm = soil_temperature_100_to_300cm;
    this.soil_moisture_0_to_10cm = soil_moisture_0_to_10cm;
    this.soil_moisture_10_to_35cm = soil_moisture_10_to_35cm;
    this.soil_moisture_35_to_100cm = soil_moisture_35_to_100cm;
    this.soil_moisture_100_to_300cm = soil_moisture_100_to_300cm;
    this.is_day = is_day;
    this.sunshine_duration = sunshine_duration;
    this.shortwave_radiation = shortwave_radiation;
    this.direct_radiation = direct_radiation;
    this.diffuse_radiation = diffuse_radiation;
    this.direct_normal_irradiance = direct_normal_irradiance;
    this.global_tilted_irradiance = global_tilted_irradiance;
    this.terrestrial_radiation = terrestrial_radiation;
    this.shortwave_radiation_instant = shortwave_radiation_instant;
    this.direct_radiation_instant = direct_radiation_instant;
    this.diffuse_radiation_instant = diffuse_radiation_instant;
    this.direct_normal_irradiance_instant = direct_normal_irradiance_instant;
    this.global_tilted_irradiance_instant = global_tilted_irradiance_instant;
    this.terrestrial_radiation_instant = terrestrial_radiation_instant;
  }


  @Override
  public String toString() {
    return "HourlyForecast{" +
        "id=" + id +
        ", location=" +  location +
        ", timestamp=" + timestamp +
        ", temperature_2m=" + temperature_2m +
        ", relative_humidity_2m=" + relative_humidity_2m +
        ", dew_point_2m=" + dew_point_2m +
        ", apparent_temperature=" + apparent_temperature +
        ", precipitation=" + precipitation +
        ", rain=" + rain +
        ", showers=" + showers +
        ", snowfall=" + snowfall +
        ", snow_depth=" + snow_depth +
        ", weather_code=" + weather_code +
        ", pressure_msl=" + pressure_msl +
        ", surface_pressure=" + surface_pressure +
        ", cloud_cover=" + cloud_cover +
        ", cloud_cover_low=" + cloud_cover_low +
        ", cloud_cover_mid=" + cloud_cover_mid +
        ", cloud_cover_high=" + cloud_cover_high +
        ", visibility=" + visibility +
        ", et0_fao_evapotranspiration=" + et0_fao_evapotranspiration +
        ", vapour_pressure_deficit=" + vapour_pressure_deficit +
        ", wind_speed_10m=" + wind_speed_10m +
        ", wind_speed_40m=" + wind_speed_40m +
        ", wind_speed_80m=" + wind_speed_80m +
        ", wind_speed_120m=" + wind_speed_120m +
        ", wind_direction_10m=" + wind_direction_10m +
        ", wind_direction_40m=" + wind_direction_40m +
        ", wind_direction_80m=" + wind_direction_80m +
        ", wind_direction_120m=" + wind_direction_120m +
        ", wind_gusts_10m=" + wind_gusts_10m +
        ", surface_temperature=" + surface_temperature +
        ", soil_temperature_0_to_10cm=" + soil_temperature_0_to_10cm +
        ", soil_temperature_10_to_35cm=" + soil_temperature_10_to_35cm +
        ", soil_temperature_35_to_100cm=" + soil_temperature_35_to_100cm +
        ", soil_temperature_100_to_300cm=" + soil_temperature_100_to_300cm +
        ", soil_moisture_0_to_10cm=" + soil_moisture_0_to_10cm +
        ", soil_moisture_10_to_35cm=" + soil_moisture_10_to_35cm +
        ", soil_moisture_35_to_100cm=" + soil_moisture_35_to_100cm +
        ", soil_moisture_100_to_300cm=" + soil_moisture_100_to_300cm +
        ", is_day=" + is_day +
        ", sunshine_duration=" + sunshine_duration +
        ", shortwave_radiation=" + shortwave_radiation +
        ", direct_radiation=" + direct_radiation +
        ", diffuse_radiation=" + diffuse_radiation +
        ", direct_normal_irradiance=" + direct_normal_irradiance +
        ", global_tilted_irradiance=" + global_tilted_irradiance +
        ", terrestrial_radiation=" + terrestrial_radiation +
        ", shortwave_radiation_instant=" + shortwave_radiation_instant +
        ", direct_radiation_instant=" + direct_radiation_instant +
        ", diffuse_radiation_instant=" + diffuse_radiation_instant +
        ", direct_normal_irradiance_instant=" + direct_normal_irradiance_instant +
        ", global_tilted_irradiance_instant=" + global_tilted_irradiance_instant +
        ", terrestrial_radiation_instant=" + terrestrial_radiation_instant +
        '}';
  }

}
