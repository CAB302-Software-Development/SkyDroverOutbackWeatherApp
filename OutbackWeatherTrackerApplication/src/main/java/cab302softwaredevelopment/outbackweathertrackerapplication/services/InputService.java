package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.DashboardController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.CrowdsourcedDataDialogController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.ForecastAlertDialogController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.LocationSelectorController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.WidgetConfigDialogController;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CrowdsourcedDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

/**
 * Utility service for managing user input dialogs and confirmations throughout the application.
 * Provides methods to prompt the user for various types of input, including text, location selection,
 * widget configuration, and custom forecast alerts.
 */
public class InputService {
    /**
     * Displays a text input dialog to the user and returns the entered string.
     *
     * @param title   The title of the dialog.
     * @param content The content text displayed in the dialog.
     * @return The user-entered string, or null if the dialog was canceled.
     */
    public static String getString(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Opens a location selector dialog, allowing the user to choose a location on the map.
     *
     * @param title   The title of the location selector dialog.
     * @param content The content text explaining the selection purpose.
     * @return The LocationCreateModel representing the selected location, or null if canceled.
     */
    public static LocationCreateModel getLocation(String title, String content) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/location-selector.fxml"));
            Parent root = loader.load();
            LocationSelectorController controller = loader.getController();
            controller.setContent(content);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            return controller.getSelectedLocation();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Opens a widget configuration dialog, allowing the user to configure a widget's settings.
     *
     * @param widgetInfo The WidgetInfo object containing initial widget settings.
     * @param parent     The DashboardController that manages the widget.
     * @return The updated WidgetInfo object, or null if canceled.
     */
    public static WidgetInfo getWidgetConfig(WidgetInfo widgetInfo, DashboardController parent) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/widget-config-dialog.fxml"));
            Parent root = loader.load();

            WidgetConfigDialogController controller = loader.getController();
            controller.setWidgetInfo(widgetInfo, parent);

            Stage stage = new Stage();
            stage.setTitle("Widget Configuration");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            return controller.getWidgetInfo();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Displays a confirmation dialog and returns true if the user confirms the action.
     *
     * @param title   The title of the confirmation dialog.
     * @param header  The header text of the confirmation dialog.
     * @param content The content text of the confirmation dialog.
     * @return True if the user clicks OK, false otherwise.
     */
    public static boolean getConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Opens a dialog to gather crowdsourced data input from the user.
     *
     * @return A CrowdsourcedDTO object with user-provided data, or null if canceled.
     */
    public static CrowdsourcedDTO getCrowdData() {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/crowdsourced-data-dialog.fxml"));
            Parent root = loader.load();
            CrowdsourcedDataDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Crowdsourced Data Configuration");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            return controller.getCrowdsourcedData();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Opens a dialog for creating a custom alert based on hourly forecast data.
     *
     * @return A CustomAlertCondition for the hourly forecast, or null if canceled.
     */
    public static CustomAlertCondition getHourlyForecastAlert() {
        return getForecastAlert(false);
    }

    /**
     * Opens a dialog for creating a custom alert based on daily forecast data.
     *
     * @return A CustomAlertCondition for the daily forecast, or null if canceled.
     */
    public static CustomAlertCondition getDailyForecastAlert() {
        return getForecastAlert(true);
    }

    /**
     * Opens a dialog for creating a custom forecast alert based on the specified forecast type.
     *
     * @param forecastType True for daily forecasts, false for hourly forecasts.
     * @return A CustomAlertCondition for the specified forecast type, or null if canceled.
     */
    public static CustomAlertCondition getForecastAlert(boolean forecastType) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/forecast-alert-dialog.fxml"));
            Parent root = loader.load();
            ForecastAlertDialogController controller = loader.getController();
            controller.setForecastType(forecastType);
            Stage stage = new Stage();
            stage.setTitle(forecastType ? "Daily Forecast Query Configuration" : "Hourly Forecast Query Configuration");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            return controller.getForecastAlert();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}