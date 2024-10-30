package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;

public interface IConfigurableWidget {
    void applyConfig(WidgetConfig config);
    void unregister();
    void updateWidget();
}
