package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class SoilWidgetController extends BaseWidgetController {
    @FXML
    public Text txtET, txtLocation, txtTemperature, txtMoisture;

    public void updateWidget() {
        HourlyForecast currentForecast = forecastService.getLatestHourlyForecast(location);
        if (currentForecast != null) {
            txtLocation.setText(LocationService.getShortName(currentForecast.getLocation()));
            txtMoisture.setText(currentForecast.getSoil_moisture_0_to_10cm() + "%");
            txtET.setText(currentForecast.getEt0_fao_evapotranspiration() + "");
            txtTemperature.setText(currentForecast.getSoil_temperature_0_to_10cm()+"°");
        }
    }
}
