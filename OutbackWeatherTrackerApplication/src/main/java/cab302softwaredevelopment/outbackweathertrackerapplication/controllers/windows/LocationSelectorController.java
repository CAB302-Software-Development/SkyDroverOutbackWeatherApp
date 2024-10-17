package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import java.net.URL;
import java.util.ResourceBundle;

public class LocationSelectorController implements Initializable {
    @FXML
    private MapView mapView;

    @FXML
    private TextField addressField;

    @FXML
    private Button btnConfirm, btnCancel, btnSearch;

    @Getter
    private LocationCreateModel selectedLocation;
    private LocationService locationService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.locationService = LocationService.getInstance();
        MapPoint initialPoint = new MapPoint(-27.467331464, 153.02333324);
        mapView.setZoom(10.0);
        mapView.flyTo(0, initialPoint, 1.0);
        mapView.setOnMouseClicked((MouseEvent event) -> {
            if (event.isStillSincePress()) {
                MapPoint clickedPoint = mapView.getMapPosition(event.getX(), event.getY());
                if (clickedPoint == null) return;
                double latitude = clickedPoint.getLatitude();
                double longitude = clickedPoint.getLongitude();
                setSelectedLocation(new LocationCreateModel(null, latitude, longitude, 0));
            }
        });
    }

    @FXML
    private void handleButtonPress(ActionEvent event) {
        if (event.getSource() == btnConfirm) {
            closePopup();
        } else if (event.getSource() == btnCancel) {
            selectedLocation = null;
            closePopup();
        } else if (event.getSource() == btnSearch) {
            LocationCreateModel newLocation = locationService.geocodeAddress(addressField.getText());
            if (newLocation == null) {
                // TODO show error
            } else {
                setSelectedLocation(newLocation);
            }
        }
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
        addressField.setText(newLocation.getName());
        selectedLocation = newLocation;
    }

    private void closePopup() {
        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.close();
    }
}