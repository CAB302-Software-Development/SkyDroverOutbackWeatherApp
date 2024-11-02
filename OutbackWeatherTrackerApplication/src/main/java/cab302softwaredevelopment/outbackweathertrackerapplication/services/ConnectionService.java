package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
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
    private boolean isOffline = true;
    @Getter
    private boolean openMeteoDataOffline = false;
    @Getter
    private boolean openWeatherMapOffline = false;
    @Getter
    private boolean restAPIOffline = true;

    /**
     * Scheduled executor service for periodically updating local data.
     */
    private ScheduledExecutorService scheduler;

   public ConnectionService() {
        init();
    }
    /**
     * Initializes the ConnectionService, setting up a scheduler for periodic data updates.
     */
   
    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateLocalDB, 0, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(this::doConnectivityCheck, 0, 10, TimeUnit.SECONDS);
    }


    private void doConnectivityCheck() {
        ConnectivityApiService.getInstance().test();
    }

    /**
     * Updates the local database with alerts and forecast data.
     * Checks connection status and sets the application to offline mode if updates fail.
     */
    private void updateLocalDB() {
        try {
            AlertsService.getInstance().updateBOMAlertsForCurrentUserLocations();
            ForecastService.getInstance().updateForecastsForCurrentUser(7, 2);
        } catch (Exception e) {
            e.printStackTrace(); // Or use proper logging
        }
    }

    public void setOpenMeteoDataOffline(boolean value) {
        Logger.printLog("Setting OpenMeteo offline status to: " + value);
        openMeteoDataOffline = value;
        updateOfflineStatus();
    }

    public void setOpenWeatherMapOffline(boolean value) {
        Logger.printLog("Setting Open Weather Map offline status to: " + value);
        openWeatherMapOffline = value;
        updateOfflineStatus();
    }

    public void setRestAPIOffline(boolean value) {
        Logger.printLog("Setting rest API offline status to: " + value);
        restAPIOffline = value;
        updateOfflineStatus();
    }

    public void updateOfflineStatus() {
        if (openMeteoDataOffline || openWeatherMapOffline || restAPIOffline) {
            setOffline(true);
        } else {
            setOffline(false);
        }
    }

    /**
     * Sets the application's offline status and adjusts update frequency based on connectivity.
     * Triggers a notification to the user indicating the current connection mode.
     *
     * @param value True to set the application to offline mode; false for online mode.
     */
    public void setOffline(boolean value) {
        Logger.printLog("Setting offline status to: " + value);

        if (isOffline != value) {
            isOffline = value;
            if (isOffline()) {
                //updateSchedulerDelay(10);
                InputService.showAlert("Offline Mode", "You are currently offline. Data may be outdated.");
            } else {
                //updateSchedulerDelay(600);
                InputService.showAlert("Online Mode", "Connection established. Updating local data.");
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
