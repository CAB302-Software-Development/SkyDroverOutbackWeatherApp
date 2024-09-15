package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;

public enum WidgetType {
    @SerializedName("Forecast")
    Forecast("widgets/forecast-widget.fxml"),
    @SerializedName("TodayTemp")
    TodayTemp("widgets/todaytemp-widget.fxml"),
    @SerializedName("Placeholder")
    Placeholder("widgets/placeholder-widget.fxml");


    private final String filepath;

    WidgetType(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

}
