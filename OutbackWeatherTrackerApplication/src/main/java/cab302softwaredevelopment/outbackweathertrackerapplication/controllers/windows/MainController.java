package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Outback Weather Tracker Application";

    private static MainController controller;

    @FXML
    public Button btnProfile, btnDashboard, btnMap, btnForecast, btnAlerts, btnReports, btnSettings, btnDrawer;
    @FXML
    public VBox vbNavbar;
    private Scene scene;
    @FXML
    BorderPane root;

    private ScheduledExecutorService scheduler;

    public void setScene(Scene scene) {
        this.scene = scene;
        refreshDisplay();
    }

    public static void refreshDisplay() {
        controller.scene.getStylesheets().clear();
        controller.scene.getStylesheets().addAll(getCurrentThemeData());
    }

    public static List<String> getCurrentThemeData() {
        Theme currentTheme = LoginState.getCurrentAccount().getCurrentTheme();
        String iconsPath = Objects.requireNonNull(ApplicationEntry.class.getResource("themes/icons.css")).toExternalForm();
        String themePath = Objects.requireNonNull(ApplicationEntry.class.getResource(currentTheme.getFilePath())).toExternalForm();
        String stylePath = Objects.requireNonNull(ApplicationEntry.class.getResource("themes/style.css")).toExternalForm();
        return Arrays.asList(stylePath, themePath, iconsPath);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = this;
        Node swpProfile = createSwapPanel("panels/profile-panel.fxml", btnProfile);
        Node swpDashboard = createSwapPanel("panels/dashboard-panel.fxml", btnDashboard);
        Node swpForecast = createSwapPanel("panels/forecast-panel.fxml", btnForecast);
        Node swpMap = createSwapPanel("panels/map-panel.fxml", btnMap);
        Node swpAlerts = createSwapPanel("panels/alerts-panel.fxml", btnAlerts);
        Node swpReports = createSwapPanel("panels/reports-panel.fxml", btnReports);

        Node swpSettings = createSwapPanel("panels/settings-panel.fxml", btnSettings);
        root.centerProperty().set(swpDashboard);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateLocalDB, 0, 10, TimeUnit.MINUTES);
    }

    private void updateLocalDB() {
        try {
            Sdk sdk = new Sdk();
            List<Location> locations = (new LocationDAO.LocationQuery())
                    .whereAccount(LoginState.getCurrentAccount())
                    .getResults();
            for (Location location : locations) {
                sdk.updateDailyForecast(location, 7, 2);
                sdk.updateHourlyForecast(location, 7, 2);
            }
            if (LoginState.isOffline()) LoginState.setOffline(false);
        } catch (Exception e) {
            e.printStackTrace();
            if (!LoginState.isOffline()) LoginState.setOffline(true);
        }
    }

    private Node createSwapPanel(String fxmlPath, Button button) {
        FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(fxmlPath));
        Node panelNode;
        try {
            panelNode = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        button.setOnAction(actionEvent -> root.centerProperty().set(panelNode));
        return panelNode;
    }

    public void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
