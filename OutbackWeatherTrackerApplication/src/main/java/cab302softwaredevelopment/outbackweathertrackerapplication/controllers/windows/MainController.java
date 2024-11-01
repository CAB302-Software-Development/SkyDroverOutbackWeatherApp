package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.PageFactory;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ConnectionService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Outback Weather Tracker Application";

    @Getter
    private static MainController controller;

    @FXML
    public Button btnProfile, btnDashboard, btnMap, btnForecast, btnAlerts, btnSettings, btnDrawer;
    @FXML
    public VBox vbNavbar;
    private Scene scene;
    @FXML
    private BorderPane root;

    private ScheduledExecutorService scheduler;
    private PageFactory pageFactory;

    private ImageView imgProfile, imgProfileOffline;

    public void setScene(Scene scene) {
        this.scene = scene;
        refreshDisplay();
    }

    public static void refreshDisplay() {
        controller.scene.getStylesheets().clear();
        controller.scene.getStylesheets().addAll(getCurrentThemeData());
    }

    public static List<String> getCurrentThemeData() {
        Theme currentTheme = UserService.getInstance().getCurrentAccount().getCurrentTheme();
        String iconsPath = Objects.requireNonNull(ApplicationEntry.class.getResource("themes/icons.css")).toExternalForm();
        String themePath = Objects.requireNonNull(ApplicationEntry.class.getResource(currentTheme.getFilePath())).toExternalForm();
        String stylePath = Objects.requireNonNull(ApplicationEntry.class.getResource("themes/style.css")).toExternalForm();
        return Arrays.asList(stylePath, themePath, iconsPath);
    }

    @FXML
    public void initialize() {
        controller = this;
        pageFactory = new PageFactory(root);

        try {
            String profileURL = ApplicationEntry.class.getResource("images/account_circle.png").toExternalForm();
            String offlineURL = ApplicationEntry.class.getResource("images/offline.png").toExternalForm();
            imgProfile =  new ImageView(profileURL);
            imgProfileOffline = new ImageView(offlineURL);
            imgProfile.setFitWidth(30);
            imgProfile.setFitHeight(30);
            imgProfile.setPreserveRatio(true);

            imgProfileOffline.setFitWidth(30);
            imgProfileOffline.setFitHeight(30);
            imgProfileOffline.setPreserveRatio(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        btnProfile.graphicProperty().setValue(imgProfile);

        Node swpDashboard = pageFactory.createSwapPanel("panels/dashboard-panel.fxml", btnDashboard);
        root.centerProperty().set(swpDashboard);

        new Thread(() -> {
            pageFactory.createSwapPanel("panels/profile-panel.fxml", btnProfile);
            pageFactory.createSwapPanel("panels/forecast-panel.fxml", btnForecast);
            pageFactory.createSwapPanel("panels/alerts-panel.fxml", btnAlerts);
            pageFactory.createSwapPanel("panels/settings-panel.fxml", btnSettings);
        }).start();

        // Map page needs to be on main thread to work
        pageFactory.createSwapPanel("panels/map-panel.fxml", btnMap);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateUIData, 2, 300, TimeUnit.SECONDS);
    }

    public void updateUIData() {
        if (ConnectionService.getInstance().isOffline()) {
            Platform.runLater(() -> btnProfile.graphicProperty().setValue(imgProfileOffline));
        } else {
            Platform.runLater(() -> btnProfile.graphicProperty().setValue(imgProfile));
        }
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

    public static void shutdownScheduler() {
        if (controller == null) return;
        if (controller.scheduler != null && !controller.scheduler.isShutdown()) {
            controller.scheduler.shutdown();
        }
    }
}
