package cab302softwaredevelopment.outbackweathertrackerapplication;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import java.io.IOException;
import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.DAO.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.OpenMeteo.*;



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
    Connection connection = DatabaseConnection.getInstance();
    LocationDAO locationDAO = new LocationDAO();
    DailyForecastDAO dailyForecastDAO = new DailyForecastDAO();
    HourlyForecastDAO hourlyForecastDAO = new HourlyForecastDAO();
    // Create required tables
    locationDAO.createTable();
    dailyForecastDAO.createTable();
    hourlyForecastDAO.createTable();

    // Insert some new records
    locationDAO.insert(new Location(153.02333324, -27.467331464, 27.0)); // brisbane
    locationDAO.insert(new Location(153.0372, -27.5703, 23.0)); // coopers plains

    // update weather data
    Sdk openMeteoSdk = new Sdk();
    Location location = locationDAO.getById(2);
    System.out.println(location);
    openMeteoSdk.updateDailyForecast(location);
    openMeteoSdk.updateHourlyForecast(location);

    //List<Location> locations = locationDAO.getAll();
    //for (Location location : locations) {
    //  System.out.println(location);
    //}

    // Retrieve a record by ID
    //Location location = locationDAO.getById(2);
    //System.out.println(location);

    //locationDAO.delete(1);
    //System.out.println("After deleting record with id = 1:");
    //for (Location location : locationDAO.getAll()) {
    //  System.out.println(location);
    //}
    locationDAO.close();
    dailyForecastDAO.close();

    launch();
  }
}