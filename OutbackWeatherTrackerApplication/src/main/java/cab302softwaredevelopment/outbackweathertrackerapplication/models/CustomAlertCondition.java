package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import lombok.Getter;
import lombok.Setter;
import java.time.*;
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

    public CustomAlertCondition(boolean forecastType) {
        this.forecastType = forecastType;
    }

    public void addAlertCondition(String field, boolean isGreaterOrEqual, String value) throws IllegalStateException {
        filters.stream()
                .filter(f -> f.getField().equals(field) && f.getIsGreaterOrEqual().equals(isGreaterOrEqual))
                .findAny().ifPresent(f -> {
            throw new IllegalStateException("Filter already exists.");
        });
        filters.add(new FilterValue(field, isGreaterOrEqual, value));
    }

    public Optional<WeatherAlert> getAlert(Location location) {
        if (!enabled) return Optional.empty();

        // Set defaults
        long timestampGE = 0;
        long timestampLE = 86400; // 1 day
        int countGE = 1;
        int countLE = Integer.MAX_VALUE;

        DateData now = new DateData(LocalDateTime.now());
        long currentTimeEpoch = now.getCurrentEpoch();

        DailyForecastDAO.DailyForecastQuery dailyForecastQuery = new DailyForecastDAO.DailyForecastQuery();
        HourlyForecastDAO.HourlyForecastQuery hourlyForecastQuery = new HourlyForecastDAO.HourlyForecastQuery();

        for (FilterValue filter : filters) {
            if (filter.getField().equals("timestamp")) {
                if (filter.getIsGreaterOrEqual()) {
                    timestampGE = Integer.parseInt(filter.getValue()) + currentTimeEpoch;
                } else {
                    timestampLE = Integer.parseInt(filter.getValue()) + currentTimeEpoch;
                }
            } else if (filter.getField().equals("count")) {
                if (filter.getIsGreaterOrEqual()) {
                    countGE = Integer.parseInt(filter.getValue());
                } else {
                    countLE = Integer.parseInt(filter.getValue());
                }
            } else {
                if (forecastType) {
                    if (filter.getIsGreaterOrEqual()) {
                        dailyForecastQuery.whereFieldGE(filter.getField(), filter.getValue());
                    } else {
                        dailyForecastQuery.whereFieldLE(filter.getField(), filter.getValue());
                    }
                } else {
                    if (filter.getIsGreaterOrEqual()) {
                        hourlyForecastQuery.whereFieldGE(filter.getField(), filter.getValue());
                    } else {
                        hourlyForecastQuery.whereFieldLE(filter.getField(), filter.getValue());
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
                String[] data = results.stream().map(DailyForecast::toString).toArray(String[]::new);
                return Optional.of(new WeatherAlert(alertTitle, message, data));
            }
        } else {
            hourlyForecastQuery.whereTimestampGE((int) timestampGE);
            hourlyForecastQuery.whereTimestampLE((int) timestampLE);
            hourlyForecastQuery.whereLocationId(location.getId());

            List<HourlyForecast> results = hourlyForecastQuery.getResults();
            if (results != null && results.size() >= countGE && results.size() <= countLE) {
                String[] data = results.stream().map(HourlyForecast::toString).toArray(String[]::new);
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
