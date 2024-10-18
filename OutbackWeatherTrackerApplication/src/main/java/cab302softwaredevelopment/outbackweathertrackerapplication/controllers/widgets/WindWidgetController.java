package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DateData;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * A controller class for the Wind widget.
 */
public class WindWidgetController implements IConfigurableWidget {

    /**
     * The location text.
     */
    public Text txtLocation;
    /**
     * The wind speed text.
     */
    public Label Gust;
    /**
     * The wind direction text.
     */
    public Label Wind;
    /**
     * The wind speed text.
     */
    public Label Direction;
    /**
     * The wind speed text.
     */
    public Text txtWindspeed;
    /**
     * The wind gust speed text.
     */
    public Text txtGustSpeed;
    /**
     * The wind direction text.
     */
    public Text NSEW;

    Location location = null;

    /**
     * Applies the provided configuration to the widget.
     */
    public void applyConfig(WidgetConfig config) {
        long locationId = config.getLocationId();

        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();

        updateWindWidget();
    }
    /**
     * Updates the wind widget with the provided wind information.
     */
    public void updateWindWidget() {
        int nowEpoch = (int) DateData.getNearestHourEpoch(ZonedDateTime.now());
        HourlyForecast currentForecast = new HourlyForecastDAO.HourlyForecastQuery()
                .whereTimestamp(nowEpoch)
                .getSingleResult();

        if (currentForecast != null) {
            txtLocation.setText(currentForecast.getLocation().getName());
            txtWindspeed.setText(currentForecast.getWind_direction_10m() + " km/h");
            txtGustSpeed.setText(currentForecast.getWind_gusts_10m() + " km/h");
            NSEW.setText(currentForecast.getWind_direction_10m() + "");
        }
    }
}
