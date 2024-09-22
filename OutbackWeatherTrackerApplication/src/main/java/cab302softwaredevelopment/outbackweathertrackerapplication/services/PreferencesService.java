package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.*;

class PreferenceData {
    @SerializedName("currentTheme")
    public Theme currentTheme;
    public HashMap<String, WidgetInfo[]> dashboardLayouts;
    public String selectedLayout;
}

public class PreferencesService {
    private static PreferenceData data;
    private static final String stylePath = Objects.requireNonNull(ApplicationEntry.class.getResource("themes/style.css")).toExternalForm();

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

    public static String getSelectedLayout() {
        return data.selectedLayout;
    }

    public static List<String> getCurrentThemeData() {
        String iconsPath = Objects.requireNonNull(ApplicationEntry.class.getResource("themes/icons.css")).toExternalForm();
        String themePath = Objects.requireNonNull(ApplicationEntry.class.getResource(data.currentTheme.getFilePath())).toExternalForm();
        return Arrays.asList(stylePath, themePath, iconsPath);
    }

    /**
     * Retrieves the current theme of the application.
     *
     * @return The current theme.
     */
    public static Theme getCurrentTheme() {
        return data.currentTheme;
    }

    /**
     * Sets a new theme for the application and saves the preferences to file.
     *
     * @param newTheme The new theme to apply.
     */
    public static void setCurrentTheme(Theme newTheme) {
        data.currentTheme = newTheme;
        savePreferences();
    }

    /**
     * Gets a list of available layouts for the dashboard.
     *
     * @return An array of layout names.
     */
    public static String[] getLayouts() {
        return data.dashboardLayouts.keySet().toArray(String[]::new);
    }

    /**
     * Sets the current layout for the dashboard.
     *
     * @param name The name of the layout to set.
     */
    public static void setSelectedLayout(String name) {
        if (data.dashboardLayouts.containsKey(name)) {
            data.selectedLayout = name;
        }
    }

    /**
     * Saves the user preferences to a JSON file.
     */
    public static void savePreferences() {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("user_preferences.json")) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
