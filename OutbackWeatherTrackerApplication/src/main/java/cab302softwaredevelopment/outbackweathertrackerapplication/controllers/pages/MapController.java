package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CrowdsourcedDataDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.CrowdsourcedDataService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.PointMapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    public VBox vbLocationForecastView;
    @FXML
    private Button btnSearch, btnAddMarker;

    CrowdsourcedDataService crowdsourcedDataService;

    @Getter
    private LocationCreateModel selectedLocation = null;
    private PointMapLayer pointMapLayer;

    // TODO fix buttons not clickable when overlayed, or just have them not be overlay

    @Override
    public void initialize() {
        super.initialize();
        crowdsourcedDataService = CrowdsourcedDataService.getInstance();
        initMap();
    }

    private void initMap() {
        MapPoint initialPoint = new MapPoint(-25.2744, 133.7751);
        mapView.setZoom(10.0);
        mapView.flyTo(0, initialPoint, 0);

        mapView.setOnMouseClicked((MouseEvent event) -> {
            if (event.isStillSincePress()) {
                MapPoint clickedPos = mapView.getMapPosition(event.getX(), event.getY());
                if (clickedPos == null) return;
                double latitude = clickedPos.getLatitude();
                double longitude = clickedPos.getLongitude();
                setSelectedLocation(new LocationCreateModel(null, latitude, longitude, 0));
            }
        });
        pointMapLayer = new PointMapLayer(mapView);
        mapView.addLayer(pointMapLayer);
        addCrowdDataToMap();
    }

    private void addCrowdDataToMap() {
        try {
            List<CrowdsourcedDataDTO> crowdDataList = crowdsourcedDataService.getLatestFilteredData();
            if (crowdDataList == null) return;

            for (CrowdsourcedDataDTO crowdData : crowdDataList) {
                Circle pinCircle = new Circle(10, Color.RED);
                pinCircle.setStroke(Color.BLACK);
                pinCircle.setStrokeWidth(2);
                Text tempText = new Text(crowdData.getActualTemp() + "°");
                tempText.setFill(Color.WHITE);
                StackPane pin = new StackPane(pinCircle, tempText);
                pin.setTranslateX(-10);
                pin.setTranslateY(-10);
                pin.setOnMouseClicked(event -> showCrowdDataDetails(crowdData));
                MapPoint location = new MapPoint(crowdData.getLatitude(), crowdData.getLongitude());
                pointMapLayer.addPoint(location, pin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCrowdDataDetails(CrowdsourcedDataDTO crowdData) {
        vbCrowdDataView.getChildren().clear();
        List<Node> details = new ArrayList<>();

        if (crowdData.getLocation() != null) {
            details.add(new Label("Location: " + crowdData.getLocation()));
        }
        if (crowdData.getUserName() != null) {
            details.add(new Label("User: " + crowdData.getUserName()));
        }
        if (crowdData.getActualTemp() != null) {
            details.add(new Label("Actual Temp: " + crowdData.getActualTemp() + "°C"));
        }
        if (crowdData.getFeelsLikeTemp() != null) {
            details.add(new Label("Feels Like Temp: " + crowdData.getFeelsLikeTemp() + "°C"));
        }

        vbCrowdDataView.getChildren().addAll(details);
    }


    private void setSelectedLocation(LocationCreateModel newLocation) {
        if (newLocation == null) return;
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


    @FXML
    private void handleButtonPress(ActionEvent event) {
        if (event.getSource() == btnSearch) {
            LocationCreateModel newLocation = locationService.geocodeAddress(txtSearchBar.getText());
            if (newLocation == null) {
                // TODO show error
            } else {
                txtSearchBar.setText("");
                setSelectedLocation(newLocation);
            }
        }

        // TODO add submit button
    }

    @Override
    public void updateData() {
        // TODO add refresh after new data submitted
    }
}