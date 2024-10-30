package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.AlertsService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;

public class BOMWeatherAlert implements IAlertCondition {
    @Getter
    private Location location;
    private String region;

    public BOMWeatherAlert(Location location) {
        this.location = location;
    }
    public BOMWeatherAlert(long location) {
        this.location = LocationService.getInstance().getById(location);
        if (this.location == null) {
            throw new IllegalArgumentException("Location not found");
        }
    }

    @Override
    public String getAlertTitle() {
        String locationStr = location.getName().length() > 20 ?
                location.getName().substring(0, 20) + "..." :
                location.getName();
        return "BOM Weather Alerts For: " + locationStr;
    }

    @Override
    public List<WeatherAlert> getAlerts() {
        return AlertsService.getInstance().getBOMAlertsForLocation(location);
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "BOMWeatherAlert");
        jsonObject.put("location", (long) location.getId());
        return jsonObject;
    }
}
