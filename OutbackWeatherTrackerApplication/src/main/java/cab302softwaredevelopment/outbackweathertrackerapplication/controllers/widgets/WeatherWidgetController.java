package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * A controller class for the Weather widget.
 */
public class WeatherWidgetController {

  /**
   * The weather condition text.
   */
  @FXML
  private Text weatherConditionText;
  /**
   * The weather icon image view.
   */
  @FXML
  private ImageView weatherIconImageView;
  /**
   * The date and time text.
   */
  @FXML
  private Text dateTimeText;
  /**
   * The temperature text.
   */
  @FXML
  private Text temperatureText;
  /**
   * The high and low temperature text.
   */
  @FXML
  private Text minMaxTempText;

  /**
   * Updates the weather widget with the provided weather information.
   *
   * @param condition The weather condition
   * @param iconPath The path to the weather icon
   * @param dateTime The date and time
   * @param temperature The temperature
   * @param high The high temperature
   * @param low The low temperature
   */
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
