package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.SplashController;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.DailyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.HourlyForecast;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
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
    Session session = DatabaseConnection.getSession();
    LocationDAO locationDAO = new LocationDAO();
    DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();
    HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
    // Create required tables
    //locationDAO.createTable();
    //dailyForecastDAO.createTable();
    //hourlyForecastDAO.createTable();

    // Insert some new records
    locationDAO.insert(new Location(153.02333324, -27.467331464, 27.0,"Brisbane")); // brisbane
    locationDAO.insert(new Location(153.0372, -27.5703, 23.0,"Coopers Plains")); // coopers plains

    // update weather data
    Sdk openMeteoSdk = new Sdk();
    Location location = locationDAO.getById(1);
    System.out.println(location);
    openMeteoSdk.updateDailyForecast(location);
    openMeteoSdk.updateHourlyForecast(location);

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


    launch();
  }
}