package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.CrowdsourcedModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.SelectablePoint;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CrowdsourcedDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.CrowdsourcedApiService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.InputService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.PointMapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing and displaying a map with user and crowdsourced data points.
 * Provides functionalities for searching locations, displaying forecast data, and adding markers.
 */
public class MapController extends BasePage {
    @FXML
    private MapView mapView;
    @FXML
    private AnchorPane apContent;
    @FXML
    private TextField txtSearchBar;
    @FXML
    private VBox vbCrowdDataView;
    @FXML
    private Label lblTemperature, lblWind, lblPrecipitation, lblHumidity, lblClouds;
    @FXML
    private HBox hbTemperature, hbWind, hbPrecipitation, hbHumidity, hbClouds;
    @FXML
    private StackPane spLocationForecastView;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button btnAddMarker, btnRefresh;

    CrowdsourcedApiService crowdsourcedDataService;

    @Getter
    private LocationCreateModel selectedLocation = null;
    private PointMapLayer pointMapLayer;
    private DoubleBinding centeredAnchor;

    @Override
    public void initialize() {
        super.initialize();
        crowdsourcedDataService = CrowdsourcedApiService.getInstance();

        AnchorPane.setTopAnchor(txtSearchBar, 30.0);
        centeredAnchor = apContent.widthProperty().divide(3);

        centeredAnchor.addListener((obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(txtSearchBar, newVal.doubleValue());
            AnchorPane.setRightAnchor(txtSearchBar, newVal.doubleValue());
        });

        initMap();
    }

    /**
     * Initializes the map, setting the initial view and handling click events on the map.
     * Updates the map data upon initialization.
     */
    private void initMap() {
        MapPoint initialPoint = new MapPoint(-25.2744, 133.7751);
        mapView.setZoom(5.0);
        mapView.setCenter(initialPoint);

        mapView.setOnMouseClicked((MouseEvent event) -> {
            if (event.isStillSincePress()) {
                MapPoint clickedPos = mapView.getMapPosition(event.getX(), event.getY());
                if (clickedPos == null) return;
                double latitude = clickedPos.getLatitude();
                double longitude = clickedPos.getLongitude();
                hideCrowdPanel();
                hideForecastPanel();
                setSelectedLocation(new LocationCreateModel(null, latitude, longitude));
            }
        });
        updateData();
    }

