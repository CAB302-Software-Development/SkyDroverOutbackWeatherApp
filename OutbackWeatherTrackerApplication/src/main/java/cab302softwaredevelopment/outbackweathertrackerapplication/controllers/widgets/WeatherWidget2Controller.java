package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import javax.sound.sampled.Line;

public class WeatherWidget2Controller {
    public Text minMaxTempText;
    public LineChart<Number,Number> Graph;
    public Text Location;

    public void updateWeatherWidget(String location, int high, int low, XYChart.Series<Number,Number> chart) {
        // Update weather condition
        Location.setText(location);

        Graph.getData().add(chart);

        // Update high and low temperatures
        minMaxTempText.setText("H: " + high + "° L: " + low + "°");
    }
}
