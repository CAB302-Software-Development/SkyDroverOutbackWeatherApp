package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages.DashboardController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.LocationSelectorController;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.WidgetConfigDialogController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.LocationCreateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class InputService {

    // Method to request string input directly via TextInputDialog
    public static String getString(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    // Method to request location using the FXML and LocationPopupController
    public static LocationCreateModel getLocation(String title, String content) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/location-selector.fxml"));
            Parent root = loader.load();
            LocationSelectorController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            return controller.getSelectedLocation();

        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error case
        }
    }

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
            return null; // Handle error case
        }
    }

    public static HourlyForecastDAO.HourlyForecastQuery getHourlyForecastQuery() {
        return new HourlyForecastDAO.HourlyForecastQuery();
    }

    public static DailyForecastDAO.DailyForecastQuery getDailyForecastQuery() {
        return new DailyForecastDAO.DailyForecastQuery();
    }
}