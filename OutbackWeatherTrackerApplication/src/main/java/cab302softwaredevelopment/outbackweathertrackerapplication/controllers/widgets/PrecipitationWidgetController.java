package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PrecipitationWidgetController extends BaseWidgetController {
    @FXML
    private Text txtLocation, txtPercentage, txtWidgetDescription, txtTemperature;

    public void updateWidget() {
        HourlyForecast latestForecast = forecastService.getLatestHourlyForecast(location);
        if (latestForecast != null) {
            txtLocation.setText(latestForecast.getLocation().getName());
            txtPercentage.setText(latestForecast.getCloud_cover().intValue() + "%");
            txtWidgetDescription.setText("Description");
            txtTemperature.setText(latestForecast.getApparent_temperature().intValue() + "°");
        }
    }

}
