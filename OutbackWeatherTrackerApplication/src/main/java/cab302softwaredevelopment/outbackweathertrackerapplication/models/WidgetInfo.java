package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;

public class WidgetInfo {
    @SerializedName("widgetType")
    public WidgetType type;
    public int rowIndex;
    public int columnIndex;
    public int colSpan;
    public int rowSpan;

    public WidgetInfo() {
    }

    public WidgetInfo(WidgetType type, int rowIndex, int columnIndex, int colSpan, int rowSpan) {
        this.type = type;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }
}
