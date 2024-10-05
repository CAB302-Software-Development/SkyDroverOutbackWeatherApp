package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import java.awt.*;

public class WindWidgetController extends BaseWidgetController {


    public Text Location;
    public Label Gust;
    public Label Wind;
    public Label Direction;
    public Text Windspeed;
    public Text GustSpeed;
    public Text NSEW;

    public void updateWindWidget(String location, int windspeed, int gustspeed, int dir){
        Location.setText(location);
        Windspeed.setText(windspeed+" km/h");
        GustSpeed.setText(gustspeed+" km/h");
        NSEW.setText(dir+"");
    }

}
