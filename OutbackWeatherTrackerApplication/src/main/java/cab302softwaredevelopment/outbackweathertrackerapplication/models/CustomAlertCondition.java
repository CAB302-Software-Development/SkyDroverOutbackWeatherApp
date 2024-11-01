package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import lombok.Getter;
import lombok.Setter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CustomAlertCondition {
    @Getter @Setter
    private String alertTitle = null;
    @Getter @Setter
    private String message = null;
    @Getter @Setter
    private boolean enabled = true;
    private boolean forecastType; // True = DailyForecast; False = HourlyForecast;
    private List<FilterValue> filters = new ArrayList<>();
    private record FilterValue (String field, Boolean isGreaterOrEqual, String value) {}

    public CustomAlertCondition(boolean forecastType) {
        this.forecastType = forecastType;
    }

    public void addAlertCondition(String field, boolean isGreaterOrEqual, String value) {
        filters.add(new FilterValue(field, isGreaterOrEqual, value));
    }

    public Optional<WeatherAlert> getAlert(Location location) {
        if (!enabled) return Optional.empty();

        // Set defaults
        long timestampGE = 0;
        long timestampLE = 86400; // 1 day
        int countGE = 0;
        int countLE = Integer.MAX_VALUE;

        DateData now = new DateData(LocalDateTime.now());
        long currentTimeEpoch = now.getCurrentEpoch();

        DailyForecastDAO.DailyForecastQuery dailyForecastQuery = new DailyForecastDAO.DailyForecastQuery();
        HourlyForecastDAO.HourlyForecastQuery hourlyForecastQuery = new HourlyForecastDAO.HourlyForecastQuery();

        for (FilterValue filter : filters) {
            if (filter.field.equals("timestamp")) {
                if (filter.isGreaterOrEqual) {
                    timestampGE = Integer.parseInt(filter.value) + currentTimeEpoch;
                } else {
                    timestampLE = Integer.parseInt(filter.value) + currentTimeEpoch;
                }
            } else if (filter.field.equals("count")) {
                if (filter.isGreaterOrEqual) {
                    countGE = Integer.parseInt(filter.value);
                } else {
                    countLE = Integer.parseInt(filter.value);
                }
            } else {
                if (forecastType) {
                    if (filter.isGreaterOrEqual) {
                        dailyForecastQuery.whereFieldGE(filter.field, filter.value);
                    } else {
                        dailyForecastQuery.whereFieldLE(filter.field, filter.value);
                    }
                } else {
                    if (filter.isGreaterOrEqual) {
                        hourlyForecastQuery.whereFieldGE(filter.field, filter.value);
                    } else {
                        hourlyForecastQuery.whereFieldLE(filter.field, filter.value);
                    }
                }
            }
        }

        if (forecastType) {
            dailyForecastQuery.whereTimestampGE((int) timestampGE);
            dailyForecastQuery.whereTimestampLE((int) timestampLE);
            dailyForecastQuery.whereLocationId(location.getId());

            List<DailyForecast> results = dailyForecastQuery.getResults();
            if (results != null && results.size() >= countGE && results.size() <= countLE) {
                String[] data = results.stream().map(forecast -> {
                    StringBuilder returnString = new StringBuilder();
                    returnString.append("Location: ");
                    returnString.append(forecast.getLocation().getName());
                    returnString.append("Temperature MAX: ");
                    returnString.append(forecast.getTemperature_2m_max().toString());
                    returnString.append("Temperature MIN: ");
                    returnString.append(forecast.getTemperature_2m_min().toString());
                    returnString.append("Time: ");
                    LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(forecast.getTimestamp()), ZoneId.systemDefault());
                    returnString.append(time.format(DateTimeFormatter.ISO_DATE_TIME));
                    return returnString.toString();
                }).toArray(String[]::new);
                return Optional.of(new WeatherAlert(alertTitle, message, data));
            }
        } else {
            hourlyForecastQuery.whereTimestampGE((int) timestampGE);
            hourlyForecastQuery.whereTimestampLE((int) timestampLE);
            hourlyForecastQuery.whereLocationId(location.getId());

            List<HourlyForecast> results = hourlyForecastQuery.getResults();
            if (results != null && results.size() >= countGE && results.size() <= countLE) {
                String[] data = results.stream().map(forecast -> {
                    StringBuilder returnString = new StringBuilder();
                    returnString.append("Location: ");
                    returnString.append(forecast.getLocation().getName());
                    returnString.append("Temperature: ");
                    returnString.append(forecast.getTemperature_2m().toString());
                    returnString.append("Time: ");
                    LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(forecast.getTimestamp()), ZoneId.systemDefault());
                    returnString.append(time.format(DateTimeFormatter.ISO_DATE_TIME));
                    return returnString.toString();
                }).toArray(String[]::new);
                return Optional.of(new WeatherAlert(alertTitle, message, data));
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return getAlertTitle();
    }
}