    /**
     * Adds crowdsourced data points to the map as pins, creating interactive elements for each point.
     */
    private void addCrowdDataToMap() {
        try {
            List<CrowdsourcedDTO> crowdDataList = crowdsourcedDataService.getLatestFilteredData();
            if (crowdDataList == null) return;

            for (CrowdsourcedDTO crowdData : crowdDataList) {
                MapPoint location = new MapPoint(crowdData.getLatitude(), crowdData.getLongitude());
                SelectablePoint point = new SelectablePoint(
                        location,
                        getCrowdPin(location, crowdData, true),
                        getCrowdPin(location, crowdData, false),
                        crowdData);
                pointMapLayer.addPoint(point);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds user-saved locations to the map, creating selectable points for each location.
     */
    private void addUserLocationsToMap() {
        try {
            List<Location> locations = locationService.getCurrentUserLocations();
            if (locations == null) return;

            for (Location location : locations) {
                MapPoint point = new MapPoint(location.getLatitude(), location.getLongitude());
                SelectablePoint selectablePoint = new SelectablePoint(
                        point,
                        getLocationPin(point, location, true),
                        getLocationPin(point, location, false),
                        location);
                pointMapLayer.addPoint(selectablePoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a visual pin for a location point, showing location data on selection.
     *
     * @param location The MapPoint representing the location.
     * @param locationData The location model data.
     * @param selected Indicates if node is the selected or unselected design.
     * @return A Node representing the pin on the map.
     */
    private Node getLocationPin(MapPoint location, Location locationData, boolean selected) {
        Circle pinCircle = new Circle(10, Color.BLUE);
        if (selected) {
            pinCircle.setFill(Color.GREEN);
        }
        pinCircle.setOnMouseClicked(event -> {
            pointMapLayer.setSelectedLocation(location);
            hideCrowdPanel();
            setForecastLoading(true);
            Thread locationThread = new Thread(() -> {
                forecastService.updateForecastsForCurrentUser(2,1);
                HourlyForecast forecast = forecastService.getLatestHourlyForecast(locationData);
                Platform.runLater(() -> displayForecast(forecast));
            });
            event.consume();
            locationThread.start();
        });

        return pinCircle;
    }

    /**
     * Creates a visual pin for a crowdsourced data point, showing data details on selection.
     *
     * @param location The MapPoint of the data point.
     * @param crowdData The crowdsourced data.
     * @param selected Indicates if node is the selected or unselected design.
     * @return A Node representing the crowdsourced data pin.
     */
    private Node getCrowdPin(MapPoint location, CrowdsourcedDTO crowdData, boolean selected) {
        Circle pinCircle = new Circle(10, Color.RED);
        if (selected) {
            pinCircle.setStroke(Color.YELLOW);
        } else {
            pinCircle.setStroke(Color.BLACK);
        }
        pinCircle.setStrokeWidth(2);
        Text tempText = new Text(crowdData.getActualTemp() + "°");
        tempText.setFill(Color.WHITE);
        StackPane pin = new StackPane(pinCircle, tempText);
        pin.setTranslateX(-10);
        pin.setTranslateY(-10);

        pin.setOnMouseClicked(event -> {
            pointMapLayer.setSelectedLocation(location);
            hideForecastPanel();
            showCrowdDataDetails(crowdData);
            event.consume();
        });

        return pin;
    }

    /**
     * Hides the panel displaying crowdsourced data details.
     */
    private void hideCrowdPanel() {
        vbCrowdDataView.setVisible(false);
    }

    /**
     * Displays detailed information about a specific crowdsourced data point.
     *
     * @param crowdData The data point to display details for.
     */
    private void showCrowdDataDetails(CrowdsourcedDTO crowdData) {
        vbCrowdDataView.getChildren().clear();
        List<Node> details = new ArrayList<>();

        details.add(new Label("Location: " + crowdData.getLocation()));
        details.add(new Label("User: " + crowdData.getUsername()));
        details.add(new Label("Feels like temp: " + crowdData.getFeelsLikeTemp()));
        details.add(new Label("Actual temp: " + crowdData.getActualTemp()));

        // TODO add more fields?

        vbCrowdDataView.getChildren().addAll(details);
        vbCrowdDataView.setVisible(true);
    }

    /**
     * Sets a new location as the selected location, updating the map and UI accordingly.
     *
     * @param newLocation The new location to set as selected.
     */
    private void setSelectedLocation(LocationCreateModel newLocation) {
        if (newLocation == null) {
            pointMapLayer.clearSelectedLocation();
            selectedLocation = null;
        } else {
            selectedLocation = newLocation;

            MapPoint point = new MapPoint(selectedLocation.getLatitude(), selectedLocation.getLongitude());
            pointMapLayer.setSelectedLocation(point);
            if (!pointMapLayer.isInBounds(point)) {
                mapView.flyTo(0, point, 1);
            }
        }
    }

    /**
     * Hides the panel displaying forecast data.
     */
    private void hideForecastPanel() {
        spLocationForecastView.setVisible(false);
    }

    /**
     * Toggles the loading state for the forecast display, showing or hiding data and loading indicators.
     *
     * @param value True to show the loading indicator; false to display forecast data.
     */
    private void setForecastLoading(boolean value) {
        spLocationForecastView.setVisible(true);
        hbTemperature.setVisible(!value);
        hbClouds.setVisible(!value);
        hbPrecipitation.setVisible(!value);
        hbHumidity.setVisible(!value);
        hbWind.setVisible(!value);
        progressIndicator.setVisible(value);
    }

    /**
     * Displays forecast data in the UI based on the given forecast details.
     *
     * @param forecast The forecast data to display.
     */
    private void displayForecast(HourlyForecast forecast) {
        setForecastLoading(false);
        spLocationForecastView.setVisible(true);
        lblTemperature.setText(forecast.getTemperature_2m() + "");
        lblClouds.setText(forecast.getCloud_cover() + "%");
        lblPrecipitation.setText(forecast.getPrecipitation() + "ml");
        lblHumidity.setText(forecast.getRelative_humidity_2m() + "%");
        lblWind.setText(forecast.getWind_speed_10m() + "m/s");
    }

    /**
     * Handles button press events for searching locations, adding markers, and refreshing map data.
     *
     * @param event The ActionEvent triggered by button presses.
     */
    @FXML
    private void handleButtonPress(ActionEvent event) {
        if (event.getSource() == txtSearchBar) {
            LocationCreateModel newLocation = locationService.geocodeAddress(txtSearchBar.getText());
            if (newLocation != null) {
                txtSearchBar.setText("");
                setSelectedLocation(newLocation);
            }
        } else if (event.getSource() == btnAddMarker) {
            if (UserService.getInstance().isGuest()) {
                InputService.showAlert("Error", "Guests are not allowed to add markers, please log in or create an account to continue.");
                return;
            }
            CrowdsourcedDTO data = InputService.getCrowdData();
            if (data == null) return;
            try {
                CrowdsourcedModel temp = crowdsourcedDataService.createMarker(data);
                if (temp == null) InputService.showAlert("Error", "Error de-serialising data from API.");

            } catch (Exception e) {
                InputService.showAlert("Error creating marker", "Could not reach API");
            }
        } else if (event.getSource() == btnRefresh) {
            updateData();
        }
    }

    /**
     * Updates the data displayed on the map, reloading user and crowdsourced locations.
     */
    @Override
    public void updateData() {
        hideCrowdPanel();
        pointMapLayer = new PointMapLayer(mapView);
        mapView.addLayer(pointMapLayer);
        addUserLocationsToMap();
        addCrowdDataToMap();
    }
}