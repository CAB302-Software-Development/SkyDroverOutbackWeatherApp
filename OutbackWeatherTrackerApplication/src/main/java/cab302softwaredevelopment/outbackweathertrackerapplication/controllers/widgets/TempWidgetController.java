package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TempWidgetController extends BaseWidgetController {
    @FXML
    public Label lblLocation, lblCurrTempLabel, lblSoilTempLabel, lblFeelsTempLabel, lblSurfTempLabel;
    @FXML
    public Text txtCurrTemp, txtSoilTemp, txtFeelsTemp, txtSurfTemp;

    public void updateWidget() {
        HourlyForecast currentForecast = forecastService.getLatestHourlyForecast(location);
        if (currentForecast != null) {
            lblLocation.setText(currentForecast.getLocation().getName());
            txtCurrTemp.setText(currentForecast.getTemperature_2m() + "°");
            txtSoilTemp.setText(currentForecast.getSoil_temperature_0_to_10cm() + "°" );
            txtFeelsTemp.setText(currentForecast.getApparent_temperature() + "°");
            txtSurfTemp.setText(currentForecast.getSurface_temperature() + "°");
        }
    }
}
