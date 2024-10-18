package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.text.Text;
/**
 * A controller class for the Soil widget.
 */
public class SoilWidgetController {
    /**
     * The evapotranspiration text.
     */
    public  Text ET;
    /**
     * The location text.
     */
    public Text Location;
    /**
     * The temperature text.
     */
    public Text Temperature;
    /**
     * The moisture text.
     */
    public Text Moisture;

    /**
     * Updates the soil widget with the provided soil information.
     *
     * @param location The location
     * @param temperature The temperature
     * @param moisture The moisture
     * @param et The evapotranspiration
     * @param Description The weather description
     */
    public void updatePrecipitationWidget(String location, int temperature, int moisture, int et, String Description) {
        Location.setText(location);
        Moisture.setText(moisture+"%");
        ET.setText(et + "");
        Temperature.setText(temperature+"°");
    }
}
