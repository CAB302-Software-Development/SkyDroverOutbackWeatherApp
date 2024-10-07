package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WeatherWidget4Controller {
    public Text Location;
    public Text Time;
    public Text Date;
    public ImageView sunrise;
    public ImageView sunset;
    public Text CloudCoverage;
    public Text UVIndex;

    public void updateWeatherWidget(String location, String time, String date, int coverage, int uv){

        Location.setText(location);
        Time.setText(time);
        Date.setText(date);
        CloudCoverage.setText(coverage+"%");
        UVIndex.setText(uv+ "");

    }
}
