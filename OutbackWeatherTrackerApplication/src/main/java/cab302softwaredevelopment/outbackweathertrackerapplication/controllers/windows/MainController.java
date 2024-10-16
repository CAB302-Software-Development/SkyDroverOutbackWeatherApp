package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.PageFactory;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    private BorderPane root;

    private ScheduledExecutorService scheduler;
    private PageFactory pageFactory;

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
        pageFactory = new PageFactory(root);

        Node swpProfile = pageFactory.createSwapPanel("panels/profile-panel.fxml", btnProfile);
        Node swpDashboard = pageFactory.createSwapPanel("panels/dashboard-panel.fxml", btnDashboard);
        Node swpForecast = pageFactory.createSwapPanel("panels/forecast-panel.fxml", btnForecast);
        Node swpMap = pageFactory.createSwapPanel("panels/map-panel.fxml", btnMap);
        Node swpAlerts = pageFactory.createSwapPanel("panels/alerts-panel.fxml", btnAlerts);
        Node swpReports = pageFactory.createSwapPanel("panels/reports-panel.fxml", btnReports);
        Node swpSettings = pageFactory.createSwapPanel("panels/settings-panel.fxml", btnSettings);
        root.centerProperty().set(swpDashboard);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateUIData, 0, 5, TimeUnit.MINUTES);
    }

    private void updateUIData() {
        pageFactory.updateAllPages();
    }

    public static void showAlert(String title, String message) {
        if (controller.root == null) {
            Logger.printLog(title, message);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(controller.root.getScene().getWindow());
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}
