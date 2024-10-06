package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import com.google.gson.annotations.SerializedName;

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

    }

    public static WidgetInfo[] getCurrentLayout() {
        HashMap<String, WidgetInfo[]> dashboardLayouts = LoginState.getCurrentAccount().getDashboardLayouts();
        return dashboardLayouts.get(LoginState.getCurrentAccount().getSelectedLayout());
    }

    public static String getSelectedLayout() {
        return LoginState.getCurrentAccount().getSelectedLayout();
    }

    public static List<String> getCurrentThemeData() {
        Theme currentTheme = LoginState.getCurrentAccount().getCurrentTheme();
        String iconsPath = Objects.requireNonNull(ApplicationEntry.class.getResource("themes/icons.css")).toExternalForm();
        String themePath = Objects.requireNonNull(ApplicationEntry.class.getResource(currentTheme.getFilePath())).toExternalForm();
        return Arrays.asList(stylePath, themePath, iconsPath);
    }

    /**
     * Retrieves the current theme of the application.
     *
     * @return The current theme.
     */
    public static Theme getCurrentTheme() {
        return LoginState.getCurrentAccount().getCurrentTheme();
    }

    /**
     * Sets a new theme for the application and saves the preferences to file.
     *
     * @param newTheme The new theme to apply.
     */
    public static void setCurrentTheme(Theme newTheme) {
        Account currentAccount = LoginState.getCurrentAccount();
        currentAccount.setCurrentTheme(newTheme);
        LoginState.updateAccount(currentAccount);
    }

    /**
     * Gets a list of available layouts for the dashboard.
     *
     * @return An array of layout names.
     */
    public static String[] getLayouts() {
        HashMap<String, WidgetInfo[]> dashboardLayouts = LoginState.getCurrentAccount().getDashboardLayouts();
        return dashboardLayouts.keySet().toArray(String[]::new);
    }

    /**
     * Sets the current layout for the dashboard.
     *
     * @param name The name of the layout to set.
     */
    public static void setSelectedLayout(String name) {
        if (Arrays.asList(getLayouts()).contains(name)) {
            Account currentAccount = LoginState.getCurrentAccount();
            currentAccount.setSelectedLayout(name);
            LoginState.updateAccount(currentAccount);
        }
    }

    /**
     * Saves the user preferences to a JSON file.
     */
    public static void savePreferences() {

    }
}
