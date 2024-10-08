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


public class PrecipitationWidgetController implements IConfigurableWidget {


    @FXML
    private Text txtLocation;

    @FXML
    private Text txtPercentage;

    @FXML
    private Text txtWidgetDescription;

    @FXML
    private Text txtTemperature;

    Location location = null;

    public void applyConfig(WidgetConfig config) {
        long locationId = config.getLocationId();

        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();

        updatePrecipitationWidget();
    }

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
