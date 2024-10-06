package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import javax.sound.sampled.Line;

public class WeatherWidget2Controller {
    public Text minMaxTempText;
    public LineChart Graph;
    public Text Location;

    public void updateWeatherWidget(String location, int high, int low, XYChart.Series<String,Number> chart) {
        // Update weather condition
        Location.setText(location);


        chart = updateChart();
        Graph.getData().add(chart);

        // Update high and low temperatures
        minMaxTempText.setText("H: " + high + "° L: " + low + "°");
    }

    public XYChart.Series updateChart(){
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data<String,Number>("Today",22));
        series.getData().add(new XYChart.Data<String,Number>("Tomorrow",19));
        series.getData().add(new XYChart.Data<String,Number>("Day 2",10));
        series.getData().add(new XYChart.Data<String,Number>("Day 3",2));

        return series;
    }
}
