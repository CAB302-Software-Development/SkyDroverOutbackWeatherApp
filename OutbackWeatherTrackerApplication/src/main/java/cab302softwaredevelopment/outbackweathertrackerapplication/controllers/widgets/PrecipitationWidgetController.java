package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;


import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.time.*;
import java.util.Map;

/**
 * A controller class for the Precipitation widget.
 */
public class PrecipitationWidgetController implements IConfigurableWidget {

    /**
     * The location text.
     */
    @FXML
    private Text txtLocation;
    /**
     * The percentage text.
     */
    @FXML
    private Text txtPercentage;
    /**
     * The widget description text.
     */
    @FXML
    private Text txtWidgetDescription;
    /**
     * The temperature text.
     */
    @FXML
    private Text txtTemperature;

    Location location = null;

    /**
     * Applies the provided configuration to the widget.
     */
    public void applyConfig(WidgetConfig config) {
        long locationId = config.getLocationId();

        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();

        updatePrecipitationWidget();
    }

    /**
     * Updates the precipitation widget with the provided weather information.
     */
    public void updatePrecipitationWidget() {
        int nowEpoch = (int) DateData.getNearestHourEpoch(ZonedDateTime.now());
        HourlyForecast currentForecast = new HourlyForecastDAO.HourlyForecastQuery()
                .whereTimestamp(nowEpoch)
                .getSingleResult();

        txtLocation.setText(currentForecast.getLocation().getName());
        txtPercentage.setText(currentForecast.getCloud_cover().intValue() + "%");
        txtWidgetDescription.setText("Description");
        txtTemperature.setText(currentForecast.getApparent_temperature().intValue() + "°");
    }

}
