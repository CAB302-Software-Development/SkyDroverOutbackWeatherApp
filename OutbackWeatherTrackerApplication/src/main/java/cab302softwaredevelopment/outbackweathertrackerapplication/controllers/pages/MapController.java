package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
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

public class MapController extends BasePage {
    @FXML
    public MapView mapView;
    @FXML
    public AnchorPane apContent;
    @FXML
    public TextField txtSearchBar;
    @FXML
    public VBox vbCrowdDataView;
    @FXML
    public Label lblTemperature, lblWind, lblPrecipitation, lblHumidity, lblClouds;
    @FXML
    public HBox hbTemperature, hbWind, hbPrecipitation, hbHumidity, hbClouds;
    @FXML
    public StackPane spLocationForecastView;
    @FXML
    public ProgressIndicator progressIndicator;
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
                setForecastVisible(false);
                setSelectedLocation(new LocationCreateModel(null, latitude, longitude, 0));
            }
        });
        updateData();
    }

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

    private Node getLocationPin(MapPoint location, Location locationData, boolean selected) {
        Circle pinCircle = new Circle(10, Color.BLUE);
        if (selected) {
            pinCircle.setFill(Color.GREEN);
        }
        pinCircle.setOnMouseClicked(event -> {
            pointMapLayer.setSelectedLocation(location);
            setForecastLoading(true);
            Thread locationThread = new Thread(() -> {
                HourlyForecast forecast = forecastService.updateForecastsForLocationGetHourly(locationData, 1, 1);
                Platform.runLater(() -> displayForecast(forecast));
            });
            event.consume();
            locationThread.start();
        });

        return pinCircle;
    }

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
            showCrowdDataDetails(crowdData);
            event.consume();
        });

        return pin;
    }

    private void hideCrowdPanel() {
        vbCrowdDataView.setVisible(false);
    }

    private void showCrowdDataDetails(CrowdsourcedDTO crowdData) {
        vbCrowdDataView.getChildren().clear();
        List<Node> details = new ArrayList<>();

        if (crowdData.getLocation() != null) {
            details.add(new Label("Location: " + crowdData.getLocation()));
        }
        if (crowdData.getUserName() != null) {
            details.add(new Label("User: " + crowdData.getUserName()));
        }
        // if (crowdData.getActualTemp() != null) {
        //     details.add(new Label("Actual Temp: " + crowdData.getActualTemp() + "°C"));
        // }
        // if (crowdData.getFeelsLikeTemp() != null) {
        //     details.add(new Label("Feels Like Temp: " + crowdData.getFeelsLikeTemp() + "°C"));
        // }

        vbCrowdDataView.getChildren().addAll(details);
        vbCrowdDataView.setVisible(true);
    }

    private void setSelectedLocation(LocationCreateModel newLocation) {
        if (newLocation == null) {
            pointMapLayer.clearSelectedLocation();
            selectedLocation = null;
        } else {
            String name;
            if (newLocation.getName() == null || newLocation.getName().isEmpty()) {
                try {
                    name = locationService.getAddressFromCoordinates(newLocation);
                } catch (Exception e) {
                    e.printStackTrace();
                    name = "Unknown location";
                }
                newLocation.setName(name);
            }
            selectedLocation = newLocation;

            MapPoint point = new MapPoint(selectedLocation.getLatitude(), selectedLocation.getLongitude());
            pointMapLayer.setSelectedLocation(point);
            if (!pointMapLayer.isInBounds(point)) {
                mapView.flyTo(0, point, 1);
            }
        }
    }

    private void setForecastVisible(boolean value) {
        hbTemperature.setVisible(value);
        hbClouds.setVisible(value);
        hbPrecipitation.setVisible(value);
        hbHumidity.setVisible(value);
        hbWind.setVisible(value);
    }

    private void setForecastLoading(boolean value) {
        setForecastVisible(!value);
        progressIndicator.setVisible(value);
    }

    private void displayForecast(HourlyForecast forecast) {
        setForecastLoading(false);
        lblTemperature.setText(forecast.getTemperature_2m() + "");
        lblClouds.setText(forecast.getCloud_cover() + "%");
        lblPrecipitation.setText(forecast.getPrecipitation() + "ml");
        lblHumidity.setText(forecast.getRelative_humidity_2m() + "%");
        lblWind.setText(forecast.getWind_speed_10m() + "m/s");
    }

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
                MainController.showAlert("Error", "Guests are not allowed to add markers, please log in or create an account to continue.");
                return;
            }
            CrowdsourcedDTO data = InputService.getCrowdData();
            if (data == null) return;
            try {
                crowdsourcedDataService.createMarker(data);
            } catch (Exception e) {
                MainController.showAlert("Error creating marker", e.getMessage());
            }
        } else if (event.getSource() == btnRefresh) {
            updateData();
        }
    }

    @Override
    public void updateData() {
        hideCrowdPanel();
        pointMapLayer = new PointMapLayer(mapView);
        mapView.addLayer(pointMapLayer);
        addUserLocationsToMap();
        addCrowdDataToMap();
    }
}