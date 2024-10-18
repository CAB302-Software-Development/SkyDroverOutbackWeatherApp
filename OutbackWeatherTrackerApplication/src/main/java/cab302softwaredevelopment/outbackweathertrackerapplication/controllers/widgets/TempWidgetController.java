package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 * A controller class for the Temperature widget.
 */
public class TempWidgetController {
    /**
     * The location label.
     */
    public Label Location;
    /**
     * The current temperature label.
     */
    public Label CurrTempLabel;
    /**
     * The current temperature.
     */
    public Text CurrTemp;
    /**
     * The soil temperature label.
     */
    public Label SoilTempLabel;
    /**
     * The soil temperature.
     */
    public Text SoilTemp;
    /**
     * The feels like temperature label.
     */
    public Label FeelsTempLabel;
    /**
     * The feels like temperature.
     */
    public Text FeelsTemp;
    /**
     * The surface temperature label.
     */
    public Label SurfTempLabel;
    /**
     * The surface temperature.
     */
    public Text SurfTemp;

    /**
     * Updates the temperature widget with the provided temperatures.
     *
     * @param currTemp The current temperature
     * @param feelsTemp The feels like temperature
     * @param surfTemp The surface temperature
     * @param soilTemp The soil temperature
     */
    public void updateTempWidget(int currTemp, int feelsTemp, int surfTemp, int soilTemp){
        CurrTemp.setText(currTemp + "°");
        SoilTemp.setText(feelsTemp + "°" );
        FeelsTemp.setText(surfTemp + "°");
        SurfTemp.setText(soilTemp + "°");
    }
}
