package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    public ComboBox<String> cboThemes;

    /**
     * Toggle between the light and dark theme
     */
    @FXML
    public void swapTheme() {
        switch (PreferencesService.getCurrentTheme()) {
            case Dark -> PreferencesService.setCurrentTheme(Theme.Light);
            case Light -> PreferencesService.setCurrentTheme(Theme.Dark);
        }
        MainController.refreshDisplay();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] layouts = PreferencesService.getLayouts();
        ObservableList<String> options = FXCollections.observableArrayList(layouts);
        cboThemes.setItems(options);
    }

    @FXML
    private void comboAction(ActionEvent event) {
        PreferencesService.setSelectedLayout(cboThemes.getValue());
        MainController.refreshDisplay();
    }
}