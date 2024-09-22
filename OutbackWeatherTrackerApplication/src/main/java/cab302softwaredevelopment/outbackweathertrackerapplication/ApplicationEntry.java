package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import java.util.List;
import java.awt.*;
import java.io.IOException;

public class ApplicationEntry extends Application {
  private static Stage rootStage;

  public static final String stageTitle = "Outback Weather Tracker";

  /**
   * Starts the application and sets up the main stage and scene.
   *
   * @param stage The primary stage for the application.
   * @throws IOException if the FXML file cannot be loaded.
   */
  @Override
  public void start(Stage stage) {
    if (!GraphicsEnvironment.isHeadless()) {
      PreferencesService.loadPreferences();
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

    rootStage = new Stage();
    rootStage.setTitle("Login");
    rootStage.setScene(scene);
    rootStage.show();
  }

  public static void openMainWindow() throws IOException {
    FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource("windows/main-view.fxml"));
    Scene scene = new Scene(loader.load(), MainController.WIDTH, MainController.HEIGHT);
    MainController controller = loader.getController();
    controller.setScene(scene);

    rootStage = new Stage();
    rootStage.setTitle(stageTitle);
    rootStage.setScene(scene);
    rootStage.show();
  }

  /**
   * Main method that launches the application.
   *
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    addTestData();
    Logger.printLog("Application started, " + stageTitle);
    Session session = DatabaseConnection.getSession();
    launch();
  }

  private static void addTestData() {
    Logger.printLog("Application started, " + stageTitle);
    Session session = DatabaseConnection.getSession();
    AccountDAO accountDAO = new AccountDAO();
    LocationDAO locationDAO = new LocationDAO();

    // Insert some new accounts
    // Add the accounts to the template
    accountDAO.insert(new Account("guest@guest.com", "SecurePass1!",true));

    Account account = new AccountDAO.AccountQuery().whereEmail("guest@guest.com").getSingleResult();

    // Insert some new records
    locationDAO.insert(new Location(account,153.02333324, -27.467331464, 27.0,"Brisbane")); // brisbane
    locationDAO.insert(new Location(account,153.0372, -27.5703, 23.0,"Coopers Plains")); // coopers plains
  }

}