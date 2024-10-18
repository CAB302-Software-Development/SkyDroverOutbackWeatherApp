package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * A controller class for the Weather widget.
 */
public class WeatherWidget3Controller {
    /**
     * The weather icon image view.
     */
    public ImageView weatherIconImageView;
    /**
     * The current temperature text.
     */
    public Text temperatureText;
    /**
     * The location text.
     */
    public Text Location;
    /**
     * The precipitation text.
     */
    public Text Precipitation;
    /**
     * The wind text.
     */
    public Text Wind;
    /**
     * The humidity text.
     */
    public Text Humidity;
    /**
     * The feels like temperature text.
     */
    public Text FeelsLike;

    /**
     * Updates the weather widget with the provided weather information.
     *
     * @param temp The current temperature
     * @param location The location
     * @param precipitation The precipitation
     * @param wind The wind speed
     * @param humid The humidity
     * @param feels The feels like temperature
     */
    public void updateWeatherWidget(int temp, String location, String precipitation, int wind, int humid, int feels){
        temperatureText.setText(temp + "°");
        Location.setText(location);
        Precipitation.setText(precipitation);
        Wind.setText(wind + "km/h");
        Humidity.setText(humid + "%");
        FeelsLike.setText(feels + "°");


    }

}
