package UITests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets.CurrentTempController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;


//Tests for Current Temp Widgets
public class CurrentTempWidgetTest extends ApplicationTest {
  // import the controller
  private CurrentTempController controller;

  // Override the start method to load the FXML file
    @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cab302softwaredevelopment/outbackweathertrackerapplication/widgets/current-temp-widget.fxml"));
    // Load the FXML file

    // Create a new scene with the loaded FXML file
    Scene scene = new Scene(fxmlLoader.load());
    stage.setScene(scene);
    stage.show();
  }

  @Test
  public void testTemperatureLabelDisplaysCorrectly() {
    // Lookup the label in the widget
    Label lblTemp = lookup("#lblTemp").query();
    assertEquals("25.0° Celsius", lblTemp.getText());
  }

  @Test
  public void testLoadTemperatureData() {
      // Lookup the label in the widget
      Label lblTemp = lookup("#lblTemp").query();
      assertEquals("25.0° Celsius", lblTemp.getText());
  }

  @Test
  public void testLoadTemperatureDataNoLocation() {
      // Lookup the label in the widget
      Label lblTemp = lookup("#lblTemp").query();
      assertEquals("No location set.", lblTemp.getText());
  }

}


