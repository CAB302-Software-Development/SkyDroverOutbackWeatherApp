package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import javafx.fxml.FXML;

public class SettingsController {

    @FXML
    public void swapTheme() {
        switch (PreferencesService.getCurrentTheme()) {
            case Dark -> PreferencesService.setCurrentTheme(Theme.Light);
            case Light -> PreferencesService.setCurrentTheme(Theme.Dark);
        }
        MainController.refreshDisplay();
    }
}