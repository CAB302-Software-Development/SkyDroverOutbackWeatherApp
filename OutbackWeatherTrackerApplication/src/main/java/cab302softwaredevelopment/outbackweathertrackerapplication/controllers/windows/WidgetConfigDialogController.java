package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.net.URL;
import java.util.*;

public class WidgetConfigDialogController implements Initializable {

    @FXML
    private ComboBox<WidgetType> widgetTypeComboBox;

    @FXML
    private VBox configFieldsVBox;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private ButtonType saveButtonType;

    @FXML
    private ButtonType deleteButtonType;

    @Getter
    private WidgetInfo widgetInfo;

    private Map<String, Control> configControls = new HashMap<>();

    public void setWidgetInfo(WidgetInfo widgetInfo) {
        this.widgetInfo = widgetInfo;
        initializeDialog();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize widget type combo box
        widgetTypeComboBox.setItems(FXCollections.observableArrayList(WidgetType.values()));
        widgetTypeComboBox.setOnAction(event -> updateConfigFields());

        // Handle Save and Delete button actions
        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        saveButton.setOnAction(event -> onSave());

        Button deleteButton = (Button) dialogPane.lookupButton(deleteButtonType);
        deleteButton.setOnAction(event -> onDelete());
    }

    private void initializeDialog() {
        widgetTypeComboBox.getSelectionModel().select(widgetInfo.type);
        widgetTypeComboBox.setDisable(true);
        updateConfigFields();
    }

    private void updateConfigFields() {
        configFieldsVBox.getChildren().clear();
        configControls.clear();

        WidgetType selectedType = widgetTypeComboBox.getValue();
        if (selectedType == null) {
            return;
        }

        // Dynamically create configuration fields based on widget type
        // For example, if it's CURRENT_TEMPERATURE, add fields for locationId and unit
        if (selectedType == WidgetType.CurrentTemp) {
            addConfigField("locationId", "Location ID", "Integer");
            addConfigField("unit", "Unit (Celsius/Fahrenheit)", "String");
        }
        // Add other widget types and their configuration fields here

        // Pre-fill fields if editing an existing widget
        if (widgetInfo != null && widgetInfo.config != null) {
            for (String key : widgetInfo.config.keySet()) {
                Control control = configControls.get(key);
                if (control instanceof TextField) {
                    ((TextField) control).setText(widgetInfo.config.get(key).toString());
                }
            }
        }
    }

    private void addConfigField(String key, String labelText, String dataType) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        configControls.put(key, textField);
        configFieldsVBox.getChildren().addAll(label, textField);
    }

    private void onSave() {
        // Validate and collect configuration values
        Map<String, Object> newConfig = new HashMap<>();
        for (String key : configControls.keySet()) {
            Control control = configControls.get(key);
            if (control instanceof TextField) {
                String text = ((TextField) control).getText();
                // Perform validation and type conversion
                Object value = convertValue(text, key);
                if (value == null) {
                    showError("Invalid value for " + key);
                    return;
                }
                newConfig.put(key, value);
            }
        }
        // Update widgetInfo with new configuration
        if (widgetInfo == null) {
            widgetInfo = new WidgetInfo();
        }
        widgetInfo.type = widgetTypeComboBox.getValue();
        widgetInfo.config = newConfig;
        // Close the dialog
        dialogPane.getScene().getWindow().hide();
    }

    private void onDelete() {
        // Set widgetInfo to null to indicate deletion
        widgetInfo = null;
        dialogPane.getScene().getWindow().hide();
    }

    private Object convertValue(String text, String key) {
        // Implement type conversion based on key or expected data type
        try {
            if (key.equals("locationId")) {
                return Integer.parseInt(text);
            } else if (key.equals("unit")) {
                return text;
            }
            // Add conversion for other keys
        } catch (NumberFormatException e) {
            return null;
        }
        return text;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogPane.getScene().getWindow());
        alert.setContentText(message);
        alert.showAndWait();
    }

}
