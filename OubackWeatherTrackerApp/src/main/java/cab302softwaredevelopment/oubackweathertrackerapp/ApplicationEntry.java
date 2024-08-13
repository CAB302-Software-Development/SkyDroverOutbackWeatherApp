package cab302softwaredevelopment.oubackweathertrackerapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.Test;

import java.io.IOException;

public class ApplicationEntry extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    // test test test
    @Test
    public void test() {
    }

    public static void main(String[] args) {
        launch();
    }
}