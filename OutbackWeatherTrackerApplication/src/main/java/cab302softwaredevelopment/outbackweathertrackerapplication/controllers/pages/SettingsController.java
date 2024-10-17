package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.InputService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class SettingsController extends BasePage {
    @FXML
    public VBox vbRoot;
    @FXML
    private ComboBox<Theme> cboThemes;
    @FXML
    private ListView<Location> lstLocations;
    @FXML
    private Button btnAddLocation, btnDeleteLocation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.updateData();

        ObservableList<Theme> options = FXCollections.observableArrayList(Theme.values());
        cboThemes.setItems(options);

        List<Location> locations = locationService.getCurrentUserLocations();
        lstLocations.setItems(FXCollections.observableArrayList(locations));

        lstLocations.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Location item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        updateData();
    }

    @Override
    public void updateData() {
        cboThemes.getSelectionModel().select(userService.getCurrentAccount().getCurrentTheme());
        refreshLocationList();
    }

    private void refreshLocationList() {
        List<Location> locations = locationService.getCurrentUserLocations();
        lstLocations.setItems(FXCollections.observableArrayList(locations));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnAddLocation) {
            LocationCreateModel location = InputService.getLocation("Select location", "Please select your location on the map");
            if (location != null) {
                locationService.addLocationForCurrentUser(location);
                refreshLocationList();
            }
        } else if (event.getSource() == btnDeleteLocation) {
            Location selectedLocation = lstLocations.getSelectionModel().getSelectedItem();
            if (selectedLocation != null) {
                locationService.deleteLocation(selectedLocation);
                refreshLocationList();
            }
        }
    }

    @FXML
    private void onSwapTheme() {
        Theme currentTheme = userService.getCurrentAccount().getCurrentTheme();
        Theme newTheme = cboThemes.getValue();
        if (currentTheme != newTheme) {
            userService.setCurrentTheme(newTheme);
            MainController.refreshDisplay();
        }
    }
}
