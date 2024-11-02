package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DailyForecastField;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.HourlyForecastField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ForecastAlertDialogController {
    @FXML
    private VBox configFieldsVBox;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ButtonType saveButtonType, closeButtonType;
    @FXML
    private Button btnAddFilter;
    @FXML
    private TextField txtTitle, txtMessage;

    @Setter
    private boolean forecastType = true;
    @Getter
    private CustomAlertCondition forecastAlert;

    private final List<FilterInput> filterInputs = new ArrayList<>();
    private record FilterInput(ComboBox<Enum<?>> fieldComboBox, ComboBox<String> comparisonComboBox, TextField valueTextField) { }

    @FXML
    public void initialize() {
        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        saveButton.setOnAction(event -> {
            if(buildForecastQuery()) {
                dialogPane.getScene().getWindow().hide();
            }
        });

        Button closeButton = (Button) dialogPane.lookupButton(closeButtonType);
        closeButton.setOnAction(event -> {
            forecastAlert = null;
            dialogPane.getScene().getWindow().hide();
        });

        btnAddFilter.setOnAction(event -> addFilterInput());
    }

    private void addFilterInput() {
        ComboBox<Enum<?>> fieldComboBox = new ComboBox<>();
        if (forecastType) {
            fieldComboBox.setItems(FXCollections.observableArrayList(DailyForecastField.values()));
            fieldComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Enum<?> field) {
                    return ((DailyForecastField) field).getHumanReadable();
                }
                @Override
                public Enum<?> fromString(String string) {
                    return null;
                }
            });
        } else {
            fieldComboBox.setItems(FXCollections.observableArrayList(HourlyForecastField.values()));
            fieldComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Enum<?> field) {
                    return ((HourlyForecastField) field).getHumanReadable();
                }
                @Override
                public Enum<?> fromString(String string) {
                    return null;
                }
            });
        }
        fieldComboBox.setPromptText("Select Field");

        ComboBox<String> comparisonComboBox = new ComboBox<>(FXCollections.observableArrayList("Greater than", "Less than"));
        comparisonComboBox.setPromptText("Select Comparison");

        TextField valueTextField = new TextField();
        valueTextField.setPromptText("Enter Value");

        Button removeButton = new Button("Remove");
        HBox filterBox = new HBox(10, fieldComboBox, comparisonComboBox, valueTextField, removeButton);

        removeButton.setOnAction(event -> removeFilterInput(filterBox));

        configFieldsVBox.getChildren().add(filterBox);
        dialogPane.getScene().getWindow().sizeToScene();
        filterInputs.add(new FilterInput(fieldComboBox, comparisonComboBox, valueTextField));
    }

    private void removeFilterInput(HBox filterBox) {
        configFieldsVBox.getChildren().remove(filterBox);
        dialogPane.getScene().getWindow().sizeToScene();
        filterInputs.removeIf(filterInput ->
                filterInput.fieldComboBox().equals(((ComboBox<?>) filterBox.getChildren().get(0))));
    }

    private boolean buildForecastQuery() {
        forecastAlert = new CustomAlertCondition(forecastType);
        forecastAlert.setAlertTitle(txtTitle.getText());
        forecastAlert.setMessage(txtMessage.getText());

        for (FilterInput filterInput : filterInputs) {
            if (filterInput.fieldComboBox().getValue() == null) return false;
            String selectedField = forecastType ?
                    ((DailyForecastField) filterInput.fieldComboBox().getValue()).getFieldName() :
                    ((HourlyForecastField) filterInput.fieldComboBox().getValue()).getFieldName();
            String comparisonType = filterInput.comparisonComboBox().getValue();
            String value = filterInput.valueTextField().getText();

            if (selectedField != null && comparisonType != null && !value.isEmpty()) {
                boolean isGE = comparisonType.equals("Greater than");
                forecastAlert.addAlertCondition(selectedField, isGE, value);
            } else {
                return false;
            }
        }
        return true;
    }
}
