package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class WeatherWidget2Controller extends BaseWidgetController {
    @FXML
    public Text txtMinMaxTemp;
    @FXML
    public LineChart lgGraph;
    @FXML
    public Text txtLocation;

    public void updateWidget() {
        DailyForecast todaysForecast = forecastService.getTodayForecast(location);
        txtLocation.setText(todaysForecast.getLocation().getName());
        XYChart.Series<String,Number> chart = updateChart();
        lgGraph.getData().add(chart);
        txtMinMaxTemp.setText(
                "H: " + todaysForecast.getTemperature_2m_max() + "° " +
                "L: " + todaysForecast.getTemperature_2m_min() + "°");
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
