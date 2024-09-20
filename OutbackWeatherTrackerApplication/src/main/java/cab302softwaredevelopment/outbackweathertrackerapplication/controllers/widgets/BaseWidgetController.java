package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

public abstract class BaseWidgetController implements IConfigurableWidget {
    protected String widgetId;

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }
}
