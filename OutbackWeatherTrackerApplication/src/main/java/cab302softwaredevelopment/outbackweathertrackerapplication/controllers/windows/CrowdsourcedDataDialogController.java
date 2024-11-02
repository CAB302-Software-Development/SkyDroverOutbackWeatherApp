package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.CrowdsourcedDataModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CrowdsourcedDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Getter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CrowdsourcedDataDialogController {
    @FXML
    private VBox configFieldsVBox;
    @FXML
    private ComboBox<Location> locationComboBox;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ButtonType saveButtonType, cancelButtonType;
    @FXML
    TextField actualTempField, feelsLikeTempField;

    @Getter
    private CrowdsourcedDTO crowdsourcedData = null;


    @FXML
    public void initialize() {
        List<Location> locations = LocationService.getInstance().getCurrentUserLocations();
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
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                crowdsourcedData.setLatitude(newValue.getLatitude());
                crowdsourcedData.setLongitude(newValue.getLongitude());
            }
        });

        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        saveButton.setOnAction(event -> {
            buildCrowdData();
            dialogPane.getScene().getWindow().hide();
        });

        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        cancelButton.setOnAction(event -> {
            crowdsourcedData = null;
            dialogPane.getScene().getWindow().hide();
        });
    }

    private void buildCrowdData() {
        Location location = locationComboBox.getSelectionModel().getSelectedItem();
        crowdsourcedData = new CrowdsourcedDTO();
        crowdsourcedData.setLocation(location.getName());
        crowdsourcedData.setLatitude(location.getLatitude());
        crowdsourcedData.setLongitude(location.getLongitude());
        if (actualTempField.getText() != null && !actualTempField.getText().isEmpty()) {
            crowdsourcedData.setActualTemp(parseInteger(actualTempField.getText()));
        }
        if (feelsLikeTempField.getText() != null && !feelsLikeTempField.getText().isEmpty()) {
            crowdsourcedData.setFeelsLikeTemp(parseInteger(feelsLikeTempField.getText()));
        }
    }

    private Integer parseInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
