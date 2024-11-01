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
    private ButtonType saveButtonType;

    @Getter
    private CrowdsourcedDTO crowdsourcedData;

    private LocationService locationService;
    private List<Location> locations;

    @FXML
    public void initialize() {
        locationService = LocationService.getInstance();
        locations = locationService.getCurrentUserLocations();
        initializeLocationComboBox();
        populateFields();
    }

    private void initializeLocationComboBox() {
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
    }

    private void populateFields() {
        configFieldsVBox.getChildren().clear();
        crowdsourcedData = new CrowdsourcedDTO();

        TextField locationField = new TextField();
        locationField.setPromptText("Enter location name");
        configFieldsVBox.getChildren().add(new Label("Location"));
        configFieldsVBox.getChildren().add(locationField);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your name");
        configFieldsVBox.getChildren().add(new Label("User Name"));
        configFieldsVBox.getChildren().add(usernameField);

        TextField actualTempField = new TextField();
        actualTempField.setPromptText("Enter actual temperature");
        configFieldsVBox.getChildren().add(new Label("Actual Temperature"));
        configFieldsVBox.getChildren().add(actualTempField);

        TextField feelsLikeTempField = new TextField();
        feelsLikeTempField.setPromptText("Enter feels-like temperature");
        configFieldsVBox.getChildren().add(new Label("Feels Like Temperature"));
        configFieldsVBox.getChildren().add(feelsLikeTempField);

        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        saveButton.setOnAction(event -> {
            crowdsourcedData.setLocation(locationField.getText());
            crowdsourcedData.setUsername(usernameField.getText());
            crowdsourcedData.setActualTemp(parseInteger(actualTempField.getText()));
            crowdsourcedData.setFeelsLikeTemp(parseInteger(feelsLikeTempField.getText()));
            dialogPane.getScene().getWindow().hide();
        });
    }

    private Integer parseInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
