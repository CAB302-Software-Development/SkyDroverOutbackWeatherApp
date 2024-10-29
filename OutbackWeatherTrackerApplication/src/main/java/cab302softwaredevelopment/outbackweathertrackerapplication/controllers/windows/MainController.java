package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.ForecastController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.PageFactory;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets.WidgetFactory;
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
import javafx.scene.control.ProgressIndicator;
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
import java.util.concurrent.atomic.AtomicBoolean;


public class MainController implements Initializable {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Outback Weather Tracker Application";

    private static MainController controller;

    @FXML
    public Button btnProfile, btnDashboard, btnMap, btnForecast, btnAlerts, btnReports, btnSettings, btnRefresh, btnDrawer;
    @FXML
    public VBox vbNavbar;
    private Scene scene;
    @FXML
    private BorderPane root;
    @FXML
    public ProgressIndicator progressIndicator;

    private ScheduledExecutorService scheduler;
    private PageFactory pageFactory;
    private final AtomicBoolean isRefreshing = new AtomicBoolean(false);

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = this;
        pageFactory = new PageFactory(root);

        setupRefreshButton();

        Node swpDashboard = pageFactory.createSwapPanel("panels/dashboard-panel.fxml", btnDashboard);
        root.centerProperty().set(swpDashboard);

        new Thread(() -> {
            pageFactory.createSwapPanel("panels/profile-panel.fxml", btnProfile);
            pageFactory.createSwapPanel("panels/forecast-panel.fxml", btnForecast);
            pageFactory.createSwapPanel("panels/alerts-panel.fxml", btnAlerts);
            pageFactory.createSwapPanel("panels/reports-panel.fxml", btnReports);
            pageFactory.createSwapPanel("panels/settings-panel.fxml", btnSettings);
        }).start();

        // Map page needs to be on main thread to work
        pageFactory.createSwapPanel("panels/map-panel.fxml", btnMap);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateUIData, 5, 300, TimeUnit.SECONDS);
    }

    private void setupRefreshButton() {
        if (progressIndicator != null) {
            progressIndicator.setVisible(false);
        }

        btnRefresh.setOnAction(e -> performGlobalRefresh());
    }

    private void performGlobalRefresh() {
        // Prevent multiple simultaneous refreshes
        if (isRefreshing.get()) {
            return;
        }

        try {
            isRefreshing.set(true);
            btnRefresh.setDisable(true);
            if (progressIndicator != null) {
                progressIndicator.setVisible(true);
            }

            // Start refresh in background thread
            new Thread(() -> {
                try {
                    // Force refresh connection service
                    ConnectionService.getInstance().forceRefresh();

                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        try {
                            // Force refresh all pages
                            pageFactory.updateAllPages();

                            // Update all widgets
                            WidgetFactory.getWidgetManager().updateWidgets();

                            showAlert("Refresh Complete", "All data has been refreshed.");
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert("Refresh Error", "An error occurred while refreshing the UI.");
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() ->
                        showAlert("Connection Error", "Failed to refresh data from server.")
                    );
                } finally {
                    Platform.runLater(() -> {
                        isRefreshing.set(false);
                        btnRefresh.setDisable(false);
                        if (progressIndicator != null) {
                            progressIndicator.setVisible(false);
                        }
                    });
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            isRefreshing.set(false);
            btnRefresh.setDisable(false);
            if (progressIndicator != null) {
                progressIndicator.setVisible(false);
            }
            showAlert("Error", "An unexpected error occurred during refresh.");
        }
    }

    private void updateUIData() {
        if (!isRefreshing.get()) {
            pageFactory.updateAllPages();
        }
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
