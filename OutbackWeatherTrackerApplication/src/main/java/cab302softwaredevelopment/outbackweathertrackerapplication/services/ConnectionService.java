package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import lombok.Getter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionService {
    @Getter
    private static final ConnectionService instance = new ConnectionService();

    @Getter
    private boolean isOffline = false;

    private ScheduledExecutorService scheduler;

    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        updateSchedulerDelay(600);
        setOffline(!isInternetAvailable());
    }

    private void updateSchedulerDelay(int seconds) {
        scheduler.scheduleAtFixedRate(this::updateLocalDB, 5, seconds, TimeUnit.SECONDS);
    }

    private void updateLocalDB() {
        boolean updateResult = ForecastService.getInstance().updateForecastsForCurrentUser(7, 2);
        setOffline(!updateResult);
    }

    public void setOffline(boolean value) {
        if (isOffline != value) {
            isOffline = value;
            if (isOffline) {
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

    private boolean isInternetAvailable() {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setConnectTimeout(5000);
            urlConnect.connect();
            return urlConnect.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    public void forceRefresh() {
    }
}
