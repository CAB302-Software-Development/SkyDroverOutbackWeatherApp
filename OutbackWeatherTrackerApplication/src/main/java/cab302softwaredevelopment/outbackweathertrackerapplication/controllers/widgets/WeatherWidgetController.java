package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WeatherWidgetController {

    @FXML
  private Text weatherConditionText;

  @FXML
  private ImageView weatherIconImageView;

  @FXML
  private Text dateTimeText;

  @FXML
  private Text temperatureText;

  @FXML
  private Text minMaxTempText;

  // Method to update the weather widget
  public void updateWeatherWidget(String condition, String iconPath, String dateTime, int temperature, int high, int low) {
    // Update weather condition
    weatherConditionText.setText(condition);

    // Update weather icon
    Image weatherIcon = new Image(iconPath);
    weatherIconImageView.setImage(weatherIcon);

    // Update date and time
    dateTimeText.setText(dateTime);

    // Update temperature
    temperatureText.setText(temperature + "°");

    // Update high and low temperatures
    minMaxTempText.setText("H: " + high + "° L: " + low + "°");
  }
}
