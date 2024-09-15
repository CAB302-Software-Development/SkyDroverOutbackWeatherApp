package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;

public class ProfileController implements ISwapPanel {
    @FXML
    Panel pnlRoot;

    public void initialize(Pane parent) {
        pnlRoot.prefWidthProperty().bind(parent.widthProperty());
        pnlRoot.prefHeightProperty().bind(parent.heightProperty());
    }

    @Override
    public void updateAppearance() {

    }

    @FXML
    public void showLoginPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("popups/login-signup.fxml"));
            Parent root = fxmlLoader.load();
            Stage popupStage = new Stage();
            popupStage.setTitle("Login");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}