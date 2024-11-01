package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.pages;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class responsible for handling the lifecycle and updates of registered pages
 * that implement the ISwapPanel interface. Manages page registration, unregistration,
 * and data updates across all pages.
 */
public class PageManager {
    /**
     * List of registered pages (observers) that implement the ISwapPanel interface.
     */
    private final List<ISwapPanel> observers = new ArrayList<>();

    /**
     * Registers a page to the manager, allowing it to receive updates.
     *
     * @param observer The page to register, implementing the ISwapPanel interface.
     */
    public void registerPage(ISwapPanel observer) {
        observers.add(observer);
    }

    /**
     * Unregisters a page from the manager, stopping it from receiving updates.
     *
     * @param observer The page to unregister.
     */
    public void unregisterPage(ISwapPanel observer) {
        observers.remove(observer);
    }

    /**
     * Updates all registered pages by calling their updateData method asynchronously
     * on the JavaFX Application Thread.
     */
    public void updatePages() {
        for (ISwapPanel observer : observers) {
            try {
                Platform.runLater(observer::updateData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Unregisters all pages from the manager, clearing the list of observers.
     */
    public void unregisterAllPages() {
        observers.clear();
    }
}