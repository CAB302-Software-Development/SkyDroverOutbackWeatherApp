package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.chart.LineChart;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class WeatherWidget2Controller {
    public Text minMaxTempText;
    public LineChart Graph;
    public Text Location;

    public void updateWeatherWidget(String location,int high, int low) {
        // Update weather condition
        Location.setText(location);

        // Update high and low temperatures
        minMaxTempText.setText("H: " + high + "° L: " + low + "°");
    }
}
