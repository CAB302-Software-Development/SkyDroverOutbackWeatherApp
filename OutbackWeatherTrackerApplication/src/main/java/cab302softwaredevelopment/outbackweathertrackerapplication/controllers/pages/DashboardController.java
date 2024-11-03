package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets.WidgetFactory;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.WidgetConfigDialogController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetType;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.InputService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserService;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.io.*;
import java.util.*;

/**
 * Controller class for managing the application's dashboard.
 * Provides functionality for displaying, editing, saving, and managing dashboard layouts and widgets.
 */
public class DashboardController extends BasePage {
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

    private WidgetFactory widgetFactory;
    private UserService userService;


    @Override
    public void initialize() {
        super.initialize();
        userService = UserService.getInstance();
        widgetFactory = new WidgetFactory();
        txtWelcome.setText("Welcome " + userService.getCurrentAccount().getUsername());
        currentLayout = userService.getCurrentLayout();
        resetLayoutComboBox();
        loadWidgetsToGrid();
    }

    /**
     * Resets the layout ComboBox, populating it with available layouts and setting up a listener
     * to handle layout changes.
     */
    private void resetLayoutComboBox() {
        cboSelectedLayout.getItems().clear();
        cboSelectedLayout.getItems().addAll(userService.getCurrentAccount().getDashboardLayouts().keySet());
        cboSelectedLayout.getSelectionModel().select(userService.getCurrentAccount().getSelectedLayout());
        cboSelectedLayout.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            changeLayout(newValue);
        });
    }

    /**
     * Handles button clicks for editing, saving, canceling, creating, and deleting layouts.
     *
     * @param event The ActionEvent triggered by button clicks.
     */
    @FXML
    public void handleButtonClick(ActionEvent event) {
        if (event.getSource() == btnEditDashboard){
            enterEditMode();
        } else if (event.getSource() == btnSave) {
            saveCurrentLayout();
            exitEditMode();
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
            String selectedLayout = cboSelectedLayout.getValue();
            if (selectedLayout == null) {
                InputService.showAlert("Error", "No layout selected.");
                return;
            }

            boolean confirmed = InputService.getConfirmation(
                    "Delete Layout",
                    "Are you sure you want to delete this layout?",
                    "This action cannot be undone."
            );

            if (confirmed) {
                Account account = userService.getCurrentAccount();
                account.getDashboardLayouts().remove(selectedLayout);
                resetLayoutComboBox();
                String newLayout = cboSelectedLayout.getItems().isEmpty() ? null : cboSelectedLayout.getItems().getFirst();
                changeLayout(newLayout);
                InputService.showAlert("Success", "Layout deleted successfully.");
            }
        }
    }

    /**
     * Enters edit mode, making the edit toolbar visible and setting up the grid for editing.
     */
    private void enterEditMode() {
        isEditing = true;
        bpHeader.setVisible(false);
        bpHeader.setManaged(false);
        hbEditModeToolbar.setVisible(true);
        hbEditModeToolbar.setManaged(true);
        resetLayoutComboBox();
        loadWidgetsToGrid();
    }

    /**
     * Exits edit mode, discarding or saving changes as needed and resetting the UI.
     */
    private void exitEditMode() {
        if(checkUnsavedChanges()) return;
        currentLayout = userService.getCurrentLayout();
        isEditing = false;
        hbEditModeToolbar.setVisible(false);
        hbEditModeToolbar.setManaged(false);
        bpHeader.setVisible(true);
        bpHeader.setManaged(true);
        resetLayoutComboBox();
        loadWidgetsToGrid();
        MainController.getController().updateUIData();
    }

    /**
     * Clears then loads widgets to the grid based on the current layout, setting up widget containers.
     */
    public void loadWidgetsToGrid() {
        dashboardGrid.getChildren().clear();
        widgetFactory.clearAllWidgets();
        try {
            for (WidgetInfo info : currentLayout) {
                StackPane widgetContainer;
                if (isEditing) {
                    widgetContainer = widgetFactory.createEditWidget(info, this);
                } else {
                    widgetContainer = widgetFactory.createWidget(info);
                }
                dashboardGrid.getChildren().add(widgetContainer);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading widgets to grid", e);
        }

        if (isEditing) {
            resetOccupied();
        }
    }

    /**
     * Resets the occupied cells array, setting cells as occupied based on the widgets in the current layout.
     */
    private void resetOccupied() {
        int numRows = dashboardGrid.getRowCount();
        int numCols = dashboardGrid.getColumnCount();

        occupiedCells = new boolean[numRows][numCols];
        for (WidgetInfo info : currentLayout) {
            setOccupied(info, true);
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

    /**
     * Sets cells as occupied or unoccupied based on a widget's dimensions and position.
     *
     * @param info The WidgetInfo object containing layout data for the widget.
     * @param occupied Boolean indicating whether cells should be marked as occupied or free.
     */
    private void setOccupied(WidgetInfo info, boolean occupied) {
        int row = info.rowIndex;
        int col = info.columnIndex;
        for (int r = row; r < row + info.rowSpan; r++) {
            for (int c = col; c < col + info.colSpan; c++) {
                occupiedCells[r][c] = occupied;
            }
        }
    }

    /**
     * Checks if the specified widget's cells are already occupied by another widget.
     *
     * @param info The WidgetInfo object containing layout data for the widget.
     * @return True if any cells are occupied; false otherwise.
     */
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

    /**
     * Adds a new widget to the grid at the specified column and row indices.
     *
     * @param columnIndex The column index in the grid.
     * @param rowIndex The row index in the grid.
     */
    private void addWidgetAt(int columnIndex, int rowIndex) {
        WidgetInfo newWidgetInfo = new WidgetInfo(WidgetType.CurrentTemp, rowIndex, columnIndex, 1, 1, new HashMap<>());
        currentLayout.add(newWidgetInfo);
        unsavedChanges = true;
        setOccupied(newWidgetInfo, true);
        loadWidgetsToGrid();
        editWidget(currentLayout.indexOf(newWidgetInfo));
    }

    /**
     * Changes the current layout to the selected layout in the ComboBox.
     *
     * @param newLayout The name of the layout to switch to.
     */
    public void changeLayout(String newLayout) {
        if (checkUnsavedChanges()) return;

        Account account = userService.getCurrentAccount();
        if (account.getDashboardLayouts().containsKey(newLayout)) {
            account.setSelectedLayout(newLayout);
            currentLayout = userService.getCurrentLayout();
        } else if (cboSelectedLayout.getItems().contains(newLayout)) {
            currentLayout = new ArrayList<>();
        }
        loadWidgetsToGrid();
    }

    /**
     * Creates a new layout with the specified name and adds it to the available layouts.
     *
     * @param layoutName The name of the new layout.
     */
    public void createNewLayout(String layoutName) {
        if (!isEditing || checkUnsavedChanges()) return;

        if (userService.getCurrentAccount().getDashboardLayouts().containsKey(layoutName)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Layout with this name already exists");
        }

        cboSelectedLayout.getItems().add(layoutName);
        cboSelectedLayout.getSelectionModel().select(layoutName);
        changeLayout(layoutName);
    }

    /**
     * Opens a widget configuration dialog to edit the specified widget.
     *
     * @param widgetInfo The WidgetInfo object for the widget to edit.
     */
    public void editWidget(WidgetInfo widgetInfo) {
        int index = currentLayout.indexOf(widgetInfo);
        if (index != -1) editWidget(index);
    }

    /**
     * Opens a widget configuration dialog to edit the widget at the specified index.
     *
     * @param index The index of the widget in the current layout.
     */
    private void editWidget(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/widget-config-dialog.fxml"));
            DialogPane dialogPane = loader.load();
            WidgetConfigDialogController controller = loader.getController();
            controller.setWidgetInfo(currentLayout.get(index).deepCopy(), this);

            setOccupied(currentLayout.get(index), false);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initOwner(dashboardGrid.getScene().getWindow());
            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(response -> {
                if (response.getButtonData() == ButtonBar.ButtonData.OTHER) {
                    currentLayout.remove(index);
                    unsavedChanges = true;
                } else if (response.getButtonData() != ButtonBar.ButtonData.CANCEL_CLOSE) {
                    if (currentLayout.get(index) != controller.getWidgetInfo() && controller.getWidgetInfo() != null) {
                        currentLayout.set(index, controller.getWidgetInfo());
                        unsavedChanges = true;
                    }
                }
            });

            loadWidgetsToGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks for unsaved changes and prompts the user to save or discard them.
     *
     * @return True if there are unsaved changes and the user cancels; false otherwise.
     */
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
                saveCurrentLayout();
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

    /**
     * Saves the current layout to the user's account, marking changes as saved.
     */
    private void saveCurrentLayout() {
        String layoutName = cboSelectedLayout.getValue();
        boolean result = userService.saveLayout(layoutName, currentLayout.toArray(WidgetInfo[]::new));
        unsavedChanges = !result;
        if (result) {
            UserService.getInstance().getCurrentAccount().setSelectedLayout(layoutName);
            InputService.showAlert("Success", "Dashboard has been saved.");
        } else {
            InputService.showAlert("Error", "There was an error saving the dashboard.");
        }
    }

    /**
     * Exports all layouts for the current user to a JSON file.
     */
    private void exportLayouts() {
        HashMap<String, WidgetInfo[]> data = userService.getCurrentAccount().getDashboardLayouts();
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("user_preferences.json")) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the data for all widgets in the dashboard.
     */
    @Override
    public void updateData() {
        widgetFactory.updateAllWidgets();
    }

    /**
     * Deletes a widget from the dashboard, removing it from the layout and the UI grid.
     *
     * @param widgetInfo The WidgetInfo object for the widget to delete.
     * @param widgetContainer The container holding the widget in the UI grid.
     */
    public void deleteWidget(WidgetInfo widgetInfo, StackPane widgetContainer) {
        unsavedChanges = true;
        dashboardGrid.getChildren().remove(widgetContainer);
        currentLayout.remove(widgetInfo);
        loadWidgetsToGrid();
    }
}
