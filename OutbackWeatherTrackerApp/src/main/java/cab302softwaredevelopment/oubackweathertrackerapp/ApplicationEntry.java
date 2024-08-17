package cab302softwaredevelopment.oubackweathertrackerapp;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ApplicationEntry extends javafx.application.Application {
    private Button button;

    public void start(Stage stage) {
        button = new Button("click me!");
        button.setId("myButton");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}