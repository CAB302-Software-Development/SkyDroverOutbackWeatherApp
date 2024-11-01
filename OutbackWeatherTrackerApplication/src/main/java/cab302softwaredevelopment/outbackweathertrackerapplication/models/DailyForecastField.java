package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import lombok.Getter;

@Getter
public enum DailyForecastField {
    COUNT("count", "Number of results"),
    TIMESTAMP("timestamp", "Timestamp"),
    WEATHER_CODE("weather_code", "Weather Code"),
    TEMPERATURE_2M_MAX("temperature_2m_max", "Max Temperature at 2m"),
    TEMPERATURE_2M_MIN("temperature_2m_min", "Min Temperature at 2m"),
    APPARENT_TEMPERATURE_MAX("apparent_temperature_max", "Max Apparent Temperature"),
    APPARENT_TEMPERATURE_MIN("apparent_temperature_min", "Min Apparent Temperature"),
    SUNRISE("sunrise", "Sunrise Time"),
    SUNSET("sunset", "Sunset Time"),
    DAYLIGHT_DURATION("daylight_duration", "Daylight Duration (Seconds)"),
    SUNSHINE_DURATION("sunshine_duration", "Sunshine Duration (Seconds)"),
    UV_INDEX_MAX("uv_index_max", "Max UV Index"),
    UV_INDEX_CLEAR_SKY_MAX("uv_index_clear_sky_max", "Max UV Index (Clear Sky)"),
    PRECIPITATION_SUM("precipitation_sum", "Total Precipitation"),
    RAIN_SUM("rain_sum", "Total Rain"),
    SHOWERS_SUM("showers_sum", "Total Showers"),
    SNOWFALL_SUM("snowfall_sum", "Total Snowfall"),
    PRECIPITATION_HOURS("precipitation_hours", "Hours of Precipitation"),
    WIND_SPEED_10M_MAX("wind_speed_10m_max", "Max Wind Speed at 10m"),
    WIND_GUSTS_10M_MAX("wind_gusts_10m_max", "Max Wind Gusts at 10m"),
    WIND_DIRECTION_10M_DOMINANT("wind_direction_10m_dominant", "Dominant Wind Direction at 10m"),
    SHORTWAVE_RADIATION_SUM("shortwave_radiation_sum", "Shortwave Radiation Sum"),
    ET0_FAO_EVAPOTRANSPIRATION("et0_fao_evapotranspiration", "ET₀ Reference Evapotranspiration");

    private final String fieldName;
    private final String humanReadable;

    DailyForecastField(String fieldName, String humanReadable) {
        this.fieldName = fieldName;
        this.humanReadable = humanReadable;
    }
}
