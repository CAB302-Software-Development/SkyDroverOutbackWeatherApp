package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.services.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public abstract class BaseWidgetController implements IConfigurableWidget {
    Location location;
    UserService userService;
    LocationService locationService;
    ForecastService forecastService;
    @FXML
    StackPane root;
    Label lblError = new Label();
    VBox errorOverlay = new VBox(lblError);

    /**
     * Method to apply widget configuration.
     * This will be called after the widget is instantiated by the factory.
     *
     * @param config Configuration data for the widget.
     */
    @Override
    public void applyConfig(WidgetConfig config) {
        userService = UserService.getInstance();
        locationService = LocationService.getInstance();
        forecastService = ForecastService.getInstance();

        clearErrorOverlay();
        long locationId = config.getLocationId();
        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();
        if (location == null) {
            displayErrorOverlay("Location not found");
        }
    }

    protected void clearErrorOverlay() {
        root.getChildren().remove(errorOverlay);
        errorOverlay.getStyleClass().clear();
    }

    protected void displayErrorOverlay(String message) {
        errorOverlay.getStyleClass().add("error-overlay");
        lblError.setText(message);
        root.getChildren().add(errorOverlay);
    }

    // TODO implement automatic overlay when no location is set
    // TODO implement overlay that can be activated when "loading"

    @Override
    public void unregister() {
        WidgetFactory.getWidgetManager().unregisterWidget(this);
    }

    public abstract void updateWidget();
}
