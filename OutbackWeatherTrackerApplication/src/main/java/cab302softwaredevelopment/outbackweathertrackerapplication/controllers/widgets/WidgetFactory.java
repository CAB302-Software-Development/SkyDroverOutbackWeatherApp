package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class WidgetFactory {

    /**
     * Creates a widget Node based on the provided WidgetType and WidgetInfo.
     *
     * @param widgetInfo the information used to configure the widget (type, layout, config).
     * @return the configured Node (widget), or null if an error occurs.
     */
    public static Node createWidget(WidgetInfo widgetInfo) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(widgetInfo.type.getFilepath()));
            Node widgetNode = loader.load();
            Object controller = loader.getController();
            if (controller instanceof IConfigurableWidget) {
                IConfigurableWidget configurableWidget = (IConfigurableWidget) controller;
                WidgetConfig config = new WidgetConfig(widgetInfo.config);
                configurableWidget.applyConfig(config);
            }

            return widgetNode;
        } catch (IOException e) {
            System.err.println("Error loading widget: " + widgetInfo.type);
            e.printStackTrace();
            return null;
        }
    }
}
