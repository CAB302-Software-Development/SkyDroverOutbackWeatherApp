package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public abstract class BaseWidgetController implements IConfigurableWidget {
    Location location;

    /**
     * Method to apply widget configuration.
     * This will be called after the widget is instantiated by the factory.
     *
     * @param config Configuration data for the widget.
     */
    @Override
    public void applyConfig(WidgetConfig config) {
        long locationId = config.getLocationId();
        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();
    }

    @Override
    public void unregister() {
        WidgetFactory.getWidgetManager().unregisterWidget(this);
    }

    public abstract void updateWidget();
}
