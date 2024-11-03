package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class WidgetInfo implements Serializable {
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

    /**
     * Creates a deep copy of the current WidgetInfo object, including a new copy of the config map.
     * Useful for creating independent instances of widgets with identical properties.
     *
     * @return A new WidgetInfo object with the same properties as the current instance.
     */
    public WidgetInfo deepCopy() {
        Map<String, Object> newConfig = new HashMap<>(config);
        return new WidgetInfo(type, rowIndex, columnIndex, colSpan, rowSpan, newConfig);
    }
}
