package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.PointMapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;

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


    @Getter
    private LocationCreateModel selectedLocation;
    private PointMapLayer pointMapLayer;

    // TODO fix buttons not clickable when overlayed, or just have them not be overlay

    @Override
    public void initialize() {
        super.initialize();
        Location defaultLocation = locationService.getCurrentUserLocations().getFirst();
        selectedLocation = new LocationCreateModel(defaultLocation);
        initMap(defaultLocation);
    }

    private void initMap(Location defaultLocation) {
        MapPoint initialPoint = new MapPoint(defaultLocation.getLatitude(), defaultLocation.getLongitude());
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
        // List<CrowdData> crowdDataList = crowdsourcesService.getAllCrowdData();
        // if (crowdDataList == null) return;
        // for (CrowdData crowdData : crowdDataList) {
        //     Node node = new StackPane(new Circle(5, Color.RED));
        //     // TODO create node
        //     pointMapLayer.addPoint(crowdData.getLocation(), node);
        // }
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