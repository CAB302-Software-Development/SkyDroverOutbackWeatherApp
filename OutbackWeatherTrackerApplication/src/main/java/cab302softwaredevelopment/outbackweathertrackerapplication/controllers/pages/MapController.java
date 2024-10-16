package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.FileWriter;

public class MapController extends BasePage {
  private static final String BASE_URL = "http://localhost:8090/api/crowdsourced";

  @FXML
  private WebView mapWebView;

  @FXML
  public void initialize() {
    WebEngine webEngine = mapWebView.getEngine();

    String htmlFile = getClass().getResource(
                    "/cab302softwaredevelopment/outbackweathertrackerapplication/map/map.html")
            .toExternalForm();

    writeDataToJsonFile();

    webEngine.load(htmlFile);
  }

  @Override
  public void updateData() {

  }

  private void writeDataToJsonFile() {
    try {
      String jsonData = new Gson().toJson(fetchRecentData());

      try (FileWriter fileWriter = new FileWriter("/home/segus-faultise/Projects/SkyDroverOutbackWeatherApp/OutbackWeatherTrackerApplication/src/main/resources/cab302softwaredevelopment/outbackweathertrackerapplication/map/data.json")) {
        fileWriter.write(jsonData);
      }
      Logger.printLog("Data written to JSON file successfully.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String fetchRecentData() throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/latest-markers"))
            .header("Content-Type", "application/json")
            .GET()
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    Logger.printLog(response.body());

    return response.body(); // Return the raw JSON response
  }
}