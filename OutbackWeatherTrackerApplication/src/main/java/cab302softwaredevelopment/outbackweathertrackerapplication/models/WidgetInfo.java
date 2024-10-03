package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class WidgetInfo {
    @SerializedName("widgetType")
    public WidgetType type;
    public int rowIndex;
    public int columnIndex;
    public int colSpan;
    public int rowSpan;
    public Map<String, Object> config;

    public WidgetInfo() {
    }

    public WidgetInfo(WidgetType type, int rowIndex, int columnIndex, int colSpan, int rowSpan, Map<String, Object> config) {
        this.type = type;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.config = config;
    }
}
