package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * A controller class for the Weather widget.
 */
public class WeatherWidget4Controller {
    /**
     * The location text.
     */
    public Text Location;
    /**
     * The time text.
     */
    public Text Time;
    /**
     * The date text.
     */
    public Text Date;
    /**
     * The sunrise icon.
     */
    public ImageView sunrise;
    /**
     * The sunset icon.
     */
    public ImageView sunset;
    /**
     * The cloud coverage text.
     */
    public Text CloudCoverage;
    /**
     * The UV index text.
     */
    public Text UVIndex;

    /**
     * Updates the weather widget with the provided weather information.
     *
     * @param location The location
     * @param time The time
     * @param date The date
     * @param coverage The cloud coverage
     * @param uv The UV index
     */
    public void updateWeatherWidget(String location, String time, String date, int coverage, int uv){

        Location.setText(location);
        Time.setText(time);
        Date.setText(date);
        CloudCoverage.setText(coverage+"%");
        UVIndex.setText(uv+ "");

    }
}
