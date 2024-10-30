package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import java.util.ArrayList;
import java.util.List;

public class WidgetManager {
    private final List<IConfigurableWidget> observers = new ArrayList<>();

    /**
     * Registers a widget observer to be notified during updates.
     * @param observer
     */
    public void registerWidget(IConfigurableWidget observer) {
        observers.add(observer);
    }

    /**
     * Unregisters a widget observer if it should no longer be notified.
     * @param observer
     */
    public void unregisterWidget(IConfigurableWidget observer) {
        observers.remove(observer);
    }

    /**
     * Notify all registered widgets to update themselves.
     */
    public void updateWidgets() {
        for (IConfigurableWidget observer : observers) {
            observer.updateWidget();
        }
    }

    /**
     * Clears all registered widgets.
     */
    public void unregisterAllWidgets() {
        observers.clear();
    }
}
