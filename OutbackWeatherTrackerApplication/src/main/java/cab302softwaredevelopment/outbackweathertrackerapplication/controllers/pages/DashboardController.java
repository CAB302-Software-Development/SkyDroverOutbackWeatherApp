package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets.IConfigurableWidget;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.WidgetConfigDialogController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetType;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class DashboardController implements Initializable {
    @FXML
    public Pane pnlRoot;
    @FXML
    public GridPane dashboardGrid;
    @FXML
    public Button btnEditDashboard;
    @FXML
    private HBox hbEditModeToolbar;
    @FXML
    private HBox hbHeader;

    private boolean[][] occupiedCells;
    private List<WidgetInfo> currentLayout;
    private boolean isEditing = false;

    public void setLayout(List<WidgetInfo> newLayout) {
        currentLayout = newLayout;
    }

    private void enterEditMode() {
        isEditing = true;
        hbHeader.setVisible(false);
        hbEditModeToolbar.setVisible(true);
        loadWidgetsToGrid();
    }

    private void exitEditMode() {
        isEditing = false;
        hbEditModeToolbar.setVisible(false);
        hbHeader.setVisible(true);
        loadWidgetsToGrid();
    }

    public void loadWidgetsToGrid() {
        dashboardGrid.getChildren().clear();
        try {
            for (WidgetInfo info : currentLayout) {
                FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(info.type.getFilepath()));
                Node widgetNode = loader.load();

                Object controller = loader.getController();
                if (controller instanceof IConfigurableWidget) {
                    ((IConfigurableWidget) controller).applyConfig(info.config);
                }

                StackPane widgetContainer = new StackPane(widgetNode);

                if (isEditing) {
                    Button editButton = new Button();
                    editButton.getStyleClass().add("edit-button");
                    editButton.setOnAction(e -> editWidget(currentLayout.indexOf(info)));

                    Button deleteButton = new Button();
                    deleteButton.getStyleClass().add("delete-button");
                    deleteButton.setOnAction(e -> dashboardGrid.getChildren().remove(widgetContainer));

                    AnchorPane overlayPane = new AnchorPane(editButton, deleteButton);
                    AnchorPane.setTopAnchor(editButton, 5.0);
                    AnchorPane.setRightAnchor(editButton, 5.0);
                    AnchorPane.setTopAnchor(deleteButton, 5.0);
                    AnchorPane.setRightAnchor(deleteButton, 35.0);

                    overlayPane.setPickOnBounds(false);

                    widgetContainer.getChildren().add(overlayPane);
                }

                GridPane.setColumnIndex(widgetContainer, info.columnIndex);
                GridPane.setRowIndex(widgetContainer, info.rowIndex);
                GridPane.setColumnSpan(widgetContainer, info.colSpan);
                GridPane.setRowSpan(widgetContainer, info.rowSpan);
                dashboardGrid.getChildren().add(widgetContainer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (isEditing) {
            int numRows = dashboardGrid.getRowCount();
            int numCols = dashboardGrid.getColumnCount();

            occupiedCells = new boolean[numRows][numCols];
            for (WidgetInfo info : currentLayout) {
                setOccupied(info);
            }

            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    if (!occupiedCells[r][c]) {
                        Button addButton = new Button("+");
                        addButton.getStyleClass().add("add-widget-button");
                        int finalC = c;
                        int finalR = r;
                        addButton.setOnAction(e -> {
                            openAddWidgetPopup(finalC, finalR);

                        });
                        dashboardGrid.add(addButton, c, r);
                    }
                }
            }
        }
    }

    private void setOccupied(WidgetInfo info) {
        int row = info.rowIndex;
        int col = info.columnIndex;
        for (int r = row; r < row + info.rowSpan; r++) {
            for (int c = col; c < col + info.colSpan; c++) {
                occupiedCells[r][c] = true;
            }
        }
    }

    private void openAddWidgetPopup(int columnIndex, int rowIndex) {
        WidgetInfo newWidgetInfo = new WidgetInfo(WidgetType.CurrentTemp, columnIndex, rowIndex, 1, 1, new HashMap<>());
        currentLayout.add(newWidgetInfo);
        setOccupied(newWidgetInfo);
        loadWidgetsToGrid();
        editWidget(currentLayout.indexOf(newWidgetInfo));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLayout(Arrays.stream(getCurrentLayout()).toList());
        loadWidgetsToGrid();
    }

    public static WidgetInfo[] getCurrentLayout() {
        HashMap<String, WidgetInfo[]> dashboardLayouts = LoginState.getCurrentAccount().getDashboardLayouts();
        return dashboardLayouts.get(LoginState.getCurrentAccount().getSelectedLayout());
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
        if (event.getSource() == btnEditDashboard){
            enterEditMode();
        }
    }

    private void editWidget(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("windows/widget-config-dialog.fxml"));
            DialogPane dialogPane = loader.load();
            WidgetConfigDialogController controller = loader.getController();
            controller.setWidgetInfo(currentLayout.get(index));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initOwner(dashboardGrid.getScene().getWindow());
            Optional<ButtonType> result = dialog.showAndWait();

            if (controller.getWidgetInfo() == null) {
                currentLayout.remove(index);
            } else {
                currentLayout.set(index, controller.getWidgetInfo());
            }
            loadWidgetsToGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLayout() {
    }

}
