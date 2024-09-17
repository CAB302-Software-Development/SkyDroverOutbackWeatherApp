package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;


public class MapController {

  @FXML
  private WebView mapWebView;

  @FXML
  public void initialize() {
    WebEngine webEngine = mapWebView.getEngine();

    String htmlFile = getClass().getResource(
            "/cab302softwaredevelopment/outbackweathertrackerapplication/map/map.html")
        .toExternalForm();
    webEngine.load(htmlFile);
  }
}