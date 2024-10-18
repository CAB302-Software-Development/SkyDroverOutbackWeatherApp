package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import javax.sound.sampled.Line;

/**
 * Controller for the Weather Widget 2.
 */
public class WeatherWidget2Controller {
    /**
     * The text for the high and low temperatures.
     */
    public Text minMaxTempText;
    /**
     * The line chart for the weather data.
     */
    public LineChart<String, Number> Graph;
    /**
     * The text for the location.
     */
    public Text Location;

    /**
     * Updates the weather widget with the specified information.
     *
     * @param location The location of the weather.
     * @param high The high temperature.
     * @param low The low temperature.
     */
    public void updateWeatherWidget(String location, int high, int low, XYChart.Series<String,Number> chart) {
        // Update weather condition
        Location.setText(location);


        chart = updateChart();
        Graph.getData().add(chart);

        // Update high and low temperatures
        minMaxTempText.setText("H: " + high + "° L: " + low + "°");
    }
    /**
     * Updates the chart with the specified information.
     *
     * @return The updated chart.
     */
    public XYChart.Series<String, Number> updateChart(){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<String,Number>("Today",22));
        series.getData().add(new XYChart.Data<String,Number>("Tomorrow",19));
        series.getData().add(new XYChart.Data<String,Number>("Day 2",10));
        series.getData().add(new XYChart.Data<String,Number>("Day 3",2));

        return series;
    }
}
