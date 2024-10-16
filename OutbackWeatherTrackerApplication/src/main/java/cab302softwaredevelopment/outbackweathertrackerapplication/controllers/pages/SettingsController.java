package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SettingsController extends BasePage implements Initializable {
    @FXML
    public ComboBox<String> cboThemes;

    @Override
    public void updateData() {
        String[] layouts = getLayouts();
        ObservableList<String> options = FXCollections.observableArrayList(layouts);
        cboThemes.setItems(options);
    }

    /**
     * Toggle between the light and dark theme
     */
    @FXML
    public void swapTheme() {
        switch (getCurrentTheme()) {
            case Dark -> setCurrentTheme(Theme.Light);
            case Light -> setCurrentTheme(Theme.Dark);
        }
        MainController.refreshDisplay();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.updateData();
    }

    @FXML
    private void comboAction(ActionEvent event) {
        setSelectedLayout(cboThemes.getValue());
        MainController.refreshDisplay();
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
        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setCurrentTheme(newTheme);
        UserService.updateCurrentAccount(updateModel);
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
            AccountUpdateModel updateModel = new AccountUpdateModel();
            updateModel.setSelectedLayout(name);
            UserService.updateCurrentAccount(updateModel);
        }
    }
}