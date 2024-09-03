package cab302softwaredevelopment.outbackweathertrackerapplication.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "hourlyForecasts")
public class HourlyForecast {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Location location;

  @Column(name="timestamp")
  private Integer timestamp;

  @Column(name="temperature_2m")
  private Double temperature_2m;

  @Column(name="relative_humidity_2m")
  private Double relative_humidity_2m;

  @Column(name="dew_point_2m")
  private Double dew_point_2m;

  @Column(name="apparent_temperature")
  private Double apparent_temperature;

  @Column(name="precipitation")
  private Double precipitation;

  @Column(name="rain")

  private Double rain;

  @Column(name="showers")
  private Double showers;

  @Column(name="snowfall")
  private Double snowfall;

  @Column(name="snow_depth")
  private Double snow_depth;

  @Column(name="weather_code")
  private Integer weather_code;

  @Column(name="pressure_msl")
  private Double pressure_msl;

  @Column(name="surface_pressure")
  private Double surface_pressure;

  @Column(name="cloud_cover")
  private Double cloud_cover;

  @Column(name="cloud_cover_low")
  private Double cloud_cover_low;

  @Column(name="cloud_cover_mid")
  private Double cloud_cover_mid;

  @Column(name="cloud_cover_high")
  private Double cloud_cover_high;

  @Column(name="visibility")
  private Double visibility;

  @Column(name="et0_fao_evapotranspiration")
  private Double et0_fao_evapotranspiration;

  @Column(name="vapour_pressure_deficit")
  private Double vapour_pressure_deficit;

  @Column(name="wind_speed_10m")
  private Double wind_speed_10m;

  @Column(name="wind_speed_40m")
  private Double wind_speed_40m;

  @Column(name="wind_speed_80m")
  private Double wind_speed_80m;

  @Column(name="wind_speed_120m")
  private Double wind_speed_120m;

  @Column(name="wind_direction_10m")
  private Double wind_direction_10m;

  @Column(name="wind_direction_40m")
  private Double wind_direction_40m;

  @Column(name="wind_direction_80m")
  private Double wind_direction_80m;

  @Column(name="wind_direction_120m")
  private Double wind_direction_120m;

  @Column(name="wind_gusts_10m")
  private Double wind_gusts_10m;

  @Column(name="surface_temperature")
  private Double surface_temperature;

  @Column(name="soil_temperature_0_to_10cm")
  private Double soil_temperature_0_to_10cm;

  @Column(name="soil_temperature_10_to_35cm")
  private Double soil_temperature_10_to_35cm;

  @Column(name="soil_temperature_35_to_100cm")
  private Double soil_temperature_35_to_100cm;

  @Column(name="soil_temperature_100_to_300cm")
  private Double soil_temperature_100_to_300cm;

  @Column(name="soil_moisture_0_to_10cm")
  private Double soil_moisture_0_to_10cm;

  @Column(name="soil_moisture_10_to_35cm")
  private Double soil_moisture_10_to_35cm;

  @Column(name="soil_moisture_35_to_100cm")
  private Double soil_moisture_35_to_100cm;

  @Column(name="soil_moisture_100_to_300cm")
  private Double soil_moisture_100_to_300cm;

  @Column(name="is_day")
  private boolean is_day;

  @Column(name="sunshine_duration")
  private Double sunshine_duration;

  @Column(name="shortwave_radiation")
  private Double shortwave_radiation;

  @Column(name="direct_radiation")
  private Double direct_radiation;

  @Column(name="diffuse_radiation")
  private Double diffuse_radiation;

  @Column(name="direct_normal_irradiance")
  private Double direct_normal_irradiance;

  @Column(name="global_tilted_irradiance")
  private Double global_tilted_irradiance;

  @Column(name="terrestrial_radiation")
  private Double terrestrial_radiation;

  @Column(name="shortwave_radiation_instant")
  private Double shortwave_radiation_instant;

