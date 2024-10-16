package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ForecastService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.util.Map;

public class CurrentTempController extends BaseWidgetController {
    @FXML
    public Label lblDateTime, lblTemp, lblLocation;
    @FXML
    public VBox root;

    @Override
    public void updateWidget() {
        loadTemperatureData(LoginState.getCurrentAccount().getPreferCelsius());
    }

    private void loadTemperatureData(boolean preferCelsius) {
        if (location == null) {
            lblTemp.setText("No location set.");
            return;
        }
        lblLocation.setText(location.getName());
        HourlyForecast forecast = ForecastService.getLatestHourlyForecast(location);

        if (forecast == null) {
            lblTemp.setText("No forecast found.");
            return;
        }

        double temperature = (preferCelsius) ?
                forecast.getTemperature_2m() :
                ((forecast.getTemperature_2m() * 9) / 5) + 32;

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(forecast.getTimestamp()), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);
        lblDateTime.setText(formattedDate);
        lblTemp.setText(String.format("%.1f° %s", temperature, preferCelsius ? "Celsius" : "Fahrenheit"));
    }
}
