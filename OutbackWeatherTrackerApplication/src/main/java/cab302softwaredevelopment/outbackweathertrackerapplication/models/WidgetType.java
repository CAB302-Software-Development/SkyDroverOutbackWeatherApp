package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;

/**
 * An enumeration of the available widget types for the application.
 */
public enum WidgetType {
    /**
     * The Forecast widget.
     */
    @SerializedName("Forecast")
    Forecast("widgets/forecast-widget.fxml"),
    /**
     * The Placeholder widget.
     */
    @SerializedName("Placeholder")
    Placeholder("widgets/placeholder-widget.fxml"),
    /**
     * The CurrentTemp widget.
     */
    @SerializedName("CurrentTemp")
    CurrentTemp("widgets/currenttemp-widget.fxml"),
    /**
     * The Precipitation widget.
     */
    @SerializedName("Precipitation")
    Precipitation("widgets/precipitation-design-1.fxml");


    private final String filepath;

    /**
     * Constructor for WidgetType
     * @param filepath String
     */
    WidgetType(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Gets the file path for the widget type.
     *
     * @return The file path for the widget type
     */
    public String getFilepath() {
        return filepath;
    }

}
