package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.io.IOException;

public class PageFactory {
    @Getter
    private static final PageManager pageManager = new PageManager();
    private final BorderPane root;

    public PageFactory(BorderPane root) {
        this.root = root;
    }

    public Node createSwapPanel(String fxmlPath, Button button) {
        FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(fxmlPath));
        Node panelNode;
        try {
            panelNode = loader.load();
            if (loader.getController() instanceof ISwapPanel pageController) {
                pageManager.registerPage(pageController);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        button.setOnAction(actionEvent -> this.root.centerProperty().set(panelNode));
        return panelNode;
    }

    public void updateAllPages() {
        pageManager.updatePages();
    }

    public void clearAllPages() {
        pageManager.unregisterAllPages();
    }
}
