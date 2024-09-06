package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 600;

    @FXML
    public Pane pnlContent;
    @FXML
    public Button btnProfile, btnDashboard, btnMap, btnForecast, btnSettings;

    private Node swpProfile, swpDashboard, swpMap, swpForecast, swpSettings;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        swpForecast = createSwapPanel("panels/forecast-panel.fxml", btnForecast);
        swpMap = createSwapPanel("panels/map-panel.fxml", btnMap);
        swpProfile = createSwapPanel("panels/profile-panel.fxml", btnProfile);
        swpSettings = createSwapPanel("panels/settings-panel.fxml", btnSettings);
        swpDashboard = createSwapPanel("panels/dashboard-panel.fxml", btnDashboard);
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
        button.setOnAction(actionEvent -> pnlContent.getChildren().set(0, panelNode));
        return panelNode;
    }
}
