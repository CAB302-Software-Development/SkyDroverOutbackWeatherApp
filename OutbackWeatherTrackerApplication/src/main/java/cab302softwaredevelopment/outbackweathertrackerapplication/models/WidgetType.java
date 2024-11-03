package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;

public enum WidgetType {
    @SerializedName("CurrentTemp")
    CurrentTemp("widgets/currenttemp-widget.fxml"),
    @SerializedName("Precipitation")
    Precipitation("widgets/precipitation-design-1.fxml"),
    @SerializedName("Soil")
    Soil("widgets/Soil-widget.fxml"),
    @SerializedName("Temperature")
    Temperature("widgets/temperature-widget.fxml"),
    @SerializedName("Wind")
    Wind("widgets/Wind-Widget.fxml"),

    @SerializedName("Weather1")
    Weather1("widgets/weather-widget.fxml"),
    @SerializedName("Weather2")
    Weather2("widgets/weather-widget-2.fxml"),
    @SerializedName("Weather3")
    Weather3("widgets/weather-widget-3.fxml"),
    @SerializedName("Weather4")
    Weather4("widgets/weather-widget-4.fxml");


    private final String filepath;

    WidgetType(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

}
