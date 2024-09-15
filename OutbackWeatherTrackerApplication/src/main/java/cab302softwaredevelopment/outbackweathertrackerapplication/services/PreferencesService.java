package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

class PreferenceData {
    @SerializedName("currentTheme")
    public Theme currentTheme;
    public HashMap<String, WidgetInfo[]> dashboardLayouts;
    public String selectedLayout;
}

public class PreferencesService {
    private static PreferenceData data;

    public static void loadPreferences() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader("user_preferences.json")) {
            data = gson.fromJson(reader, PreferenceData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static WidgetInfo[] getCurrentLayout() {
        return data.dashboardLayouts.get(data.selectedLayout);
    }

    public static String getCurrentThemeData() {
        return Objects.requireNonNull(ApplicationEntry.class.getResource(data.currentTheme.getFilePath())).toExternalForm();
    }

    public static Theme getCurrentTheme() {
        return data.currentTheme;
    }

    public static void setCurrentTheme(Theme newTheme) {
        data.currentTheme = newTheme;
        savePreferences();
    }

    public String[] getLayouts() {
        return data.dashboardLayouts.keySet().toArray(String[]::new);
    }

    public static void setCurrentLayout(String name) {
        if (data.dashboardLayouts.containsKey(name)) {
            data.selectedLayout = name;
        }
    }

    public static void savePreferences() {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("user_preferences.json")) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
