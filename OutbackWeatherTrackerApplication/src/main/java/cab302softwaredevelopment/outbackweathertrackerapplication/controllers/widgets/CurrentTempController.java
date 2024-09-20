package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.Instant;
public class CurrentTempController extends BaseWidgetController implements Initializable {
    @FXML
    public ImageView weatherIconImageView;
    @FXML
    public Label lblDateTime, lblTemp, lblLocation;
    @FXML
    public VBox root;
    @FXML
    LineChart<Number,Number> lineChart;
    @FXML
    private Button configButton;
    @FXML
    private Button removeButton;

    LocationDAO locationDAO;
    HourlyForecastDAO hourlyForecastDAO;
    Sdk sdk;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locationDAO = new LocationDAO();
        hourlyForecastDAO = new HourlyForecastDAO();
        sdk = new Sdk();
    }

    @ConfigMethod
    public void applyConfig(double locationId) {
        loadTemperatureData((int)locationId, "Celsius");
    }

    private void loadTemperatureData(int locationId, String unit) {
        if (locationId == -1) {
            lblTemp.setText("No location set.");
            return;
        }

        Location location = locationDAO.getById(locationId);
        if (location == null) {
            lblTemp.setText("Invalid location.");
            return;
        }

        lblLocation.setText(location.getName());

        List<HourlyForecast> forecasts = hourlyForecastDAO.getByLocation(location);
        if (forecasts.size() == 0) {
            forecasts = sdk.getHourlyForecast(location, 2, 2);
            forecasts.forEach(f -> hourlyForecastDAO.insert(f));
        }

        long now = Instant.now().getEpochSecond();
        HourlyForecast forecast = forecasts.stream()
                .filter(f -> f.getTimestamp() >= now)
                .min(Comparator.comparingInt(HourlyForecast::getTimestamp))
                .orElse(null);

        assert forecast != null;
        double temperature = forecast.getTemperature_2m();
        if (unit.equals("Fahrenheit")) {
            temperature = temperature * 9 / 5 + 32;
        }

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(forecast.getTimestamp()), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);
        lblDateTime.setText(formattedDate);
        lblTemp.setText(String.format("%.1f° %s", temperature, unit));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(configButton.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
