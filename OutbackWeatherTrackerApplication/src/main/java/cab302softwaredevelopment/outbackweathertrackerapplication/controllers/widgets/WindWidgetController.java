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

public class WindWidgetController implements IConfigurableWidget {


    public Text txtLocation;
    public Label Gust;
    public Label Wind;
    public Label Direction;
    public Text txtWindspeed;
    public Text txtGustSpeed;
    public Text NSEW;

    Location location = null;

    public void applyConfig(WidgetConfig config) {
        long locationId = config.getLocationId();

        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();

        updateWindWidget();
    }

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
