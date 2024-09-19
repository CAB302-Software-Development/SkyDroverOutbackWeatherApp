package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.LoginController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DatabaseConnection;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.Sdk;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.DailyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.HourlyForecastDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.*;
import org.hibernate.Session;

import java.awt.*;
import java.io.IOException;
import java.util.List;


public class ApplicationEntry extends Application {

  public static final String stageTitle = "Outback Weather Tracker";

  @Override
  public void start(Stage stage) throws IOException {
    if (!GraphicsEnvironment.isHeadless()) {
      PreferencesService.loadPreferences();
      FXMLLoader fxmlLoader = new FXMLLoader(ApplicationEntry.class.getResource("windows/login-signup.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), LoginController.WIDTH, LoginController.HEIGHT);

      stage.setTitle("Login");
      stage.setScene(scene);
      stage.show();
    } else {
      Logger.printLog("Running in headless mode. No UI will be shown");
    }
  }

  public static void main(String[] args) {
    Logger.printLog("Application started, " + stageTitle);
    Session session = DatabaseConnection.getSession();
    AccountDAO accountDAO = new AccountDAO();
    LocationDAO locationDAO = new LocationDAO();
    DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();
    HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
    // Create required tables
    //locationDAO.createTable();
    //dailyForecastDAO.createTable();
    //hourlyForecastDAO.createTable();
/*
    // Insert some new accounts
    // Add the accounts to the template
    accountDAO.insert(new Account("test1@gmail.com", "password1",true));
    accountDAO.insert(new Account("test2@gmail.com", "password2",true));
    accountDAO.insert(new Account("test3@gmail.com", "password3",true));

    Account account = accountDAO.getById(1);

    // Insert some new records
    locationDAO.insert(new Location(account,153.02333324, -27.467331464, 27.0,"Brisbane")); // brisbane
    locationDAO.insert(new Location(account,153.0372, -27.5703, 23.0,"Coopers Plains")); // coopers plains


    // update weather data
    Sdk openMeteoSdk = new Sdk();
    Location location = locationDAO.getById(1);
    System.out.println(location);
    openMeteoSdk.updateDailyForecast(location,10,0);
    openMeteoSdk.updateHourlyForecast(location,10,0);

    List<Location> locations = locationDAO.getAll();
    for (Location location2 : locations) {
      System.out.println(location2);
    }

    List<HourlyForecast> hourlyForecasts = hourlyForecastDAO.getAll();
    for (HourlyForecast hourlyForecast : hourlyForecasts) {
      System.out.println(hourlyForecast);
    }

    List<DailyForecast> dailyForecasts = dailyForecastDAO.getAll();
    for (DailyForecast dailyForecast : dailyForecasts) {
      System.out.println(dailyForecast);
    }
*/
    launch();
  }
}