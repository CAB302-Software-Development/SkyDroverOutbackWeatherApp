package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ForecastController implements Initializable {
    @FXML
    public ScrollPane scrForecasts;
    @FXML
    public HBox pnForecastContainer;
    @FXML
    private VBox vbRoot;
    @FXML
    private ComboBox<Location> locationComboBox;
    @FXML
    private Button refreshButton;
    @FXML
    private TableColumn<DailyForecast, LocalDate> dateColumn;
    @FXML
    private TableColumn<DailyForecast, Double> maxTempColumn;
    @FXML
    private TableColumn<DailyForecast, Double> minTempColumn;
    @FXML
    private TableColumn<DailyForecast, Double> precipitationColumn;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private LineChart<Number,Number> lineChart;
    @FXML
    private HBox hbForecasts;
    private LocationDAO locationDAO;
    private DailyForecastDAO dailyForecastDAO;
    private HourlyForecastDAO hourlyForecastDAO;
    private Sdk sdk;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init data access objects
        locationDAO = new LocationDAO();
        dailyForecastDAO = new DailyForecastDAO();
        hourlyForecastDAO = new HourlyForecastDAO();
        sdk = new Sdk();

        progressIndicator.setVisible(false);

        // Init linechart
        lineChart.getXAxis().setLabel("Hour");
        lineChart.getYAxis().setLabel("Temperature");
        lineChart.setTitle("Hourly Temperature");


        //Init table
        // dateColumn.setCellValueFactory(cellData -> {
        //     int timestamp = cellData.getValue().getTimestamp();
        //     LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        //     return new ReadOnlyObjectWrapper<>(date);
        // });
        // maxTempColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTemperature_2m_max()));
        // minTempColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTemperature_2m_min()));
        // precipitationColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrecipitation_sum()));

        loadUserLocations();

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

        locationComboBox.setOnAction(event -> loadDailyForecastData());
        refreshButton.setOnAction(event -> refreshForecastData());

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
        List<Location> locations;
        if (LoginState.isGuest()) {
            locations = locationDAO.getAll(); // TODO limit this
        } else {
            locations = locationDAO.getByAccount(LoginState.getAccount());
        }
        locationComboBox.getItems().setAll(locations);
    }

    /**
     * Loads forecast data for the selected location from the database.
     */
    private void loadDailyForecastData() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            hbForecasts.getChildren().clear();
            return;
        }
        List<DailyForecast> forecasts = dailyForecastDAO.getByLocationId(selectedLocation.getId());
        forecasts.forEach(f -> hbForecasts.getChildren().add(createForecastDayTile(f)));
    }

    /**
     * Loads forecast data for the selected location from the database.
     */
    private void loadHourlyForecastData() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            lineChart.getData().clear();
            return;
        }

        ZonedDateTime startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        long startEpoch = startOfDay.toEpochSecond();
        long endEpoch = startOfDay.plusDays(1).toEpochSecond();

        List<HourlyForecast> forecasts =
                hourlyForecastDAO.getByLocationId(selectedLocation.getId())
                        .stream()
                        .filter(f -> f.getTimestamp() >= startEpoch && f.getTimestamp() < endEpoch)
                        .sorted(Comparator.comparingInt(HourlyForecast::getTimestamp))
                        .toList();

        XYChart.Series tempSeries = new XYChart.Series();
        for (int i = 0; i < forecasts.size(); i++) {
            int temperature = forecasts.get(i).getTemperature_2m().intValue();
            tempSeries.getData().add(new XYChart.Data(i, temperature));
        }
        lineChart.getData().clear();
        lineChart.getData().add(tempSeries);
    }

    /**
     * Refreshes forecast data by fetching new data from the API and updating the database.
     */
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
                // TODO do this smarter
                List<DailyForecast> existingForecasts = dailyForecastDAO.getByLocation(selectedLocation);
                if (existingForecasts.size() == 0) {
                    List<DailyForecast> newForecasts = sdk.getDailyForecast(selectedLocation, 7, 0);
                    newForecasts.forEach(f -> dailyForecastDAO.insert(f));
                } else {
                    sdk.updateDailyForecast(selectedLocation, 7, 0);
                }

                List<HourlyForecast> existingHourly = hourlyForecastDAO.getByLocation(selectedLocation);
                if (existingHourly.size() == 0) {
                    List<HourlyForecast> newHourly = sdk.getHourlyForecast(selectedLocation, 2, 2);
                    newHourly.forEach(f -> hourlyForecastDAO.insert(f));
                } else {
                    sdk.updateHourlyForecast(selectedLocation, 2, 2);
                }

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

    /**
     * Shows an alert dialog with the specified title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(vbRoot.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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