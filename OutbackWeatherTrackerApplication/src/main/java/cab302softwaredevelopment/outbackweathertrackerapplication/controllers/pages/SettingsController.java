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
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Controller class for managing application settings, including theme selection and location management.
 * Provides options to change the application's theme, add new locations, and delete existing locations.
 */
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
    public void initialize() {
        super.initialize();
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

    /**
     * Updates the settings data, including selecting the current theme and refreshing the location list.
     */
    @Override
    public void updateData() {
        cboThemes.getSelectionModel().select(userService.getCurrentAccount().getCurrentTheme());
        refreshLocationList();
    }

    /**
     * Refreshes the list of locations displayed in the ListView by reloading the user's current locations.
     */
    private void refreshLocationList() {
        List<Location> locations = locationService.getCurrentUserLocations();
        lstLocations.setItems(FXCollections.observableArrayList(locations));
    }

    /**
     * Handles button actions for adding or deleting a location.
     * Opens a location selection dialog for adding locations and updates the UI after deletion.
     *
     * @param event The ActionEvent triggered by button presses.
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnAddLocation) {
            LocationCreateModel location = InputService.getLocation("Select location", "Please select your location on the map");
            if (location != null) {
                locationService.addLocationForCurrentUser(location);
                MainController.getController().updateUIData();
                refreshLocationList();
            }
        } else if (event.getSource() == btnDeleteLocation) {
            Location selectedLocation = lstLocations.getSelectionModel().getSelectedItem();
            if (selectedLocation != null) {
                if (lstLocations.getItems().size() == 1) {
                    InputService.showAlert("Must have location", "Please add another location before deleting all locations");
                    return;
                }
                locationService.deleteLocation(selectedLocation);
                MainController.getController().updateUIData();
                refreshLocationList();
            }
        }
    }

    /**
     * Changes the application's theme when a new theme is selected, refreshing the UI to apply the change.
     */
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
