package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;


import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class PrecipitationWidgetController extends BaseWidgetController {
    @FXML
    private Text txtLocation;

    @FXML
    private Text txtPercentage;

    @FXML
    private Text txtWidgetDescription;

    @FXML
    private Text txtTemperature;

    public void updateWidget() {
        HourlyForecast latestForecast = getLatestHourlyForecast();
        txtLocation.setText(latestForecast.getLocation().getName());
        txtPercentage.setText(latestForecast.getCloud_cover().intValue() + "%");
        txtWidgetDescription.setText("Description");
        txtTemperature.setText(latestForecast.getApparent_temperature().intValue() + "°");
    }

}
