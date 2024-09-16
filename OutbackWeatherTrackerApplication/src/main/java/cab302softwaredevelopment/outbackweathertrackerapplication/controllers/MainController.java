package cab302softwaredevelopment.outbackweathertrackerapplication.controllers;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.ISwapPanel;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.PreferencesService;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 600;

    private static MainController controller;

    @FXML
    public Button btnProfile, btnDashboard, btnMap, btnForecast, btnSettings, btnDrawer, btnAlerts, btnReports;
    @FXML
    SVGPath icoProfile, icoDashboard, icoMap, icoForecast, icoSettings, icoDrawer, icoAlerts, icoReports;
    @FXML
    public VBox vbNavbar;
    private Scene scene;
    @FXML
    BorderPane root;

    public void setScene(Scene scene) {
        this.scene = scene;
        refreshDisplay();
    }

    public static void refreshDisplay() {
        controller.scene.getStylesheets().clear();
        controller.scene.getStylesheets().addAll(PreferencesService.getCurrentThemeData());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSVGs();

        controller = this;
        Node swpProfile = createSwapPanel("panels/profile-panel.fxml", btnProfile);
        Node swpDashboard = createSwapPanel("panels/dashboard-panel.fxml", btnDashboard);
        Node swpForecast = createSwapPanel("panels/forecast-panel.fxml", btnForecast);
        Node swpMap = createSwapPanel("panels/map-panel.fxml", btnMap);
        Node swpSettings = createSwapPanel("panels/settings-panel.fxml", btnSettings);
        root.centerProperty().set(swpDashboard);
    }

    private void loadSVGs() {
        // Set<Node> svgs = root.lookupAll(".icon");
        // Node temp = ((Button)svgs.iterator().next()).getGraphic();
        // svgs.forEach(s -> setSvgPathFromFile((SVGPath) s, s.parentProperty().get()));

        //setSvgPathFromFile(icoProfile);
        //setSvgPathFromFile(icoDashboard);
        //setSvgPathFromFile(icoForecast);
        //setSvgPathFromFile(icoMap);
        //setSvgPathFromFile(icoSettings, btnSettings);
        //setSvgPathFromFile(icoDrawer);
        //setSvgPathFromFile(icoAlerts);
        //setSvgPathFromFile(icoReports);
    }

    private Node createSwapPanel(String fxmlPath, Button button) {
        FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(fxmlPath));
        Node panelNode;
        try {
            panelNode = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        button.setOnAction(actionEvent -> root.centerProperty().set(panelNode));
        //button.getGraphic().scaleXProperty().bind(button.widthProperty().divide(1000));
        //button.getGraphic().scaleYProperty().bind(button.heightProperty().divide(1000));
        return panelNode;
    }

    /**
     * Sets the SVG content of an SVGPath element from a file.
     *
     * @param svgPath the SVGPath node to update
     */
    private void setSvgPathFromFile(SVGPath svgPath, Parent parent) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse((String) svgPath.getUserData());

            NodeList pathElements = document.getElementsByTagName("path");
            if (pathElements.getLength() > 0) {
                String pathData = pathElements.item(0).getAttributes().getNamedItem("d").getNodeValue();
                svgPath.setContent(pathData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Node node = svgPath.getParent();
        // Button container = (Button) node;
        //Scene container = parent.getScene();
        //svgPath.scaleXProperty().bind(container.widthProperty().divide(1000));
        //svgPath.scaleYProperty().bind(container.heightProperty().divide(1000));
    }
}
