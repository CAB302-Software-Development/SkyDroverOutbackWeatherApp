import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClickableButtonTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cab302softwaredevelopment/outbackweathertrackerapplication/hello-view.fxml"));
        VBox root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testButtonClickUpdatesLabel() {
        // Click the button by its text
        clickOn("Hello!");

        // Verify the label text has been updated
        Label label = (Label) lookup("#welcomeText").query();
        assertEquals("Welcome to JavaFX Application!", label.getText());
    }
}