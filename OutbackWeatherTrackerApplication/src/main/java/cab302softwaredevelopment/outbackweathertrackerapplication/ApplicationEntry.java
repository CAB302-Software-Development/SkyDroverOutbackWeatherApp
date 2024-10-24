package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ConnectionService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import java.awt.*;
import java.io.IOException;

public class ApplicationEntry extends Application {
  private static Stage rootStage;

  /**
   * Starts the application and sets up the main stage and scene.
   *
   * @param stage The primary stage for the application.
   * @throws IOException if the FXML file cannot be loaded.
   */
  @Override
  public void start(Stage stage) {
    if (!GraphicsEnvironment.isHeadless()) {
      rootStage = stage;

      // TODO Check if valid credentials already exist, skip login window if so
      try {
        openLoginWindow();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      Logger.printLog("Running in headless mode. No UI will be shown");
    }
  }

  public static void openLoginWindow() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("windows/login-signup.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), LoginController.WIDTH, LoginController.HEIGHT);

    Stage newStage = new Stage();
    newStage.setTitle(LoginController.TITLE);
    newStage.setScene(scene);
    setMainStage(newStage);
  }

  public static void openMainWindow() throws IOException {
    ConnectionService.getInstance().init();
    FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/main-view.fxml"));
    Scene scene = new Scene(loader.load(), MainController.WIDTH, MainController.HEIGHT);
    MainController controller = loader.getController();
    controller.setScene(scene);

    Stage newStage = new Stage();
    newStage.setTitle(MainController.TITLE);
    newStage.setScene(scene);
    setMainStage(newStage);
  }

  private static void setMainStage(Stage stage) {
    rootStage = stage;
    rootStage.setOnCloseRequest(event -> {
      ConnectionService.getInstance().shutdownScheduler();
      MainController.shutdownScheduler();
      Platform.exit();
    });
    rootStage.show();
  }

  /**
   * Main method that launches the application.
   *
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    Session session = DatabaseConnection.getSession();
    // addTestData();
    Logger.printLog("Application started");
    launch();
  }
}