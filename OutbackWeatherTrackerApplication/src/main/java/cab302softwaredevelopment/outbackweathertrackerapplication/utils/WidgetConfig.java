package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class WidgetConfig {
    @Getter
    Map<String, Object> config;

    public WidgetConfig() {
        config = new HashMap<>();
    }

    public WidgetConfig(Map<String, Object> config) {
        this.config = config;
    }

    public void clearConfig() {
        config.clear();
    }

    public long getLocationId() {
        if (config == null) return -1;
        if (!config.containsKey("locationId")) return -2;
        Object locationId1 = config.get("locationId");
        return switch (locationId1) {
            case null -> -3;
            case Long l -> l;
            case Double v -> v.longValue();
            default -> -4;
        };
    }


    public void setLocationId(long locationId) {
        config.put("locationId", locationId);
    }
}
