package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.*;

public class DashboardController implements ISwapPanel {
    @FXML
    public Pane pnlRoot;
    @FXML
    public GridPane dashboardGrid;

    public void initialize(Pane parent) {
        pnlRoot.prefHeightProperty().bind(parent.heightProperty());
        pnlRoot.prefWidthProperty().bind(parent.widthProperty());

        dashboardGrid.setGridLinesVisible(true);

        updateAppearance();
    }

    public void updateAppearance() {
        dashboardGrid.getChildren().clear();
        try {
            for (WidgetInfo info : PreferencesService.getCurrentLayout()) {
                FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(info.type.getFilepath()));
                Node widgetNode = loader.load();
                GridPane.setColumnIndex(widgetNode, info.columnIndex);
                GridPane.setRowIndex(widgetNode, info.rowIndex);
                GridPane.setColumnSpan(widgetNode, info.colSpan);
                GridPane.setRowSpan(widgetNode, info.rowSpan);
                dashboardGrid.getChildren().add(widgetNode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
