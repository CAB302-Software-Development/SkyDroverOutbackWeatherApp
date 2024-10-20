package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import lombok.Getter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionService {
    @Getter
    private static ConnectionService instance = new ConnectionService();

    @Getter
    private boolean isOffline = false;

    private ScheduledExecutorService scheduler;

    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        updateSchedulerDelay(600);
    }

    private void updateSchedulerDelay(int seconds) {
        scheduler.scheduleAtFixedRate(this::updateLocalDB, 0, seconds, TimeUnit.MINUTES);
    }

    private void updateLocalDB() {
        new Thread(() -> AlertsService.getInstance().updateBOMAlertsForCurrentUserLocations()).start();
        setOffline(ForecastService.getInstance().updateForecastsForCurrentUser(7, 2));
    }

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

    public void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
