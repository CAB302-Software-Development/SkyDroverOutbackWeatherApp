package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ForecastService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

public class WindWidgetController extends BaseWidgetController {
    @FXML
    public Label lblGust, lblWind, lblDirection;
    @FXML
    public Text txtLocation, txtWindspeed, txtGustSpeed, txtNSEW;

    public void updateWidget() {
        HourlyForecast currentForecast = ForecastService.getLatestHourlyForecast(location);
        if (currentForecast == null) {
            // Display error?
        } else {
            txtLocation.setText(currentForecast.getLocation().getName());
            txtWindspeed.setText(currentForecast.getWind_direction_10m() + " km/h");
            txtGustSpeed.setText(currentForecast.getWind_gusts_10m() + " km/h");
            txtNSEW.setText(currentForecast.getWind_direction_10m() + "");
        }
    }
}
