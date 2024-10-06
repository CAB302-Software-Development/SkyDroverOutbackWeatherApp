package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.text.Text;

public class SoilWidgetController {
    public  Text ET;
    public Text Location;
    public Text Temperature;
    public Text Moisture;

    public void updatePrecipitationWidget(String location, int temperature, int moisture, int et, String Description) {
        Location.setText(location);
        Moisture.setText(moisture+"%");
        ET.setText(et + "");
        Temperature.setText(temperature+"°");
    }
}
