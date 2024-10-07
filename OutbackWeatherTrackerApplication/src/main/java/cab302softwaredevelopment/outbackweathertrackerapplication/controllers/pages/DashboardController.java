package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets.IConfigurableWidget;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.WidgetConfigDialogController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetType;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LoginState;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {
    @FXML
    public Pane pnlRoot;
    @FXML
    public GridPane dashboardGrid;
    @FXML
    public Button btnEditDashboard, btnSave, btnCancel, btnNewLayout, btnDeleteLayout;
    @FXML
    public Text txtWelcome;
    @FXML
    private HBox hbEditModeToolbar;
    @FXML
    private BorderPane bpHeader;
    @FXML
    private ComboBox<String> cboSelectedLayout;

    private boolean[][] occupiedCells;
    private List<WidgetInfo> currentLayout;
    private boolean isEditing = false;
    private boolean unsavedChanges = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Account account = LoginState.getCurrentAccount();
        txtWelcome.setText("Welcome " + account.getEmail().split("@")[0]);
        currentLayout = getCurrentLayout();
        resetLayoutComboBox();
        loadWidgetsToGrid();
    }

    private void resetLayoutComboBox() {
        cboSelectedLayout.getItems().clear();
        cboSelectedLayout.getItems().addAll(LoginState.getCurrentAccount().getDashboardLayouts().keySet());
        cboSelectedLayout.getSelectionModel().select(LoginState.getCurrentAccount().getSelectedLayout());
        cboSelectedLayout.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            changeLayout(newValue);
        });
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
        if (event.getSource() == btnEditDashboard){
            enterEditMode();
        } else if (event.getSource() == btnSave) {
            saveLayout();
        } else if (event.getSource() == btnCancel) {
            exitEditMode();
        } else if (event.getSource() == btnNewLayout) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Layout");
            dialog.setHeaderText("Enter new layout name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                if (!s.isBlank() && !s.isEmpty()) createNewLayout(s);
            });
        } else if (event.getSource() == btnDeleteLayout) {

        }
    }

    private void enterEditMode() {
        isEditing = true;
        bpHeader.setVisible(false);
        bpHeader.setManaged(false);
        hbEditModeToolbar.setVisible(true);
        hbEditModeToolbar.setManaged(true);
        resetLayoutComboBox();
        loadWidgetsToGrid();
    }

    private void exitEditMode() {
        if(checkUnsavedChanges()) return;

        isEditing = false;
        hbEditModeToolbar.setVisible(false);
        hbEditModeToolbar.setManaged(false);
        bpHeader.setVisible(true);
        bpHeader.setManaged(true);
        resetLayoutComboBox();
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
                    ((IConfigurableWidget) controller).applyConfig(new WidgetConfig(info.config));
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
            resetOccupied();
        }
    }


    private void resetOccupied() {
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
                        addWidgetAt(finalC, finalR);
                    });
                    dashboardGrid.add(addButton, c, r);
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

    public boolean checkOccupied(WidgetInfo info) {
        int row = info.rowIndex;
        int col = info.columnIndex;
        for (int r = row; r < row + info.rowSpan; r++) {
            for (int c = col; c < col + info.colSpan; c++) {
                if(occupiedCells[r][c]) return true;
            }
        }
        return false;
    }


    private void addWidgetAt(int columnIndex, int rowIndex) {
        WidgetInfo newWidgetInfo = new WidgetInfo(WidgetType.CurrentTemp, rowIndex, columnIndex, 1, 1, new HashMap<>());
        currentLayout.add(newWidgetInfo);
        unsavedChanges = true;
        setOccupied(newWidgetInfo);
        loadWidgetsToGrid();
        editWidget(currentLayout.indexOf(newWidgetInfo));
    }

    public static List<WidgetInfo> getCurrentLayout() {
        HashMap<String, WidgetInfo[]> dashboardLayouts = LoginState.getCurrentAccount().getDashboardLayouts();
        WidgetInfo[] layout = dashboardLayouts.get(LoginState.getCurrentAccount().getSelectedLayout());
        return Arrays.stream(layout).collect(Collectors.toCollection(ArrayList::new));
    }

    public void changeLayout(String newLayout) {
        if (checkUnsavedChanges()) return;

        Account account = LoginState.getCurrentAccount();
        if (account.getDashboardLayouts().containsKey(newLayout)) {
            account.setSelectedLayout(newLayout);
            currentLayout = getCurrentLayout();
        } else if (cboSelectedLayout.getItems().contains(newLayout)) {
            currentLayout = new ArrayList<>();
        }
        loadWidgetsToGrid();
    }

    public void createNewLayout(String layoutName) {
        if (!isEditing || checkUnsavedChanges()) return;

        if (LoginState.getCurrentAccount().getDashboardLayouts().containsKey(layoutName)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Layout with this name already exists");
        }

        cboSelectedLayout.getItems().add(layoutName);
        cboSelectedLayout.getSelectionModel().select(layoutName);
        changeLayout(layoutName);
    }

    private void editWidget(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/widget-config-dialog.fxml"));
            DialogPane dialogPane = loader.load();
            WidgetConfigDialogController controller = loader.getController();
            controller.setWidgetInfo(currentLayout.get(index).deepCopy(), this);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initOwner(dashboardGrid.getScene().getWindow());
            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(response -> {
                if (response.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    if (currentLayout.get(index) != controller.getWidgetInfo() && controller.getWidgetInfo() != null) {
                        currentLayout.set(index, controller.getWidgetInfo());
                        unsavedChanges = true;
                    }
                }
                if (response.getButtonData() == ButtonBar.ButtonData.OTHER) {
                    currentLayout.remove(index);
                    unsavedChanges = true;
                }
                if (response.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {

                }
            });

            loadWidgetsToGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkUnsavedChanges() {
        if (!unsavedChanges) return false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("You have unsaved changes.");
        alert.setContentText("Do you want to save your changes before proceeding?");
        ButtonType saveButton = new ButtonType("Save");
        ButtonType discardButton = new ButtonType("Discard");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == saveButton) {
                saveLayout();
                return false;
            } else if (result.get() == discardButton) {
                unsavedChanges = false;
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void saveLayout() {
        HashMap<String, WidgetInfo[]> existingLayouts = LoginState.getCurrentAccount().getDashboardLayouts();
        existingLayouts.put(cboSelectedLayout.getValue(), currentLayout.toArray(WidgetInfo[]::new));

        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setDashboardLayouts(existingLayouts);
        unsavedChanges = !LoginState.updateAccount(updateModel);
    }

    private void exportLayouts() {
        HashMap<String, WidgetInfo[]> data = LoginState.getCurrentAccount().getDashboardLayouts();
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("user_preferences.json")) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
