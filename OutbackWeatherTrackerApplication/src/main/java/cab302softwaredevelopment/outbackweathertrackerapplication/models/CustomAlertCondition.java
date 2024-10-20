package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CustomAlertCondition implements IAlertCondition {
    private String alertTitle = null;
    private String message = null;
    private DailyForecastDAO.DailyForecastQuery dailyForecastQuery = null;
    private HourlyForecastDAO.HourlyForecastQuery hourlyForecastQuery = null;
    private Long timestampGE = null;
    private Long timestampLE = null;
    private int threshold = 0;
    private String threshParam = null;
    private double value = 0;

    public CustomAlertCondition(String name, String message, DailyForecastDAO.DailyForecastQuery dailyForecastQuery,
                                Long timestampGE, Long timestampLE, int threshold, String threshParam, double value) {
        this.alertTitle = name;
        this.message = message;
        this.dailyForecastQuery = dailyForecastQuery;
        this.timestampGE = timestampGE;
        this.timestampLE = timestampLE;
        this.threshold = threshold;
        this.threshParam = threshParam;
        this.value = value;
    }

    public CustomAlertCondition(String name, String message, HourlyForecastDAO.HourlyForecastQuery hourlyForecastQuery,
                                Long timestampGE, Long timestampLE, int threshold, String threshParam, double value) {
        this.alertTitle = name;
        this.message = message;
        this.hourlyForecastQuery = hourlyForecastQuery;
        this.timestampGE = timestampGE;
        this.timestampLE = timestampLE;
        this.threshold = threshold;
        this.threshParam = threshParam;
        this.value = value;
    }

    @Override
    public List<WeatherAlert> getAlerts() {
        boolean triggered = false;
        DateData now = new DateData(LocalDateTime.now());
        long currentTimeEpoch = now.getCurrentEpoch();
        if (dailyForecastQuery != null) {
            if (timestampGE != null) dailyForecastQuery.whereTimestampGE((int)(currentTimeEpoch + timestampGE));
            if (timestampLE != null) dailyForecastQuery.whereTimestampLE((int)(currentTimeEpoch + timestampLE));
            List<DailyForecast> results = dailyForecastQuery.getResults();
            switch (threshParam) {
                case "temperature_2m_max": {
                    if (results.get(this.threshold) != null &&
                            results.get(this.threshold).getTemperature_2m_max() > value) {
                        triggered = true;
                    }
                }
            }
        } else if (hourlyForecastQuery != null) {
            if (timestampGE != null) hourlyForecastQuery.whereTimestampGE((int)(currentTimeEpoch + timestampGE));
            if (timestampLE != null) hourlyForecastQuery.whereTimestampLE((int)(currentTimeEpoch + timestampLE));
            List<HourlyForecast> results = hourlyForecastQuery.getResults();
            switch (threshParam) {
                case "temperature_2m": {
                    if (results.get(this.threshold) != null &&
                            results.get(this.threshold).getTemperature_2m() > value) {
                        triggered = true;
                    }
                }
            }
        }

        if (triggered) {
            WeatherAlert alert = new WeatherAlert(message, null, now.toString());
            return List.of(alert);
        } else {
            return List.of();
        }
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "CustomAlertCondition");
        jsonObject.put("name", getAlertTitle());
        jsonObject.put("message", getMessage());
        jsonObject.put("value", getValue());
        jsonObject.put("threshold", getThreshold());
        jsonObject.put("threshParam", getThreshParam());
        return jsonObject;
    }

    @Override
    public String toString() {
        return getAlertTitle();
    }
}
