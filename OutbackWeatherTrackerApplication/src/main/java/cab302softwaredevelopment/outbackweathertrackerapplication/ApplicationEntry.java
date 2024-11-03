package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.ConnectionService;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.SyncService;
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

  /**
   * Opens the login window, setting it as the primary scene for user authentication.
   *
   * @throws IOException if the login window's FXML file cannot be loaded.
   */
  public static void openLoginWindow() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("windows/login-signup.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), LoginController.WIDTH, LoginController.HEIGHT);

    Stage newStage = new Stage();
    newStage.setTitle(LoginController.TITLE);
    newStage.setScene(scene);
    setMainStage(newStage);
  }

  /**
   * Opens the main application window after successful login, initializing necessary services.
   * Sets up the primary scene and controller for the main dashboard.
   *
   * @throws IOException if the main window's FXML file cannot be loaded.
   */
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

  /**
   * Sets the specified stage as the main application stage and defines behavior for closing the application.
   * Ensures that services and schedulers are properly shut down when the application is closed.
   *
   * @param stage The stage to set as the main application window.
   */
  private static void setMainStage(Stage stage) {
    rootStage = stage;
    rootStage.setOnCloseRequest(event -> {
      ConnectionService.getInstance().shutdownScheduler();
      MainController.shutdownScheduler();
      SyncService.getInstance().shutdownScheduler();
      Platform.exit();
    });
    rootStage.setMaximized(true);
    rootStage.show();
  }

  /**
   * Main method that launches the application.
   *
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    Session session = DatabaseConnection.getSession();
    // start services

    // This defines a static location for map caching, but requires custom VM arguments
    // Services.registerServiceFactory(new LocalStorageServiceFactory());
    // Start the connection service
    ConnectionService connectionService = ConnectionService.getInstance();
    // Start the sync service
    SyncService syncService = SyncService.getInstance();
    Logger.printLog("Application started");
    launch();
  }
}