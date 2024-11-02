package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.InputService;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Controller class for managing and displaying weather forecasts.
 * Provides functionalities for selecting locations, refreshing forecast data,
 * and displaying daily and hourly temperature charts.
 */
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
    private LineChart<Number,Number> lineChart;
    @FXML
    private HBox hbForecasts;
    private Sdk sdk;

    @Override
    public void initialize() {
        super.initialize();
        sdk = new Sdk();

        progressIndicator.setVisible(false);

        // Init linechart
        lineChart.getXAxis().setLabel("Hour");
        lineChart.getYAxis().setLabel("Temperature");
        lineChart.setTitle("Hourly Temperature");

        initLocationsComboBox();
        refreshForecastData(false);
    }

    /**
     * Loads the user's locations into the ComboBox for selecting a forecast location.
     * Configures the ComboBox to display location names and select the first available location by default.
     */
    private void initLocationsComboBox() {
        List<Location> locations = locationService.getCurrentUserLocations();
        locationComboBox.getItems().setAll(locations);

        locationComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Location location) {
                return location.getName();
            }
            @Override
            public Location fromString(String string) {
                return null;
            }
        });

        if (!locationComboBox.getItems().isEmpty()) {
            locationComboBox.getSelectionModel().selectFirst();
            if (locationComboBox.getItems().size() == 1) {
                locationComboBox.disableProperty().set(true);
            } else {
                locationComboBox.disableProperty().set(false);
            }
        } else {
            InputService.showAlert("No Locations Found", "Please add a location to view forecasts.");
        }
    }

    /**
     * Loads daily forecast data for the selected location and populates the forecast display.
     * Also loads the hourly forecast data for the first day in the forecast period.
     */
    @FXML
    private void loadDailyForecastData() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            return;
        }

        DateData temp = new DateData(LocalDateTime.now(), 7);
        List<DailyForecast> forecasts = forecastService.getDailyForecastByTimeRange(selectedLocation, temp);

        hbForecasts.getChildren().clear();
        forecasts.forEach(f -> hbForecasts.getChildren().add(createForecastDayTile(f)));

        forecasts.stream().min(Comparator.comparingInt(DailyForecast::getTimestamp)).ifPresent(this::loadHourlyForecastData);
    }

    /**
     * Loads hourly forecast data for a specified day and populates the line chart with temperature data.
     *
     * @param dailyForecast The daily forecast data for which hourly data will be displayed.
     */
    private void loadHourlyForecastData(DailyForecast dailyForecast) {
        Location selectedLocation = dailyForecast.getLocation();
        if (selectedLocation == null) {
            lineChart.getData().clear();
            return;
        }

        LocalDateTime day = Instant.ofEpochSecond(dailyForecast.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateData temp = new DateData(day);
        List<HourlyForecast> forecasts = (new HourlyForecastDAO.HourlyForecastQuery())
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

    /**
     * Refreshes the forecast data for the selected location by reloading daily forecasts.
     * This method is triggered by the refresh button.
     */
    @FXML
    private void refreshForecastData() {
        refreshForecastData(true);
    }

    /**
     * Refreshes the forecast data with an option to update the forecast data from the API.
     *
     * @param update Indicates whether to update data in the forecast service.
     */
    private void refreshForecastData(boolean update) {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            InputService.showAlert("No Location Selected", "Please select a location to refresh data.");
            return;
        }

        progressIndicator.setVisible(true);
        refreshButton.setDisable(true);

        new Thread(() -> {
            try {
                boolean result;
                if (update) result = forecastService.updateForecastsForCurrentUser(7, 1);
                else {
                    result = true;
                }

                Platform.runLater(() -> {
                    loadDailyForecastData();

                    progressIndicator.setVisible(false);
                    refreshButton.setDisable(false);
                    if (result) {
                        if (update) InputService.showAlert("Data Refreshed", "Forecast data has been updated.");
                        connectionService.setOpenMeteoDataOffline(false);
                    } else {
                        connectionService.setOpenMeteoDataOffline(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    refreshButton.setDisable(false);
                    if (update) InputService.showAlert("Error", "Failed to refresh forecast data.");
                });
            }
        }).start();
    }

    /**
     * Creates a UI tile to display daily forecast data, including date, temperatures,
     * precipitation, and wind speed.
     *
     * @param forecast The daily forecast data to display on the tile.
     * @return A VBox node representing a day’s forecast information.
     */
    private Node createForecastDayTile(DailyForecast forecast) {
        VBox root = new VBox();
        root.setOnMouseClicked(event -> loadHourlyForecastData(forecast));
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

    /**
     * Updates the data for all forecast-related UI elements.
     */
    @Override
    public void updateData() {

    }
}