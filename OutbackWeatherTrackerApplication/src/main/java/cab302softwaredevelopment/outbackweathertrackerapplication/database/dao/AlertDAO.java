package cab302softwaredevelopment.outbackweathertrackerapplication.database.dao;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.IAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.BOMWeatherAlert;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class AlertDAO {
    private static final String ALERT_PREFERENCES_KEY = "alert_preferences";
    private Preferences preferences;

    public AlertDAO() {
        preferences = Preferences.userNodeForPackage(AlertDAO.class);
    }

    public void saveAlertPreference(IAlertCondition alertCondition) {
        List<IAlertCondition> currentPreferences = getAllAlertPreferences();
        currentPreferences.add(alertCondition);
        saveAllAlertPreferences(currentPreferences);
    }

    public List<IAlertCondition> getAllAlertPreferences() {
        String jsonString = preferences.get(ALERT_PREFERENCES_KEY, "[]");
        JSONArray jsonArray = new JSONArray(jsonString);
        List<IAlertCondition> alertConditions = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");

            if (type.equals("BOMWeatherAlert")) {
                alertConditions.add(new BOMWeatherAlert(jsonObject.getLong("location")));
            } else if (type.equals("CustomAlertCondition")) {
                String name = jsonObject.getString("name");
                String message = jsonObject.getString("message");
                double value = jsonObject.getDouble("value");
                int threshold = jsonObject.getInt("threshold");
                String threshParam = jsonObject.getString("threshParam");
                CustomAlertCondition customAlert = new CustomAlertCondition(
                        name,
                        message,
                        (DailyForecastDAO.DailyForecastQuery) null,
                        null,
                        null,
                        threshold,
                        threshParam,
                        value);
                alertConditions.add(customAlert);
            }
        }

        return alertConditions;
    }

    private void saveAllAlertPreferences(List<IAlertCondition> alertConditions) {
        JSONArray jsonArray = new JSONArray();
        for (IAlertCondition alertCondition : alertConditions) {
            JSONObject jsonObject = alertCondition.toJsonObject();
            jsonArray.put(jsonObject);
        }
        preferences.put(ALERT_PREFERENCES_KEY, jsonArray.toString());
    }

    public void clearAllAlertPreferences() {
        preferences.remove(ALERT_PREFERENCES_KEY);
    }

    public void deleteAlertPreference(IAlertCondition alertCondition) {
        List<IAlertCondition> currentPreferences = getAllAlertPreferences();
        currentPreferences.removeIf(existingCondition -> existingCondition.toJsonObject().toString().equals(alertCondition.toJsonObject().toString()));
        saveAllAlertPreferences(currentPreferences);
    }
}
