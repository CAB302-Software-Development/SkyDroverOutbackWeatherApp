package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import lombok.Getter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service class responsible for managing the application's connection status.
 * Monitors connectivity, triggers data updates, and handles offline and online states.
 */
public class ConnectionService {
    /**
     * Singleton instance of ConnectionService for global access.
     */
    @Getter
    private static ConnectionService instance = new ConnectionService();

    /**
     * Flag indicating whether the application is currently offline.
     */
    @Getter
    private boolean isOffline = false;

    /**
     * Scheduled executor service for periodically updating local data.
     */
    private ScheduledExecutorService scheduler;

    /**
     * Initializes the ConnectionService, setting up a scheduler for periodic data updates.
     */
    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        updateSchedulerDelay(600);
    }

    /**
     * Updates the scheduling delay for data updates.
     *
     * @param seconds The interval in seconds at which data updates will be scheduled.
     */
    private void updateSchedulerDelay(int seconds) {
        scheduler.scheduleAtFixedRate(this::updateLocalDB, 0, seconds, TimeUnit.MINUTES);
    }

    /**
     * Updates the local database with alerts and forecast data.
     * Checks connection status and sets the application to offline mode if updates fail.
     */
    private void updateLocalDB() {
        new Thread(() -> AlertsService.getInstance().updateBOMAlertsForCurrentUserLocations()).start();
        boolean result = ForecastService.getInstance().updateForecastsForCurrentUser(7, 2);
        if (!result) setOffline(true);
    }

    /**
     * Sets the application's offline status and adjusts update frequency based on connectivity.
     * Triggers a notification to the user indicating the current connection mode.
     *
     * @param value True to set the application to offline mode; false for online mode.
     */
    public void setOffline(boolean value) {
        if (isOffline != value) {
            isOffline = value;
            if (isOffline()) {
                updateSchedulerDelay(10);
                MainController.showAlert("Offline Mode", "You are currently offline. Data may be outdated.");
            } else {
                updateSchedulerDelay(600);
                MainController.showAlert("Online Mode", "Connection established. Updating local data.");
            }
        }
    }

    /**
     * Shuts down the scheduler, stopping any further data updates. MUST be called before application close.
     */
    public void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
