package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;

public enum Theme {
    @SerializedName("Light")
    Light("themes/theme-light.css"),
    @SerializedName("Dark")
    Dark("themes/theme-dark.css");

    private final String filePath;
    Theme(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
