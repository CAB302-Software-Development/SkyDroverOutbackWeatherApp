package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.HashMap;

public class PreferencesService {
    @SerializedName("currentTheme")
    public Theme currentTheme;
    HashMap<String, WidgetInfo[]> dashboardLayouts;
    String selectedLayout;

    public static PreferencesService loadPreferences() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader("user_preferences.json")) {
            return gson.fromJson(reader, PreferencesService.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WidgetInfo[] getCurrentLayout() {
        return dashboardLayouts.get(selectedLayout);
    }

    public String[] getLayouts() {
        return dashboardLayouts.keySet().toArray(String[]::new);
    }

    public void setCurrentLayout(String name) {
        if (dashboardLayouts.containsKey(name)) {
            selectedLayout = name;
        }
    }

    public void savePreferences() {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("user_preferences.json")) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
