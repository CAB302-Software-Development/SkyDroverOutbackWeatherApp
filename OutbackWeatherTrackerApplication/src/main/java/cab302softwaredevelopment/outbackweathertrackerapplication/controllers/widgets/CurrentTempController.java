package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.DashboardController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class CurrentTempController extends BaseWidgetController implements Initializable {

    private Map<String, Object> config;
    @FXML
    public Label lblTemp;

    @FXML
    public VBox root;

    @FXML
    LineChart<Number,Number> lineChart;

    @FXML
    private Label temperatureLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Button configButton;
    @FXML
    private Button removeButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (config != null) {
            loadTemperatureData();
        }

        // this.config = config;
        // int locationId = (int) config.get("locationId");
        // int locationId = 1;
        // DailyForecastDAO temp = new DailyForecastDAO();
        // double temperature = temp.getByLocationId(locationId).get(0).getTemperature_2m_max();
        // lblTemp.setText(String.format("%.1f° %s", temperature, "c"));
    }

    @Override
    public void applyConfig(Map<String, Object> config) {
        this.config = config;
        if (temperatureLabel != null) {
            loadTemperatureData();
        }
    }

    private void loadTemperatureData() {
        int locationId = config.containsKey("locationId") ? (int) config.get("locationId") : -1;
        String unit = config.containsKey("unit") ? (String) config.get("unit") : "Celsius";

        if (locationId == -1) {
            temperatureLabel.setText("No location set.");
            return;
        }

        LocationDAO locationDAO = new LocationDAO();
        Location location = locationDAO.getById(locationId);
        if (location == null) {
            temperatureLabel.setText("Invalid location.");
            return;
        }

        locationLabel.setText(location.getName());

        // Fetch temperature data
        HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
        HourlyForecast forecast = hourlyForecastDAO.getByLocation(location).get(0);
        if (forecast == null) {
            temperatureLabel.setText("No data available.");
            return;
        }

        double temperature = forecast.getTemperature_2m();
        if (unit.equals("Fahrenheit")) {
            temperature = temperature * 9 / 5 + 32;
        }

        temperatureLabel.setText(String.format("%.1f° %s", temperature, unit));
    }

    @FXML
    private void openConfigDialog() {
        Dialog<Map<String, Object>> dialog = new Dialog<>();
        dialog.setTitle("Configure Widget");

        // Set up dialog UI components
        TextField locationIdField = new TextField();
        locationIdField.setPromptText("Location ID");
        if (config != null && config.containsKey("locationId")) {
            locationIdField.setText(String.valueOf(config.get("locationId")));
        }

        ChoiceBox<String> unitChoiceBox = new ChoiceBox<>();
        unitChoiceBox.getItems().addAll("Celsius", "Fahrenheit");
        if (config != null && config.containsKey("unit")) {
            unitChoiceBox.setValue((String) config.get("unit"));
        } else {
            unitChoiceBox.setValue("Celsius");
        }

        VBox content = new VBox(new Label("Location ID:"), locationIdField, new Label("Unit:"), unitChoiceBox);
        dialog.getDialogPane().setContent(content);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Map<String, Object> newConfig = new HashMap<>();
                try {
                    newConfig.put("locationId", Integer.parseInt(locationIdField.getText()));
                } catch (NumberFormatException e) {
                    showAlert("Invalid Location ID", "Please enter a valid integer for Location ID.");
                    return null;
                }
                newConfig.put("unit", unitChoiceBox.getValue());
                return newConfig;
            }
            return null;
        });

        Optional<Map<String, Object>> result = dialog.showAndWait();
        result.ifPresent(newConfig -> {
            this.config = newConfig;
            saveConfig();
            loadTemperatureData();
        });
    }

    private void saveConfig() {
        // Find the widget in preferences and update its config
        WidgetInfo[] widgets = PreferencesService.getCurrentLayout();
        for (WidgetInfo widgetInfo : widgets) {
            if (widgetInfo.widgetId.equals(this.widgetId)) {
                widgetInfo.config = this.config;
                break;
            }
        }
        // Save the updated preferences
        PreferencesService.savePreferences();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(configButton.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
