package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    public Pane pnlRoot;
    @FXML
    public GridPane dashboardGrid;

    /**
     * Clears old dashboard layout and loads the currently selected layout in the PreferencesService
     */
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateAppearance();
    }
}
