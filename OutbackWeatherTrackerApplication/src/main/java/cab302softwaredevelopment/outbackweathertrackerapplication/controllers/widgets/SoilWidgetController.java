package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class SoilWidgetController extends BaseWidgetController {
    @FXML
    public Text txtET;
    @FXML
    public Text txtLocation;
    @FXML
    public Text txtTemperature;
    @FXML
    public Text txtMoisture;

    public void updateWidget() {
        HourlyForecast currentForecast = getLatestHourlyForecast();
        if (currentForecast != null) {
            txtLocation.setText(currentForecast.getLocation().getName());
            txtMoisture.setText(currentForecast.getSoil_moisture_0_to_10cm() + "%");
            txtET.setText(currentForecast.getEt0_fao_evapotranspiration() + "");
            txtTemperature.setText(currentForecast.getSoil_temperature_0_to_10cm()+"°");
        }
    }
}
