package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WeatherWidget3Controller {
    
    public ImageView weatherIconImageView;
    public Text temperatureText;
    public Text Location;
    public Text Precipitation;
    public Text Wind;
    public Text Humidity;
    public Text FeelsLike;

    public void updateWeatherWidget(int temp, String location, String precipitation, int wind, int humid, int feels){
        temperatureText.setText(temp + "°");
        Location.setText(location);
        Precipitation.setText(precipitation);
        Wind.setText(wind + "km/h");
        Humidity.setText(humid + "%");
        FeelsLike.setText(feels + "°");
// random comment

    }

}
