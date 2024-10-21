package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import org.json.JSONObject;

import java.util.List;

public interface IAlertCondition {
    String getAlertTitle();
    List<WeatherAlert> getAlerts();
    JSONObject toJsonObject();
}
