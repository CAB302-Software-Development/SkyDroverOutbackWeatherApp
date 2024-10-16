package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import javafx.fxml.FXML;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ForecastService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WeatherWidget3Controller extends BaseWidgetController {
    @FXML
    public ImageView weatherIconImageView;
    @FXML
    public Text txtTemperature, txtLocation, txtPrecipitation, txtWind, txtHumidity, txtFeelsLike;

    public void updateWidget() {
        HourlyForecast currentForecast = ForecastService.getLatestHourlyForecast(location);
        if (currentForecast != null) {
            txtTemperature.setText(currentForecast.getTemperature_2m() + "°");
            txtLocation.setText(currentForecast.getLocation().getName());
            txtPrecipitation.setText(currentForecast.getPrecipitation() + "");
            txtWind.setText(currentForecast.getWind_speed_10m() + "km/h");
            txtHumidity.setText(currentForecast.getRelative_humidity_2m() + "%");
            txtFeelsLike.setText(currentForecast.getApparent_temperature() + "°");
        }
    }
}