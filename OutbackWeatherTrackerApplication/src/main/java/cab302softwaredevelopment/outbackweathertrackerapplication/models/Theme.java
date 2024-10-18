package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;

/**
 * An enumeration of the available themes for the application.
 */
public enum Theme {
    /**
     * The Light theme.
     */
    @SerializedName("Light")
    Light("themes/theme-light.css"),
    /**
     * The Dark theme.
     */
    @SerializedName("Dark")
    Dark("themes/theme-dark.css");

    private final String filePath;
    Theme(String filePath) {
        this.filePath = filePath;
    }
    /**
     * Gets the file path for the theme.
     *
     * @return The file path for the theme
     */
    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
