package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;

import java.util.Map;

public interface IConfigurableWidget {
    void applyConfig(WidgetConfig config);
    void updateWidget();
}
