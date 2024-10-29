package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ConnectionService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Optional;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.List;

public class ForecastController extends BasePage {
    @FXML
    private VBox vbRoot;
    @FXML
    private ComboBox<Location> locationComboBox;
    @FXML
    private Button refreshButton;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private HBox hbForecasts;

    private boolean isRefreshing = false;
    private Location currentLocation;
    private final LocationService locationService = LocationService.getInstance();

    @Override
    public void initialize() {
        super.initialize();

        progressIndicator.setVisible(false);

        // Init linechart
        lineChart.getXAxis().setLabel("Hour");
        lineChart.getYAxis().setLabel("Temperature");
        lineChart.setTitle("Hourly Temperature");

        setupLocationComboBox();
        loadUserLocations();

        if (!locationComboBox.getItems().isEmpty()) {
            locationComboBox.getSelectionModel().selectFirst();
            refreshForecastData(false); // Initial load
        } else {
            MainController.showAlert("No Locations Found", "Please add a location to view forecasts.");
        }
    }

    private void setupLocationComboBox() {
        locationComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Location location) {
                return location != null ? location.getName() : "";
            }

            @Override
            public Location fromString(String string) {
                return null;
            }
        });

        // Add listener for location changes
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(currentLocation)) {
                currentLocation = newValue;
                refreshForecastData(false);
            }
        });
    }

    private void loadUserLocations() {
        List<Location> locations = locationService.getCurrentUserLocations();
        locationComboBox.getItems().setAll(locations);
    }

    @FXML
    private void loadDailyForecastData() {
        Location selectedLocation = currentLocation;
        if (selectedLocation == null) {
            return;
        }

        DateData temp = new DateData(LocalDate.now(), 7);
        List<DailyForecast> forecasts = new DailyForecastDAO.DailyForecastQuery()
            .whereLocationId(selectedLocation.getId())
            .whereTimestampGE((int) temp.getDayStartEpoch())
            .whereTimestampLE((int) temp.getDayEndEpoch())
            .getResults();

        hbForecasts.getChildren().clear();
        forecasts.forEach(f -> hbForecasts.getChildren().add(createForecastDayTile(f)));
    }

    private void loadHourlyForecastData() {
        Location selectedLocation = currentLocation;
        if (selectedLocation == null) {
            lineChart.getData().clear();
            return;
        }

        DateData temp = new DateData(LocalDate.now());
        List<HourlyForecast> forecasts = new HourlyForecastDAO.HourlyForecastQuery()
            .whereLocation(selectedLocation)
            .whereTimestampGE((int) temp.getDayStartEpoch())
            .whereTimestampLE((int) temp.getDayEndEpoch())
            .getResults();

        XYChart.Series tempSeries = new XYChart.Series();
        for (int i = 0; i < forecasts.size(); i++) {
            int temperature = forecasts.get(i).getTemperature_2m().intValue();
            tempSeries.getData().add(new XYChart.Data(i, temperature));
        }
        lineChart.getData().clear();
        lineChart.getData().add(tempSeries);
    }

    @FXML
    public void refreshForecastData() {
        refreshForecastData(true);
    }

    private void refreshForecastData(boolean showSuccessMessage) {
        if (isRefreshing) return;

        // Store current state for comparison
        Location previousSelection = locationComboBox.getValue();
        List<Location> previousLocations = new ArrayList<>(locationComboBox.getItems());

        isRefreshing = true;
        progressIndicator.setVisible(true);
        refreshButton.setDisable(true);

        new Thread(() -> {
            try {
                // First, refresh the locations list
                List<Location> currentUserLocations = locationService.getCurrentUserLocations();

                Platform.runLater(() -> {
                    try {
                        // Update locations and handle any changes
                        handleLocationUpdates(currentUserLocations, previousLocations, previousSelection);

                        // Only proceed with forecast update if we have locations
                        if (!currentUserLocations.isEmpty()) {
                            boolean result = forecastService.updateForecastsForCurrentUser(7, 2);

                            if (result) {
                                loadDailyForecastData();
                                loadHourlyForecastData();

                                if (showSuccessMessage) {
                                    MainController.showAlert("Success", "Weather data has been updated successfully.");
                                }
                            } else {
                                connectionService.setOffline(true);
                                MainController.showAlert("Connection Error",
                                    "Unable to fetch new weather data. Using cached data.");
                            }
                        }
                    } catch (Exception e) {
                        handleError("Error updating data", e);
                    } finally {
                        progressIndicator.setVisible(false);
                        refreshButton.setDisable(false);
                        isRefreshing = false;
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    handleError("Refresh failed", e);
                    progressIndicator.setVisible(false);
                    refreshButton.setDisable(false);
                    isRefreshing = false;
                });
            }
        }).start();
    }

    private void handleLocationUpdates(List<Location> currentLocations,
        List<Location> previousLocations,
        Location previousSelection) {
        locationComboBox.getItems().clear();

        if (currentLocations.isEmpty()) {
            handleNoLocations();
            return;
        }

        locationComboBox.getItems().addAll(currentLocations);
        List<Location> removedLocations = findRemovedLocations(currentLocations, previousLocations);

        if (!removedLocations.isEmpty()) {
            handleRemovedLocations(removedLocations, previousSelection, currentLocations);
        } else {
            maintainPreviousSelection(previousSelection, currentLocations);
        }
    }

    private void handleNoLocations() {
        clearDisplays();
        MainController.showAlert("No Locations",
            "You currently have no locations set up.\n" +
                "Please add a location in the Settings page.");
    }

    private List<Location> findRemovedLocations(List<Location> currentLocations, List<Location> previousLocations) {
        return previousLocations.stream()
            .filter(prev -> currentLocations.stream().noneMatch(curr -> curr.getId() == prev.getId()))
            .toList();
    }

    private void handleRemovedLocations(List<Location> removedLocations, Location previousSelection, List<Location> currentLocations) {
        if (previousSelection == null) {
            // Handle first login scenario
            locationComboBox.setValue(currentLocations.get(0));
            MainController.showAlert("Welcome",
                "Welcome! The default location is set to '" +
                    currentLocations.get(0).getName() + "'.");
        } else if (removedLocations.stream().anyMatch(loc -> loc.getId() == previousSelection.getId())) {
            locationComboBox.setValue(currentLocations.get(0));
            MainController.showAlert("Location Removed",
                "The previously selected location '" + previousSelection.getName() +
                    "' is no longer available.\nSwitching to '" +
                    currentLocations.get(0).getName() + "'.");
        } else {
            maintainPreviousSelection(previousSelection, currentLocations);
        }


        String removedNames = removedLocations.stream()
            .map(Location::getName)
            .collect(Collectors.joining(", "));

        if (!removedNames.isEmpty()) {
            MainController.showAlert("Locations Updated",
                "The following location(s) have been removed: " + removedNames);
        }
    }

    private void maintainPreviousSelection(Location previousSelection, List<Location> currentLocations) {
        if (previousSelection != null) {
            Optional<Location> stillExists = currentLocations.stream()
                .filter(l -> l.getId() == previousSelection.getId())
                .findFirst();

            stillExists.ifPresentOrElse(
                locationComboBox::setValue,
                () -> locationComboBox.setValue(currentLocations.get(0))
            );
        } else {
            locationComboBox.setValue(currentLocations.get(0));
        }
    }

    private void clearDisplays() {
        hbForecasts.getChildren().clear();
        lineChart.getData().clear();
        currentLocation = null;
    }

    private void handleError(String context, Exception e) {
        e.printStackTrace();
    }

    private Node createForecastDayTile(DailyForecast forecast) {
        VBox root = new VBox();
        root.getStyleClass().add("forecast-day");
        root.setMinSize(100, 200);
        root.setPrefSize(150, 250);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        int timestamp = forecast.getTimestamp();
        LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        Label lblDate = new Label(date.format(DateTimeFormatter.ofPattern("EEE, MMM d")));
        lblDate.getStyleClass().add("date-label");

        Label lblMaxTemp = new Label("Max: " + forecast.getTemperature_2m_max() + "°C");
        lblMaxTemp.getStyleClass().add("max-temp-label");

        Label lblMinTemp = new Label("Min: " + forecast.getTemperature_2m_min() + "°C");
        lblMinTemp.getStyleClass().add("min-temp-label");

        Label lblPrecipitation = new Label("Precipitation: " + forecast.getPrecipitation_sum() + " mm");
        lblPrecipitation.getStyleClass().add("precipitation-label");

        Label lblWindSpeed = new Label("Wind Speed: " + forecast.getWind_speed_10m_max() + " km/h");
        lblWindSpeed.getStyleClass().add("wind-speed-label");

        root.getChildren().addAll(lblDate, lblMaxTemp, lblMinTemp, lblPrecipitation, lblWindSpeed);

        return root;
    }

    @Override
    public void updateData() {
        if (!isRefreshing) {
            refreshForecastData(false);
        }
    }
}