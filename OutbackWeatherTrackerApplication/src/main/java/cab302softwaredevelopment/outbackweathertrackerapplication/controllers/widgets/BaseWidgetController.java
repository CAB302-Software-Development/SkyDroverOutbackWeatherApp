package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.services.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;

public abstract class BaseWidgetController implements IConfigurableWidget {
    Location location;
    UserService userService;
    LocationService locationService;
    ForecastService forecastService;

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

        long locationId = config.getLocationId();
        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();
    }

    // TODO implement automatic overlay when no location is set
    // TODO implement overlay that can be activated when "loading"

    @Override
    public void unregister() {
        WidgetFactory.getWidgetManager().unregisterWidget(this);
    }

    public abstract void updateWidget();
}
