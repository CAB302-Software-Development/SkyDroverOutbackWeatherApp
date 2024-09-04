package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 600;

    @FXML
    public Pane pnlContent;
    @FXML
    public Button btnProfile, btnDashboard, btnMap, btnForecast, btnSettings;

    private Node profileNode, dashboardNode, forecastNode, mapNode, settingsNode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader temp1 = new FXMLLoader(ApplicationEntry.class.getResource("profile-panel.fxml"));
        FXMLLoader temp2 =  new FXMLLoader(ApplicationEntry.class.getResource("dashboard-panel.fxml"));
        FXMLLoader temp3 =  new FXMLLoader(ApplicationEntry.class.getResource("forecast-panel.fxml"));
        FXMLLoader temp4 =  new FXMLLoader(ApplicationEntry.class.getResource("map-panel.fxml"));
        FXMLLoader temp5 =  new FXMLLoader(ApplicationEntry.class.getResource("settings-panel.fxml"));
        try {
            profileNode = temp1.load();
            dashboardNode = temp2.load();
            forecastNode = temp3.load();
            mapNode = temp4.load();
            settingsNode = temp5.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pnlContent.getChildren().add(dashboardNode);

        ((ProfileController) temp1.getController()).initialize(pnlContent);
        ((DashboardController) temp2.getController()).initialize(pnlContent);
        ((ForecastController) temp3.getController()).initialize(pnlContent);
        ((MapController) temp4.getController()).initialize(pnlContent);
        ((SettingsController) temp5.getController()).initialize(pnlContent);

        btnProfile.setOnAction(actionEvent -> pnlContent.getChildren().set(0, profileNode));
        btnDashboard.setOnAction(actionEvent -> pnlContent.getChildren().set(0, dashboardNode));
        btnMap.setOnAction(actionEvent -> pnlContent.getChildren().set(0, mapNode));
        btnForecast.setOnAction(actionEvent -> pnlContent.getChildren().set(0, forecastNode));
        btnSettings.setOnAction(actionEvent -> pnlContent.getChildren().set(0, settingsNode));
    }
}
