package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import cab302softwaredevelopment.outbackweathertrackerapplication.ApplicationEntry;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.io.IOException;

/**
 * Factory class responsible for creating and managing page panels within the application's main layout.
 * Facilitates the creation of pages and registers them with a centralized page manager.
 */
public class PageFactory {
    /**
     * Singleton instance of the PageManager that handles page registration and updates.
     */
    @Getter
    private static final PageManager pageManager = new PageManager();
    private final BorderPane root;

    public PageFactory(BorderPane root) {
        this.root = root;
    }

    /**
     * Creates a panel from the specified FXML file and registers it as a swappable page.
     * Sets up the button to display the panel in the main layout's center when clicked.
     *
     * @param fxmlPath The path to the FXML file defining the panel's layout.
     * @param button The button that, when clicked, will load the panel into the center of the main layout.
     * @return The Node representing the loaded panel, or null if loading fails.
     */
    public Node createSwapPanel(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationEntry.class.getResource(fxmlPath));
            Node panelNode = loader.load();
            if (loader.getController() instanceof ISwapPanel pageController) {
                pageManager.registerPage(pageController);
            }
            button.setOnAction(actionEvent -> this.root.centerProperty().set(panelNode));
            return panelNode;
        } catch (IOException e) {
            e.printStackTrace();
            button.disableProperty().set(true);
            return null;
        }
    }

    /**
     * Updates all registered pages, allowing each page to refresh its data and display.
     */
    public void updateAllPages() {
        pageManager.updatePages();
    }

    /**
     * Clears all registered pages from the page manager, releasing resources and unregistering them.
     */
    public void clearAllPages() {
        pageManager.unregisterAllPages();
    }
}
