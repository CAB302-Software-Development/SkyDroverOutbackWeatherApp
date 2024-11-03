package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.DashboardController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetType;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.LocationService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.IntField;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
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

    @FXML
    IntField ifColIndex, ifRowIndex, ifColSpan, ifRowSpan;

    @Getter
    private WidgetInfo widgetInfo;

    private WidgetConfig widgetConfig;

    private DashboardController parent;
    List<Location> locations;
    UserService userService;
    LocationService locationService;

    public void setWidgetInfo(WidgetInfo widgetInfo, DashboardController parent) {
        this.widgetInfo = widgetInfo;
        widgetConfig = new WidgetConfig(this.widgetInfo.config);
        this.widgetInfo.config = null;
        this.parent = parent;
        initializeDialog();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = UserService.getInstance();
        locationService = LocationService.getInstance();
        locations = locationService.getCurrentUserLocations();

        widgetTypeComboBox.setItems(FXCollections.observableArrayList(WidgetType.values()));
        widgetTypeComboBox.setOnAction(event -> {
            widgetConfig.clearConfig();
            updateConfigFields();
        });

        final Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        saveButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!onSave()) {
                event.consume();
            }
        });

        final Button deleteButton = (Button) dialogPane.lookupButton(deleteButtonType);
        deleteButton.setOnAction(event -> onDelete());
    }

    private void initializeDialog() {
        widgetTypeComboBox.getSelectionModel().select(widgetInfo.type);
        widgetTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            widgetInfo.type = newValue;
        });
        updateConfigFields();
    }

    private void updateConfigFields() {
        configFieldsVBox.getChildren().clear();

        WidgetType selectedType = widgetTypeComboBox.getValue();
        if (selectedType == null) {
            return;
        }

        ifColIndex = new IntField(0, 3, widgetInfo.columnIndex);
        ifRowIndex = new IntField(0, 2, widgetInfo.rowIndex);
        ifColSpan = new IntField(0, 4, widgetInfo.colSpan);
        ifRowSpan = new IntField(0, 3, widgetInfo.rowSpan);
        configFieldsVBox.getChildren().addAll(
                new Label("Column"), ifColIndex,
                new Label("Row"), ifRowIndex,
                new Label("Width"), ifColSpan,
                new Label("Height"), ifRowSpan);

        switch (selectedType) {
            default -> {
                ComboBox<Location> locationComboBox = createLocationComboBox();
                configFieldsVBox.getChildren().addAll(new Label("Select location: "), locationComboBox);
            }
        }
    }

    private ComboBox<Location> createLocationComboBox() {
        ComboBox<Location> locationComboBox = new ComboBox<>();
        locationComboBox.setItems(FXCollections.observableArrayList(locations));
        locationComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Location location) {
                if (location == null) return "No Location Selected";
                return location.getName();
            }

            @Override
            public Location fromString(String string) {
                return null;
            }
        });
        long locationId = widgetConfig.getLocationId();
        Location location = new LocationDAO.LocationQuery().whereId(locationId).getSingleResult();
        if (location!=null) {
            locationComboBox.getSelectionModel().select(location);
        }
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            widgetConfig.setLocationId(newValue.getId());
        });

        return locationComboBox;
    }

    private boolean onSave() {
        if (widgetInfo == null) {
            widgetInfo = new WidgetInfo();
            widgetInfo.type = widgetTypeComboBox.getValue();
        }

        widgetInfo.columnIndex = ifColIndex.getValue();
        widgetInfo.rowIndex = ifRowIndex.getValue();
        widgetInfo.colSpan = ifColSpan.getValue();
        widgetInfo.rowSpan = ifRowSpan.getValue();
        widgetInfo.config = widgetConfig.getConfig();

        if(parent.checkOccupied(widgetInfo)) {
            showError("Current configuration overlaps existing widgets.");
            return false;
        }

        return true;
    }

    private void onDelete() {
        widgetInfo = null;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogPane.getScene().getWindow());
        alert.setContentText(message);
        alert.showAndWait();
    }

}
