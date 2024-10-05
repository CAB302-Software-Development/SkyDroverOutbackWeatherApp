package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TempWidgetController {
    public Label Location;
    public Label CurrTempLabel;
    public Text CurrTemp;
    public Label SoilTempLabel;
    public Text SoilTemp;
    public Label FeelsTempLabel;
    public Text FeelsTemp;
    public Label SurfTempLabel;
    public Text SurfTemp;

    public void updateTempWidget(int currTemp, int feelsTemp, int surfTemp, int soilTemp){
        CurrTemp.setText(currTemp + "°");
        SoilTemp.setText(feelsTemp + "°" );
        FeelsTemp.setText(surfTemp + "°");
        SurfTemp.setText(soilTemp + "°");
    }
}
