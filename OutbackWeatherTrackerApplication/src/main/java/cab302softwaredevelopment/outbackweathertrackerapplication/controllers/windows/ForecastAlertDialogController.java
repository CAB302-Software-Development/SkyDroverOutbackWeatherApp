package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.DailyForecastField;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.HourlyForecastField;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
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
    private ComboBox<Location> locationComboBox;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ButtonType saveButtonType;
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
        LocationService locationService = LocationService.getInstance();
        List<Location> locations = locationService.getCurrentUserLocations();
        locationComboBox.setItems(FXCollections.observableArrayList(locations));
        locationComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Location location) {
                return (location == null) ? "Select a Location" : location.getName();
            }

            @Override
            public Location fromString(String string) {
                return null;
            }
        });

        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        saveButton.setOnAction(event -> {
            if(buildForecastQuery()) {
                dialogPane.getScene().getWindow().hide();
            }
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

        HBox filterBox = new HBox(10, fieldComboBox, comparisonComboBox, valueTextField);
        configFieldsVBox.getChildren().add(filterBox);

        filterInputs.add(new FilterInput(fieldComboBox, comparisonComboBox, valueTextField));
    }

    private boolean buildForecastQuery() {
        Location location = locationComboBox.getSelectionModel().getSelectedItem();
        if (location == null) return false;
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
