package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.MainController;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import lombok.Getter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionService {
    @Getter
    private static ConnectionService instance = new ConnectionService();

    @Getter
    private boolean isOffline = true;
    @Getter
    private boolean openMeteoDataOffline = false;
    @Getter
    private boolean openWeatherMapOffline = false;
    @Getter
    private boolean restAPIOffline = true;

    private ScheduledExecutorService scheduler;


    public ConnectionService() {
        init();
    }

    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateLocalDB, 0, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(this::doConnectivityCheck, 0, 10, TimeUnit.SECONDS);
    }

    private void doConnectivityCheck() {
        ConnectivityApiService.getInstance().test();
    }

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



    public void setOffline(boolean value) {
        Logger.printLog("Setting offline status to: " + value);

        if (isOffline != value) {
            isOffline = value;
            if (isOffline()) {
                //updateSchedulerDelay(10);
                MainController.showAlert("Offline Mode", "You are currently offline. Data may be outdated.");
            } else {
                //updateSchedulerDelay(600);
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
