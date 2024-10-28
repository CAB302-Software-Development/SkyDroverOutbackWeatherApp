package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.PointMapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class MapController extends BasePage {
    @FXML
    public MapView mapView;
    @FXML
    public BorderPane bpOverlayUI;
    @FXML
    public TextField txtSearchBar;
    @FXML
    private Button btnSearch, btnAddMarker;

    @Getter
    private LocationCreateModel selectedLocation;
    private PointMapLayer pointMapLayer;
    private Point2D clickedPoint;

    @Override
    public void initialize() {
        super.initialize();
        Location defaultLocation = locationService.getCurrentUserLocations().getFirst();
        selectedLocation = new LocationCreateModel(defaultLocation);
        initMap(defaultLocation);
    }

    // TODO fix points not adding to map

    // TODO fix buttons not clickable when overlayed, or just have them not be overlay

    private void initMap(Location defaultLocation) {
        MapPoint initialPoint = new MapPoint(defaultLocation.getLatitude(), defaultLocation.getLongitude());
        mapView.setZoom(10.0);
        mapView.flyTo(0, initialPoint, 0);
        clickedPoint = new Point2D(defaultLocation.getLatitude(), defaultLocation.getLongitude());

        mapView.setOnMouseClicked((MouseEvent event) -> {
            if (event.isStillSincePress()) {
                MapPoint clickedPos = mapView.getMapPosition(event.getX(), event.getY());
                if (clickedPos == null) return;
                double latitude = clickedPos.getLatitude();
                double longitude = clickedPos.getLongitude();
                setSelectedLocation(new LocationCreateModel(null, latitude, longitude, 0));
            }
        });
        pointMapLayer = new PointMapLayer();
        mapView.addLayer(pointMapLayer);
    }

    private Point2D latLonToPoint(LocationCreateModel mapPoint) {
        double zoom = mapView.getZoom();
        double mapWidth = mapView.getWidth();
        double mapHeight = mapView.getHeight();

        MapPoint center = mapView.getCenter();

        double centerX = lonToX(center.getLongitude(), zoom);
        double centerY = latToY(center.getLatitude(), zoom);

        double pointX = lonToX(mapPoint.getLongitude(), zoom);
        double pointY = latToY(mapPoint.getLatitude(), zoom);

        double screenX = (mapWidth / 2) + (pointX - centerX);
        double screenY = (mapHeight / 2) - (pointY - centerY);

        return new Point2D(screenX, screenY);
    }

    private double lonToX(double lon, double zoom) {
        double tileSize = 256.0; // Assuming tile size of 256 pixels
        double scale = (1 << (int) zoom) * tileSize;
        return (lon + 180.0) / 360.0 * scale;
    }

    private double latToY(double lat, double zoom) {
        double tileSize = 256.0; // Assuming tile size of 256 pixels
        double scale = (1 << (int) zoom) * tileSize;
        double siny = Math.sin(Math.toRadians(lat));
        siny = Math.min(Math.max(siny, -0.9999), 0.9999);
        return (0.5 - Math.log((1 + siny) / (1 - siny)) / (4 * Math.PI)) * scale;
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
        pointMapLayer.addPoint(new MapPoint(selectedLocation.getLatitude(), selectedLocation.getLongitude()));
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
    }

    @Override
    public void updateData() {

    }
}