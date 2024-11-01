package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.IAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WeatherAlert;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.InputService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AlertsController extends BasePage {
    @FXML
    public ScrollPane alertScrollPane;
    @FXML
    public Button btnExpandCollapse, btnAddAlert, btnDeleteAlert, btnEnable, btnDisable;
    @FXML
    public BorderPane bpContentArea;
    @FXML
    public VBox vbAlertConfig;
    @FXML
    public TableView<CustomAlertCondition> activeAlerts;
    @FXML
    public TableColumn<CustomAlertCondition, String> colTitle, colEnabled;
    @FXML
    private ComboBox<Location> cboLocations;

    private boolean isExpanded = false;

    @Override
    public void updateData() {
        loadUserLocations();
        updateAlertsPane();
    }

    @FXML
    public void initialize() {
        super.initialize();
        bpContentArea.getChildren().remove(vbAlertConfig);

        cboLocations.setConverter(new StringConverter<>() {
            @Override
            public String toString(Location location) {
                return location.getName();
            }
            @Override
            public Location fromString(String string) {
                return null;
            }
        });

        colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlertTitle()));
        colEnabled.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isEnabled() ? "Enabled" : "Disabled"));
        activeAlerts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void handleButtonPress(ActionEvent event) {
        if (event.getSource() == btnExpandCollapse) {
            if (isExpanded) {
                bpContentArea.getChildren().remove(vbAlertConfig);
                btnExpandCollapse.setText("Expand");
                isExpanded = false;
            } else {
                bpContentArea.setRight(vbAlertConfig);
                btnExpandCollapse.setText("Collapse");
                isExpanded = true;
            }
        } else if (event.getSource() == btnAddAlert) {
            CustomAlertCondition condition = InputService.getDailyForecastAlert();
            alertsService.addAlert(condition);
            updateAlertsPane();
        } else if (event.getSource() == btnDeleteAlert) {
            CustomAlertCondition selectedCondition = activeAlerts.getSelectionModel().getSelectedItem();
            alertsService.removeAlert(selectedCondition);
            updateAlertsPane();
        } else if (event.getSource() == btnEnable) {
            activeAlerts.getSelectionModel().getSelectedItem().setEnabled(true);
        } else if (event.getSource() == btnDisable) {
            activeAlerts.getSelectionModel().getSelectedItem().setEnabled(false);
        }
    }

    private void createExampleConditions() {
    }

    @FXML
    private void updateAlertsPane() {
        List<WeatherAlert> alerts = new ArrayList<>();
        List<CustomAlertCondition> conditions = alertsService.getAlertConfigs();
        Location location = cboLocations.getSelectionModel().getSelectedItem();
        conditions.forEach(c -> c.getAlert(location).ifPresent(alerts::add));

        activeAlerts.getItems().clear();
        activeAlerts.setItems(FXCollections.observableArrayList(conditions));

        VBox vbScrollContent = new VBox();
        vbScrollContent.setSpacing(10);
        alerts.forEach(a -> vbScrollContent.getChildren().add(createAlertComponent(a)));

        alertScrollPane.setContent(vbScrollContent);
    }

    private VBox createAlertComponent(WeatherAlert alert) {
        Text title = new Text(alert.getTitle());
        Text message = new Text(alert.getMessage());

        VBox alertContainer = new VBox(title, message);
        alertContainer.getStyleClass().add("weather-alert");
        alertContainer.setPadding(new Insets(10));

        int initialDisplayLimit = 2;
        List<String> alertData = alert.getData();
        VBox dataListContainer = new VBox();
        alertData.stream().limit(initialDisplayLimit).forEach(data -> dataListContainer.getChildren().add(new Text(data)));

        Button expandCollapseButton = new Button("Show More");
        expandCollapseButton.setOnAction(event -> {
            if (dataListContainer.getChildren().size() > initialDisplayLimit) {
                dataListContainer.getChildren().setAll(alertData.stream().limit(initialDisplayLimit).map(Text::new).toList());
                expandCollapseButton.setText("Show More");
            } else {
                dataListContainer.getChildren().setAll(alertData.stream().map(Text::new).toList());
                expandCollapseButton.setText("Show Less");
            }
        });

        alertContainer.getChildren().addAll(dataListContainer, expandCollapseButton);
        return alertContainer;
    }


    private void loadUserLocations() {
        List<Location> locations = locationService.getCurrentUserLocations();
        cboLocations.getItems().setAll(locations);

        if (!cboLocations.getItems().isEmpty()) {
            cboLocations.getSelectionModel().selectFirst();
            if (cboLocations.getItems().size() == 1) {
                cboLocations.disableProperty().set(true);
            } else {
                cboLocations.disableProperty().set(false);
            }
        }
    }
}