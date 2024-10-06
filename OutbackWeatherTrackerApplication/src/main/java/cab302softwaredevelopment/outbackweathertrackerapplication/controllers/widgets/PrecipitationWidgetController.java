package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;


import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class PrecipitationWidgetController extends BaseWidgetController{


    @FXML
    private Text Location;

    @FXML
    private Text Percentage;

    @FXML
    private Text WidgetDescription;

    @FXML
    private Text Temperature;

    public void updatePrecipitationWidget(String location, int percentage, int temperature, String Description) {
        Location.setText(location);
        Percentage.setText(percentage+"%");
        WidgetDescription.setText(Description);
        Temperature.setText(temperature+"°");
    }

}
