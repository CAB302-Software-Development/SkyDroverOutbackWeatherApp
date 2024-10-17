package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WeatherWidget4Controller extends BaseWidgetController {
    @FXML
    public Text txtLocation, txtTime, txtDate, txtCloudCoverage, txtUVIndex;
    @FXML
    public ImageView imgSunrise, imgSunset;

    public void updateWidget() {
        HourlyForecast currentForecast = forecastService.getLatestHourlyForecast(location);
        if (currentForecast != null) {
            txtLocation.setText(currentForecast.getLocation().getName());
            // Instant readingTime = Instant.ofEpochSecond(currentForecast.getTimestamp());
            // LocalDate localDate = readingTime.atZone(ZoneId.systemDefault()).toLocalDate();
            txtTime.setText("00:00");
            txtDate.setText("0/0/0000");
            txtCloudCoverage.setText(currentForecast.getCloud_cover() + "%");
            txtUVIndex.setText(currentForecast.getShortwave_radiation() + "");
        }
    }
}
