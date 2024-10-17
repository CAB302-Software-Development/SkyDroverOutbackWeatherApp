package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.DashboardController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import lombok.Getter;

import java.io.IOException;

public class WidgetFactory {
    @Getter
    private static final WidgetManager widgetManager = new WidgetManager();

    /**
     * Creates a widget Node based on the provided WidgetType and WidgetInfo.
     *
     * @param widgetInfo the information used to configure the widget (type, layout, config).
     * @return the configured Node (widget), or null if an error occurs.
     */
    public StackPane createWidget(WidgetInfo widgetInfo) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(widgetInfo.type.getFilepath()));
            Node widgetNode = loader.load();
            Object controller = loader.getController();
            if (controller instanceof IConfigurableWidget configurableWidget) {
                WidgetConfig config = new WidgetConfig(widgetInfo.config);
                configurableWidget.applyConfig(config);
                widgetManager.registerWidget(configurableWidget);
            }
            StackPane widgetContainer = new StackPane(widgetNode);
            GridPane.setColumnIndex(widgetContainer, widgetInfo.columnIndex);
            GridPane.setRowIndex(widgetContainer, widgetInfo.rowIndex);
            GridPane.setColumnSpan(widgetContainer, widgetInfo.colSpan);
            GridPane.setRowSpan(widgetContainer, widgetInfo.rowSpan);
            return widgetContainer;
        } catch (IOException e) {
            System.err.println("Error loading widget: " + widgetInfo.type);
            e.printStackTrace();
            return null;
        }
    }

    public StackPane createEditWidget(WidgetInfo widgetInfo, DashboardController dashboardController) {
        StackPane widgetContainer = createWidget(widgetInfo);
        Button editButton = new Button();
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(e -> dashboardController.editWidget(widgetInfo));

        Button deleteButton = new Button();
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> dashboardController.removeFromGrid(widgetContainer));

        AnchorPane overlayPane = new AnchorPane(editButton, deleteButton);
        AnchorPane.setTopAnchor(editButton, 5.0);
        AnchorPane.setRightAnchor(editButton, 5.0);
        AnchorPane.setTopAnchor(deleteButton, 5.0);
        AnchorPane.setRightAnchor(deleteButton, 35.0);
        overlayPane.setPickOnBounds(false);

        widgetContainer.getChildren().add(overlayPane);
        return widgetContainer;
    }

    public void clearAllWidgets() {
        widgetManager.unregisterAllWidgets();
    }

    /**
     * Manually trigger an update for all registered widgets.
     */
    public void updateAllWidgets() {
        widgetManager.updateWidgets();
    }
}
