package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
public class CurrentTempController extends BaseWidgetController {
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


    @ConfigMethod
    public void applyConfig(double locationId) {
        loadTemperatureData((long) locationId, "Celsius");
    }

    private void loadTemperatureData(long locationId, String unit) {
        if (locationId == -1) {
            lblTemp.setText("No location set.");
            return;
        }

        Location location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();
        if (location == null) {
            lblTemp.setText("Invalid location.");
            return;
        }

        lblLocation.setText(location.getName());

        long now = Instant.now().getEpochSecond();
        HourlyForecast forecast = new HourlyForecastDAO.HourlyForecastQuery()
                .whereLocation(location)
                .whereTimestampGE((int) now)
                .addOrderAsc("timestamp")
                .getSingleResult();

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
