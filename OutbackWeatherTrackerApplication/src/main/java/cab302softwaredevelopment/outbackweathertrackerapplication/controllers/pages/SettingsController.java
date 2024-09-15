package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class SettingsController implements ISwapPanel {
    @FXML
    Panel pnlRoot;

    public void initialize(Pane parent) {
        pnlRoot.prefWidthProperty().bind(parent.widthProperty());
        pnlRoot.prefHeightProperty().bind(parent.heightProperty());
    }

    @Override
    public void updateAppearance() {

    }

    @FXML
    public void swapTheme() {
        switch (PreferencesService.getCurrentTheme()) {
            case Dark -> PreferencesService.setCurrentTheme(Theme.Light);
            case Light -> PreferencesService.setCurrentTheme(Theme.Dark);
        }
        MainController.controller.refreshDisplay();
    }
}