  @Column(name="direct_radiation_instant")
  private Double direct_radiation_instant;

  @Column(name="diffuse_radiation_instant")
  private Double diffuse_radiation_instant;

  @Column(name="direct_normal_irradiance_instant")
  private Double direct_normal_irradiance_instant;

  @Column(name="global_tilted_irradiance_instant")
  private Double global_tilted_irradiance_instant;

  @Column(name="terrestrial_radiation_instant")
  private Double terrestrial_radiation_instant;

  public HourlyForecast() {
  }

  public HourlyForecast(int id, Location location, int timestamp, double temperature_2m,
      double relative_humidity_2m, double dew_point_2m, double apparent_temperature,
      double precipitation, double rain, double showers, double snowfall, double snow_depth,
      int weather_code, double pressure_msl, double surface_pressure, double cloud_cover,
      double cloud_cover_low, double cloud_cover_mid, double cloud_cover_high, double visibility,
      double et0_fao_evapotranspiration, double vapour_pressure_deficit, double wind_speed_10m,
      double wind_speed_40m, double wind_speed_80m, double wind_speed_120m,
      double wind_direction_10m, double wind_direction_40m, double wind_direction_80m,
      double wind_direction_120m, double wind_gusts_10m, double surface_temperature,
      double soil_temperature_0_to_10cm, double soil_temperature_10_to_35cm,
      double soil_temperature_35_to_100cm, double soil_temperature_100_to_300cm,
      double soil_moisture_0_to_10cm, double soil_moisture_10_to_35cm,
      double soil_moisture_35_to_100cm, double soil_moisture_100_to_300cm, boolean is_day,
      double sunshine_duration, double shortwave_radiation, double direct_radiation,
      double diffuse_radiation, double direct_normal_irradiance, double global_tilted_irradiance,
      double terrestrial_radiation, double shortwave_radiation_instant,
      double direct_radiation_instant, double diffuse_radiation_instant,
      double direct_normal_irradiance_instant, double global_tilted_irradiance_instant,
      double terrestrial_radiation_instant) {
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

  public HourlyForecast(Location location, int timestamp, double temperature_2m,
      double relative_humidity_2m, double dew_point_2m, double apparent_temperature,
      double precipitation, double rain, double showers, double snowfall, double snow_depth,
      int weather_code, double pressure_msl, double surface_pressure, double cloud_cover,
      double cloud_cover_low, double cloud_cover_mid, double cloud_cover_high, double visibility,
      double et0_fao_evapotranspiration, double vapour_pressure_deficit, double wind_speed_10m,
      double wind_speed_40m, double wind_speed_80m, double wind_speed_120m,
      double wind_direction_10m, double wind_direction_40m, double wind_direction_80m,
      double wind_direction_120m, double wind_gusts_10m, double surface_temperature,
      double soil_temperature_0_to_10cm, double soil_temperature_10_to_35cm,
      double soil_temperature_35_to_100cm, double soil_temperature_100_to_300cm,
      double soil_moisture_0_to_10cm, double soil_moisture_10_to_35cm,
      double soil_moisture_35_to_100cm, double soil_moisture_100_to_300cm, boolean is_day,
      double sunshine_duration, double shortwave_radiation, double direct_radiation,
      double diffuse_radiation, double direct_normal_irradiance, double global_tilted_irradiance,
      double terrestrial_radiation, double shortwave_radiation_instant,
      double direct_radiation_instant, double diffuse_radiation_instant,
      double direct_normal_irradiance_instant, double global_tilted_irradiance_instant,
      double terrestrial_radiation_instant) {
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

  public int getId() {
    return id;
  }

  public Location getLocation() {
    return location;
  }

  public int getTimestamp() {
    return timestamp;
  }

  public double getTemperature_2m() {
    return temperature_2m;
  }

  public double getRelative_humidity_2m() {
    return relative_humidity_2m;
  }

  public double getDew_point_2m() {
    return dew_point_2m;
  }

  public double getApparent_temperature() {
    return apparent_temperature;
  }

  public double getPrecipitation() {
    return precipitation;
  }

  public double getRain() {
    return rain;
  }

  public double getShowers() {
    return showers;
  }

  public double getSnowfall() {
    return snowfall;
  }

  public double getSnow_depth() {
    return snow_depth;
  }

  public int getWeather_code() {
    return weather_code;
  }

  public double getPressure_msl() {
    return pressure_msl;
  }

  public double getSurface_pressure() {
    return surface_pressure;
  }

  public double getCloud_cover() {
    return cloud_cover;
  }

  public double getCloud_cover_low() {
    return cloud_cover_low;
  }

  public double getCloud_cover_mid() {
    return cloud_cover_mid;
  }

  public double getCloud_cover_high() {
    return cloud_cover_high;
  }

  public double getVisibility() {
    return visibility;
  }

  public double getEt0_fao_evapotranspiration() {
    return et0_fao_evapotranspiration;
  }

  public double getVapour_pressure_deficit() {
    return vapour_pressure_deficit;
  }

  public double getWind_speed_10m() {
    return wind_speed_10m;
  }

  public double getWind_speed_40m() {
    return wind_speed_40m;
  }

  public double getWind_speed_80m() {
    return wind_speed_80m;
  }

  public double getWind_speed_120m() {
    return wind_speed_120m;
  }

  public double getWind_direction_10m() {
    return wind_direction_10m;
  }

  public double getWind_direction_40m() {
    return wind_direction_40m;
  }

  public double getWind_direction_80m() {
    return wind_direction_80m;
  }

  public double getWind_direction_120m() {
    return wind_direction_120m;
  }

  public double getWind_gusts_10m() {
    return wind_gusts_10m;
  }

  public double getSurface_temperature() {
    return surface_temperature;
  }

  public double getSoil_temperature_0_to_10cm() {
    return soil_temperature_0_to_10cm;
  }

  public double getSoil_temperature_10_to_35cm() {
    return soil_temperature_10_to_35cm;
  }

  public double getSoil_temperature_35_to_100cm() {
    return soil_temperature_35_to_100cm;
  }

  public double getSoil_temperature_100_to_300cm() {
    return soil_temperature_100_to_300cm;
  }

  public double getSoil_moisture_0_to_10cm() {
    return soil_moisture_0_to_10cm;
  }

  public double getSoil_moisture_10_to_35cm() {
    return soil_moisture_10_to_35cm;
  }

  public double getSoil_moisture_35_to_100cm() {
    return soil_moisture_35_to_100cm;
  }

  public double getSoil_moisture_100_to_300cm() {
    return soil_moisture_100_to_300cm;
  }

  public boolean getIs_day() {
    return is_day;
  }

  public double getSunshine_duration() {
    return sunshine_duration;
  }

  public double getShortwave_radiation() {
    return shortwave_radiation;
  }

  public double getDirect_radiation() {
    return direct_radiation;
  }

  public double getDiffuse_radiation() {
    return diffuse_radiation;
  }

  public double getDirect_normal_irradiance() {
    return direct_normal_irradiance;
  }

  public double getGlobal_tilted_irradiance() {
    return global_tilted_irradiance;
  }

  public double getTerrestrial_radiation() {
    return terrestrial_radiation;
  }

  public double getShortwave_radiation_instant() {
    return shortwave_radiation_instant;
  }

  public double getDirect_radiation_instant() {
    return direct_radiation_instant;
  }

  public double getDiffuse_radiation_instant() {
    return diffuse_radiation_instant;
  }

  public double getDirect_normal_irradiance_instant() {
    return direct_normal_irradiance_instant;
  }

  public double getGlobal_tilted_irradiance_instant() {
    return global_tilted_irradiance_instant;
  }

  public double getTerrestrial_radiation_instant() {
    return terrestrial_radiation_instant;
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
