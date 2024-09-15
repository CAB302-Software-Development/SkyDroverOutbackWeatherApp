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
import org.hibernate.annotations.*;



@Entity(name = "HourlyForecast")
@Table(name = "hourlyForecast", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_id", "timestamp"})
})
public class HourlyForecast {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter
  private Integer id;

  @ManyToOne
  @PrimaryKeyJoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Getter
  private Location location;

  @Column(name="timestamp")
  @Getter
  private Integer timestamp;

  @Column(name="temperature_2m")
  @Getter
  private Double temperature_2m;

  @Column(name="relative_humidity_2m")
  @Getter
  private Double relative_humidity_2m;

  @Column(name="dew_point_2m")
  @Getter
  private Double dew_point_2m;

  @Column(name="apparent_temperature")
  @Getter
  private Double apparent_temperature;

  @Column(name="precipitation")
  @Getter
  private Double precipitation;

  @Column(name="rain")
  @Getter
  private Double rain;

  @Column(name="showers")
  @Getter
  private Double showers;

  @Column(name="snowfall")
  @Getter
  private Double snowfall;

  @Column(name="snow_depth")
  @Getter
  private Double snow_depth;

  @Column(name="weather_code")
  @Getter
  private Integer weather_code;

  @Column(name="pressure_msl")
  @Getter
  private Double pressure_msl;

  @Column(name="surface_pressure")
  @Getter
  private Double surface_pressure;

  @Column(name="cloud_cover")
  @Getter
  private Double cloud_cover;

  @Column(name="cloud_cover_low")
  @Getter
  private Double cloud_cover_low;

  @Column(name="cloud_cover_mid")
  @Getter
  private Double cloud_cover_mid;

  @Column(name="cloud_cover_high")
  @Getter
  private Double cloud_cover_high;

  @Column(name="visibility")
  @Getter
  private Double visibility;

  @Column(name="et0_fao_evapotranspiration")
  @Getter
  private Double et0_fao_evapotranspiration;

  @Column(name="vapour_pressure_deficit")
  @Getter
  private Double vapour_pressure_deficit;

  @Column(name="wind_speed_10m")
  @Getter
  private Double wind_speed_10m;

  @Column(name="wind_speed_40m")
  @Getter
  private Double wind_speed_40m;

  @Column(name="wind_speed_80m")
  @Getter
  private Double wind_speed_80m;

  @Column(name="wind_speed_120m")
  @Getter
  private Double wind_speed_120m;

  @Column(name="wind_direction_10m")
  @Getter
  private Double wind_direction_10m;

  @Column(name="wind_direction_40m")
  @Getter
  private Double wind_direction_40m;

  @Column(name="wind_direction_80m")
  @Getter
  private Double wind_direction_80m;

  @Column(name="wind_direction_120m")
  @Getter
  private Double wind_direction_120m;

  @Column(name="wind_gusts_10m")
  @Getter
  private Double wind_gusts_10m;

  @Column(name="surface_temperature")
  @Getter
  private Double surface_temperature;

  @Column(name="soil_temperature_0_to_10cm")
  @Getter
  private Double soil_temperature_0_to_10cm;

  @Column(name="soil_temperature_10_to_35cm")
  @Getter
  private Double soil_temperature_10_to_35cm;

  @Column(name="soil_temperature_35_to_100cm")
  @Getter
  private Double soil_temperature_35_to_100cm;

  @Column(name="soil_temperature_100_to_300cm")
  @Getter
  private Double soil_temperature_100_to_300cm;

  @Column(name="soil_moisture_0_to_10cm")
  @Getter
  private Double soil_moisture_0_to_10cm;

  @Column(name="soil_moisture_10_to_35cm")
  @Getter
  private Double soil_moisture_10_to_35cm;

  @Column(name="soil_moisture_35_to_100cm")
  @Getter
  private Double soil_moisture_35_to_100cm;

  @Column(name="soil_moisture_100_to_300cm")
  @Getter
  private Double soil_moisture_100_to_300cm;

  @Column(name="is_day")
  @Getter
  private boolean is_day;

  @Column(name="sunshine_duration")
  @Getter
  private Double sunshine_duration;

  @Column(name="shortwave_radiation")
  @Getter
  private Double shortwave_radiation;

  @Column(name="direct_radiation")
  @Getter
  private Double direct_radiation;

  @Column(name="diffuse_radiation")
  @Getter
  private Double diffuse_radiation;

  @Column(name="direct_normal_irradiance")
  @Getter
  private Double direct_normal_irradiance;

  @Column(name="global_tilted_irradiance")
  @Getter
  private Double global_tilted_irradiance;

  @Column(name="terrestrial_radiation")
  @Getter
  private Double terrestrial_radiation;

  @Column(name="shortwave_radiation_instant")
  @Getter
  private Double shortwave_radiation_instant;

  @Column(name="direct_radiation_instant")
  @Getter
  private Double direct_radiation_instant;

  @Column(name="diffuse_radiation_instant")
  @Getter
  private Double diffuse_radiation_instant;

  @Column(name="direct_normal_irradiance_instant")
  @Getter
  private Double direct_normal_irradiance_instant;

  @Column(name="global_tilted_irradiance_instant")
  @Getter
  private Double global_tilted_irradiance_instant;

  @Column(name="terrestrial_radiation_instant")
  @Getter
  private Double terrestrial_radiation_instant;

  public HourlyForecast() {
  }

  public HourlyForecast(Integer id, Location location, Integer timestamp, Double temperature_2m,
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
    this.id = id;
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
        ", location_=" +  location +
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
