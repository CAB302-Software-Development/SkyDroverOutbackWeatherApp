package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SplashController {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    @FXML
    private Label welcomeText;
    @FXML
    private Button helloButton;
    @FXML
    private Button loginButton;


    public void handleButtonClick(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == helloButton) {
            welcomeText.setText("Welcome to JavaFX Application!");
        } else if (actionEvent.getSource() == loginButton) {
            Stage stage = (Stage)welcomeText.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), MainController.WIDTH, MainController.HEIGHT);
            stage.setScene(scene);
        }
    }
}