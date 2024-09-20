package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;


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
    private TableView<DailyForecast> forecastTableView;
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
    private LocationDAO locationDAO;
    private DailyForecastDAO dailyForecastDAO;
    private Sdk sdk;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // dailyForecastDAO = new DailyForecastDAO();
        // List<DailyForecast> allForecast = dailyForecastDAO.getByLocation(dailyForecastDAO.getAll().get(0).getLocation());
        // List<Node> forecastNodes = allForecast.subList(0, 7).stream().map(this::createForecastDayTile).toList();
        // pnForecastContainer.getChildren().addAll(forecastNodes);

        locationDAO = new LocationDAO();
        dailyForecastDAO = new DailyForecastDAO();
        sdk = new Sdk();

        progressIndicator.setVisible(false);

        dateColumn.setCellValueFactory(cellData -> {
            int timestamp = cellData.getValue().getTimestamp();
            LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
            return new ReadOnlyObjectWrapper<>(date);
        });

        maxTempColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTemperature_2m_max()));
        minTempColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTemperature_2m_min()));
        precipitationColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrecipitation_sum()));

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

        locationComboBox.setOnAction(event -> loadForecastData());
        refreshButton.setOnAction(event -> refreshForecastData());

        if (!locationComboBox.getItems().isEmpty()) {
            locationComboBox.getSelectionModel().selectFirst();
            loadForecastData();
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
    private void loadForecastData() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            forecastTableView.getItems().clear();
            return;
        }
        List<DailyForecast> forecasts = dailyForecastDAO.getByLocationId(selectedLocation.getId());
        forecastTableView.getItems().setAll(forecasts);
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
                List<DailyForecast> existingForecasts = dailyForecastDAO.getByLocation(selectedLocation);
                if (existingForecasts.size() == 0) {
                    List<DailyForecast> newForecasts = sdk.getDailyForecast(selectedLocation, 7, 0);
                    newForecasts.forEach(f -> dailyForecastDAO.insert(f));
                } else {
                    sdk.updateDailyForecast(selectedLocation, 7, 0);
                }

                Platform.runLater(() -> {
                    loadForecastData();
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
        Label lblTemp = new Label(forecast.getTemperature_2m_max().toString());
        lblTemp.autosize();
        VBox root = new VBox(lblTemp);
        root.setMinSize(100,200);
        root.prefHeight(150);
        root.prefWidth(250);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("forecast-day");
        return root;
    }
}