package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import lombok.Getter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionService {
    @Getter
    private static boolean isOffline = false;

    private static ScheduledExecutorService scheduler;

    public static void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        updateSchedulerDelay(600);
    }

    private static void updateSchedulerDelay(int seconds) {
        scheduler.scheduleAtFixedRate(ConnectionService::updateLocalDB, 0, seconds, TimeUnit.MINUTES);
    }

    private static void updateLocalDB() {
        setOffline(ForecastService.updateForecastsForCurrentUser(7, 2));
    }

    public static void setOffline(boolean value) {
        if (isOffline != value) {
            ConnectionService.isOffline = value;
            if (isOffline()) {
                updateSchedulerDelay(10);
                MainController.showAlert("Offline Mode", "You are currently offline. Data may be outdated.");
            } else {
                updateSchedulerDelay(600);
                MainController.showAlert("Online Mode", "Connection established. Updating local data.");
            }
        }
    }

    public static void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
