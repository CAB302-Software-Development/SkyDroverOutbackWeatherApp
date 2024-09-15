package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 600;

    public static MainController controller;

    @FXML
    public Pane pnlContent;
    @FXML
    public Button btnProfile, btnDashboard, btnMap, btnForecast, btnSettings, btnDrawer;
    @FXML
    public VBox vbNavbar;
    private Scene scene;
    private List<ISwapPanel> controllers;

    public void setScene(Scene scene) {
        this.scene = scene;
        this.scene.getStylesheets().clear();
        this.scene.getStylesheets().add(PreferencesService.getCurrentThemeData());
    }

    public void refreshDisplay() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(PreferencesService.getCurrentThemeData());
        controllers.forEach(ISwapPanel::updateAppearance);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controllers = new ArrayList<>();
        controller = this;
        Node swpForecast = createSwapPanel("panels/forecast-panel.fxml", btnForecast);
        Node swpMap = createSwapPanel("panels/map-panel.fxml", btnMap);
        Node swpProfile = createSwapPanel("panels/profile-panel.fxml", btnProfile);
        Node swpSettings = createSwapPanel("panels/settings-panel.fxml", btnSettings);
        Node swpDashboard = createSwapPanel("panels/dashboard-panel.fxml", btnDashboard);
        pnlContent.getChildren().add(swpDashboard);
    }

    private Node createSwapPanel(String fxmlPath, Button button) {
        FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(fxmlPath));
        Node panelNode;
        try {
            panelNode = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ISwapPanel controller = loader.getController();
        controller.initialize(pnlContent);
        controllers.add(controller);
        button.setOnAction(actionEvent -> pnlContent.getChildren().set(0, panelNode));
        return panelNode;
    }
}
