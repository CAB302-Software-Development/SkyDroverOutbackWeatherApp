package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ApplicationEntry extends Application {
  public static final String stageTitle = "Outback Weather Tracker";

  public static final int initialStageWidth = 1000;
  public static final int initialStageHeight = 800;

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("hello-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), initialStageWidth, initialStageHeight);

    stage.setTitle(stageTitle);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    Logger.printLog("Application started, " + stageTitle);

    launch();
  }
}