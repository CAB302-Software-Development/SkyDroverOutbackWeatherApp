package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
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

/**
 * Controller for the Current Temperature widget.
 */
public class CurrentTempController implements IConfigurableWidget {
    /**
     * The image view for the weather icon.
     */
    @FXML
    public ImageView weatherIconImageView;
    /**
     * The label for the date and time.
     */
    @FXML
    public Label lblDateTime, lblTemp, lblLocation;
    /**
     * The root VBox for the widget.
     */
    @FXML
    public VBox root;
    /**
     * The line chart for the temperature data.
     */
    @FXML
    LineChart<Number,Number> lineChart;
    /**
     * The button to configure the widget.
     */
    @FXML
    private Button configButton;
    /**
     * The button to remove the widget.
     */
    @FXML
    private Button removeButton;

    Location location = null;
    /**
     * Initialises the widget, and applies the configuration.
     *
     * @param config The configuration for the widget.
     */
    public void applyConfig(WidgetConfig config) {
        long locationId = config.getLocationId();

        location = new LocationDAO.LocationQuery()
                .whereId(locationId)
                .getSingleResult();

        loadTemperatureData(LoginState.getCurrentAccount().getPreferCelsius());
    }

    private void loadTemperatureData(boolean preferCelsius) {
        if (location == null) {
            lblTemp.setText("No location set.");
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

        double temperature = (preferCelsius) ?
                forecast.getTemperature_2m() :
                ((forecast.getTemperature_2m() * 9) / 5) + 32;

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(forecast.getTimestamp()), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);
        lblDateTime.setText(formattedDate);
        lblTemp.setText(String.format("%.1f° %s", temperature, preferCelsius ? "Celsius" : "Fahrenheit"));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(configButton.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
