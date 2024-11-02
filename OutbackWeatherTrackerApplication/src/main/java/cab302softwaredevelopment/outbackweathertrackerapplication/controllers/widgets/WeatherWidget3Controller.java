package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
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
        HourlyForecast currentForecast = forecastService.getLatestHourlyForecast(location);
        if (currentForecast != null) {
            txtLocation.setText(LocationService.getShortName(currentForecast.getLocation()));
            txtTemperature.setText(currentForecast.getTemperature_2m() + "°");
            txtPrecipitation.setText(currentForecast.getPrecipitation() + "");
            txtWind.setText(currentForecast.getWind_speed_10m() + "km/h");
            txtHumidity.setText(currentForecast.getRelative_humidity_2m() + "%");
            txtFeelsLike.setText(currentForecast.getApparent_temperature() + "°");
        }
    }
}