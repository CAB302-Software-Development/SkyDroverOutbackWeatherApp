package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Forecast page
 *
 * This class is responsible for handling the Forecast page.
 */
public class ForecastController implements Initializable {
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

    /**
     * Initializes the controller
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sdk = new Sdk();

        progressIndicator.setVisible(false);

        // Init linechart
        lineChart.getXAxis().setLabel("Hour");
        lineChart.getYAxis().setLabel("Temperature");
        lineChart.setTitle("Hourly Temperature");

        loadUserLocations();

        locationComboBox.setConverter(new StringConverter<>() {
            /**
             * Converts a location to a string.
             *
             * @param location The location to convert.
             * @return The string representation of the location.
             */
            @Override
            public String toString(Location location) {
                return location.getName();
            }
            /**
             * Converts a string to a location.
             *
             * @param string The string to convert.
             * @return The location representation of the string.
             */
            @Override
            public Location fromString(String string) {
                return null;
            }
        });

        if (!locationComboBox.getItems().isEmpty()) {
            locationComboBox.getSelectionModel().selectFirst();
            loadDailyForecastData();
            loadHourlyForecastData();
        } else {
            showAlert("No Locations Found", "Please add a location to view forecasts.");
        }
    }

    /**
     * Loads the user's locations into the combo box.
     */
    private void loadUserLocations() {
        List<Location> locations = (new LocationDAO.LocationQuery())
                .whereAccount(LoginState.getCurrentAccount())
                .getResults();
        locationComboBox.getItems().setAll(locations);
    }

    @FXML
    private void loadDailyForecastData() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            return;
        }

        DateData temp = new DateData(LocalDate.now(), 7);
        List<DailyForecast> forecasts = (new DailyForecastDAO.DailyForecastQuery())
                .whereLocationId(selectedLocation.getId())
                .whereTimestampGE((int) temp.getDayStartEpoch())
                .whereTimestampLE((int) temp.getDayEndEpoch())
                .getResults();

        hbForecasts.getChildren().clear();
        forecasts.forEach(f -> hbForecasts.getChildren().add(createForecastDayTile(f)));
    }

    private void loadHourlyForecastData() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            lineChart.getData().clear();
            return;
        }

        DateData temp = new DateData(LocalDate.now());
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

    @FXML
    private void refreshForecastData() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            showAlert("No Location Selected", "Please select a location to refresh data.");
            return;
        }

        progressIndicator.setVisible(true);
        refreshButton.setDisable(true);

        new Thread(() -> {
            try {
                sdk.updateDailyForecast(selectedLocation, 7, 0);
                sdk.updateHourlyForecast(selectedLocation, 2, 2);

                Platform.runLater(() -> {
                    loadDailyForecastData();

                    progressIndicator.setVisible(false);
                    refreshButton.setDisable(false);
                    showAlert("Data Refreshed", "Forecast data has been updated.");
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    refreshButton.setDisable(false);
                    showAlert("Error", "Failed to refresh forecast data.");
                });
            }
        }).start();
    }

    private void showAlert(String title, String message) {
        if (vbRoot == null) {
            Logger.printLog(title, message);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(vbRoot.getScene().getWindow());
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
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

        // Weather Icon
        // ImageView weatherIcon = new ImageView();
        // int weatherCode = forecast.getWeather_code();
        // Image iconImage = getWeatherIcon(weatherCode);
        // weatherIcon.setImage(iconImage);
        // weatherIcon.setFitWidth(50);
        // weatherIcon.setFitHeight(50);

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
}