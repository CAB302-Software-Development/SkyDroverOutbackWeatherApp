package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.BOMWeatherAlert;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.IAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WeatherAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
    public Button btnExpandCollapse, btnAddAlert, btnDeleteAlert;
    @FXML
    public BorderPane bpContentArea;
    @FXML
    public VBox vbAlertConfig;
    @FXML
    public ListView<IAlertCondition> activeAlerts;
    @FXML
    private ComboBox<Location> cboLocations;
    @FXML
    private ComboBox<ExampleAlerts> cboExampleAlerts;

    @Getter
    enum ExampleAlerts {
        BOMAlert("Bureau Of Meteorology alerts"),
        HotWeek("Hot week ahead alert"),
        WindyWeek("Windy week ahead alert");

        private final String title;

        ExampleAlerts(String s) {
            this.title = s;
        }
    }

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

        cboExampleAlerts.setConverter(new StringConverter<>() {
            @Override
            public String toString(ExampleAlerts object) {
                if (object == null) return "";
                return object.getTitle();
            }
            @Override
            public ExampleAlerts fromString(String string) {
                return null;
            }
        });
        cboExampleAlerts.getItems().setAll(ExampleAlerts.values());

        activeAlerts.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(IAlertCondition item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? null : item.getAlertTitle());
            }
        });
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
            IAlertCondition condition = createExampleCondition(
                    cboExampleAlerts.getSelectionModel().getSelectedItem(),
                    cboLocations.getSelectionModel().getSelectedItem());
            alertsService.addAlertPreference(condition);
            updateAlertsPane();
        } else if (event.getSource() == btnDeleteAlert) {
            IAlertCondition selectedCondition = activeAlerts.getSelectionModel().getSelectedItem();
            alertsService.removeAlertCondition(selectedCondition);
            updateAlertsPane();
        }
    }

    private IAlertCondition createExampleCondition(ExampleAlerts alertType, Location location) {
        return switch (alertType) {
            case BOMAlert -> new BOMWeatherAlert(location);
            case HotWeek -> new CustomAlertCondition(
                    ExampleAlerts.HotWeek.getTitle(),
                    "",
                    new DailyForecastDAO.DailyForecastQuery()
                            .whereLocationId(location.getId())
                            .addOrderDesc("temperature_2m_max"),
                    0L,
                    604800L,
                    3,
                    "temperature_2m_max",
                    30);
            case WindyWeek -> new CustomAlertCondition(
                    ExampleAlerts.WindyWeek.getTitle(),
                    "",
                    new DailyForecastDAO.DailyForecastQuery()
                            .whereLocationId(location.getId())
                            .addOrderDesc("wind_speed_10m_max"),
                    0L,
                    604800L,
                    3,
                    "wind_speed_10m_max",
                    30);
        };
    }

    @FXML
    private void updateAlertsPane() {
        List<WeatherAlert> alerts = new ArrayList<>();
        List<IAlertCondition> conditions = alertsService.getAlertPreferences();
        conditions.forEach(c -> alerts.addAll(c.getAlerts()));

        //List<WeatherAlert> alerts = new ArrayList<>();
        //alerts.add(new WeatherAlert("20/10:19 EDT Final Flood Warning for the Kiewa River", "http://www.bom.gov.au/vic/warnings/flood/kiewariver.shtml", "Sat, 19 Oct 2024 23:19:33 GMT"));

        activeAlerts.getItems().clear();
        activeAlerts.setItems(FXCollections.observableArrayList(conditions));

        VBox vbScrollContent = new VBox();
        vbScrollContent.setSpacing(10);
        for (WeatherAlert alert : alerts) {
            Text description = new Text(alert.getTitle());
            Text date = new Text(alert.getPubDate());
            HBox topRow = new HBox(description, date);
            VBox alertContainer = new VBox(topRow);
            if (alert.getLink() != null) alertContainer.getChildren().add(new Hyperlink(alert.getLink()));
            alertContainer.getStyleClass().add("weather-alert");
            alertContainer.prefHeight(150);
            alertContainer.setPadding(new Insets(10));
            vbScrollContent.getChildren().add(alertContainer);
        }
        alertScrollPane.setContent(vbScrollContent);
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