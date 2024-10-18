package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;

import java.util.Map;
/**
 * Interface for widgets that can be configured
 */
public interface IConfigurableWidget {
    /**
     * Get the configuration of the widget
     */
    void applyConfig(WidgetConfig config);
}
