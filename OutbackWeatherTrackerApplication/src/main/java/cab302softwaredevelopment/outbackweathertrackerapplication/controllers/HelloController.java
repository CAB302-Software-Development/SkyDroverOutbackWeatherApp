package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    private Button helloButton;
    private Button goodbyeButton;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void onGoodbyeButtonClick() {
        welcomeText.setText("Goodbye JavaFX Application!");
    }
}