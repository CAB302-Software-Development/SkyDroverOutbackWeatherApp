import static org.junit.jupiter.api.Assertions.assertEquals;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class ClickableButtonTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("splash-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), LoginController.WIDTH, LoginController.HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testButtonClickUpdatesLabel() {
        // Click the button by its text
        clickOn("Hello!");

        // Verify the label text has been updated
        Label label = lookup("#welcomeText").query();
        assertEquals("Welcome to JavaFX Application!", label.getText());
    }
}