package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;

@Getter
public enum HourlyForecastField {
    COUNT("count", "Number of results"),
    TIMESTAMP("timestamp", "Timestamp"),
    TEMPERATURE_2M("temperature_2m", "Temperature at 2m"),
    RELATIVE_HUMIDITY_2M("relative_humidity_2m", "Relative Humidity at 2m"),
    DEW_POINT_2M("dew_point_2m", "Dew Point at 2m"),
    APPARENT_TEMPERATURE("apparent_temperature", "Apparent Temperature"),
    PRECIPITATION("precipitation", "Precipitation"),
    RAIN("rain", "Rain"),
    SHOWERS("showers", "Showers"),
    SNOWFALL("snowfall", "Snowfall"),
    SNOW_DEPTH("snow_depth", "Snow Depth"),
    WEATHER_CODE("weather_code", "Weather Code"),
    PRESSURE_MSL("pressure_msl", "Mean Sea Level Pressure"),
    SURFACE_PRESSURE("surface_pressure", "Surface Pressure"),
    CLOUD_COVER("cloud_cover", "Cloud Cover"),
    CLOUD_COVER_LOW("cloud_cover_low", "Low Cloud Cover"),
    CLOUD_COVER_MID("cloud_cover_mid", "Mid Cloud Cover"),
    CLOUD_COVER_HIGH("cloud_cover_high", "High Cloud Cover"),
    VISIBILITY("visibility", "Visibility"),
    ET0_FAO_EVAPOTRANSPIRATION("et0_fao_evapotranspiration", "Evapotranspiration (FAO)"),
    VAPOUR_PRESSURE_DEFICIT("vapour_pressure_deficit", "Vapour Pressure Deficit"),
    WIND_SPEED_10M("wind_speed_10m", "Wind Speed at 10m"),
    WIND_SPEED_40M("wind_speed_40m", "Wind Speed at 40m"),
    WIND_SPEED_80M("wind_speed_80m", "Wind Speed at 80m"),
    WIND_SPEED_120M("wind_speed_120m", "Wind Speed at 120m"),
    WIND_DIRECTION_10M("wind_direction_10m", "Wind Direction at 10m"),
    WIND_DIRECTION_40M("wind_direction_40m", "Wind Direction at 40m"),
    WIND_DIRECTION_80M("wind_direction_80m", "Wind Direction at 80m"),
    WIND_DIRECTION_120M("wind_direction_120m", "Wind Direction at 120m"),
    WIND_GUSTS_10M("wind_gusts_10m", "Wind Gusts at 10m"),
    SURFACE_TEMPERATURE("surface_temperature", "Surface Temperature"),
    SOIL_TEMPERATURE_0_TO_10CM("soil_temperature_0_to_10cm", "Soil Temperature (0-10cm)"),
    SOIL_TEMPERATURE_10_TO_35CM("soil_temperature_10_to_35cm", "Soil Temperature (10-35cm)"),
    SOIL_TEMPERATURE_35_TO_100CM("soil_temperature_35_to_100cm", "Soil Temperature (35-100cm)"),
    SOIL_TEMPERATURE_100_TO_300CM("soil_temperature_100_to_300cm", "Soil Temperature (100-300cm)"),
    SOIL_MOISTURE_0_TO_10CM("soil_moisture_0_to_10cm", "Soil Moisture (0-10cm)"),
    SOIL_MOISTURE_10_TO_35CM("soil_moisture_10_to_35cm", "Soil Moisture (10-35cm)"),
    SOIL_MOISTURE_35_TO_100CM("soil_moisture_35_to_100cm", "Soil Moisture (35-100cm)"),
    SOIL_MOISTURE_100_TO_300CM("soil_moisture_100_to_300cm", "Soil Moisture (100-300cm)"),
    IS_DAY("is_day", "Is Daytime"),
    SUNSHINE_DURATION("sunshine_duration", "Sunshine Duration"),
    SHORTWAVE_RADIATION("shortwave_radiation", "Shortwave Radiation"),
    DIRECT_RADIATION("direct_radiation", "Direct Radiation"),
    DIFFUSE_RADIATION("diffuse_radiation", "Diffuse Radiation"),
    DIRECT_NORMAL_IRRADIANCE("direct_normal_irradiance", "Direct Normal Irradiance"),
    GLOBAL_TILTED_IRRADIANCE("global_tilted_irradiance", "Global Tilted Irradiance"),
    TERRESTRIAL_RADIATION("terrestrial_radiation", "Terrestrial Radiation"),
    SHORTWAVE_RADIATION_INSTANT("shortwave_radiation_instant", "Shortwave Radiation (Instantaneous)"),
    DIRECT_RADIATION_INSTANT("direct_radiation_instant", "Direct Radiation (Instantaneous)"),
    DIFFUSE_RADIATION_INSTANT("diffuse_radiation_instant", "Diffuse Radiation (Instantaneous)"),
    DIRECT_NORMAL_IRRADIANCE_INSTANT("direct_normal_irradiance_instant", "Direct Normal Irradiance (Instantaneous)"),
    GLOBAL_TILTED_IRRADIANCE_INSTANT("global_tilted_irradiance_instant", "Global Tilted Irradiance (Instantaneous)"),
    TERRESTRIAL_RADIATION_INSTANT("terrestrial_radiation_instant", "Terrestrial Radiation (Instantaneous)");

    private final String fieldName;
    private final String humanReadable;

    HourlyForecastField(String fieldName, String humanReadable) {
        this.fieldName = fieldName;
        this.humanReadable = humanReadable;
    }
}
