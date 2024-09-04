package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.SplashController;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ApplicationEntry extends Application {
  public static final String stageTitle = "Outback Weather Tracker";

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("splash-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), SplashController.WIDTH, SplashController.HEIGHT);

    stage.setTitle(stageTitle);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    Logger.printLog("Application started, " + stageTitle);

    launch();
  }
}