package cab302softwaredevelopment.oubackweathertrackerapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TempController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}