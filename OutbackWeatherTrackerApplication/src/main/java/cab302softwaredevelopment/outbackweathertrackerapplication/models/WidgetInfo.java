package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Data class for storing widget information
 */
public class WidgetInfo {
    /**
     * The type of widget.
     */
    @SerializedName("widgetType")
    /**
     * The type of widget.
     */
    public WidgetType type;
    /**
     * The row index of the widget.
     */
    public int rowIndex;
    /**
     * The column index of the widget.
     */
    public int columnIndex;
    /**
     * The column span of the widget.
     */
    public int colSpan;
    /**
     * The row span of the widget.
     */
    public int rowSpan;
    /**
     * The configuration of the widget.
     */
    public Map<String, Object> config;
    /**
     * Constructor for WidgetInfo
     */
    public WidgetInfo() {
    }
    /**
     * Constructor for WidgetInfo
     * @param type WidgetType
     * @param rowIndex int
     * @param columnIndex int
     * @param colSpan int
     * @param rowSpan int
     * @param config Map<String, Object>
     */
    public WidgetInfo(WidgetType type, int rowIndex, int columnIndex, int colSpan, int rowSpan, Map<String, Object> config) {
        this.type = type;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.config = config;
    }
    /**
     * Constructor for WidgetInfo
     * @param type WidgetType
     * @param rowIndex int
     * @param columnIndex int
     * @param colSpan int
     * @param rowSpan int
     */
    public WidgetInfo deepCopy() {
        Map<String, Object> newConfig = new HashMap<>(config);
        return new WidgetInfo(type, rowIndex, columnIndex, colSpan, rowSpan, newConfig);
    }
}
