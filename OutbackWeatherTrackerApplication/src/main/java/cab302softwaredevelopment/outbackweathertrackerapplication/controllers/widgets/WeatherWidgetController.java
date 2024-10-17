package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WeatherCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WeatherWidgetController extends BaseWidgetController {

  @FXML
  private ImageView imgWeatherIcon;

  @FXML
  private Text txtWeatherCondition, txtDateTime, txtTemperature, txtMinMaxTemp;

  public void updateWidget() {
    HourlyForecast currentForecast = forecastService.getLatestHourlyForecast(location);
    DailyForecast todayForecast = forecastService.getTodayForecast(location);

    if (currentForecast != null && todayForecast != null) {
      WeatherCondition condition = WeatherCondition.fromReading(currentForecast);
      txtWeatherCondition.setText(condition.getName());
      Image weatherIcon = null;
      try {
        String imagePath = ApplicationEntry.class.getResource(condition.getImagePath()).toURI().toString();
        weatherIcon = new Image(imagePath);
      } catch (Exception e) {
        e.printStackTrace();
      }
      imgWeatherIcon.setImage(weatherIcon);
      txtDateTime.setText("00:00");
      txtTemperature.setText(currentForecast.getTemperature_2m() + "°");
      txtMinMaxTemp.setText(
              "H: " + todayForecast.getTemperature_2m_max() + "° " +
              "L: " + todayForecast.getTemperature_2m_min() + "°");
    } else {
      // TODO Show error
    }
  }
}
