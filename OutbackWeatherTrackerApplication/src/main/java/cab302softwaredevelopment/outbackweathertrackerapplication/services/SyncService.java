package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.UserModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import java.text.DateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import lombok.Getter;

public class SyncService {

  /**
   * Singleton instance of SyncService for global access.
   */
  @Getter
  private static SyncService instance = new SyncService();

  /**
   * Scheduled executor service for periodically syncing data.
   */
  private ScheduledExecutorService scheduler;

  public SyncService() {
    init();
  }

  /**
   * Initializes the SyncService, setting up a scheduler for periodic data syncing.
   */
  public void init() {
    scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(this::syncData, 0, 10, TimeUnit.SECONDS);
  }

  /**
   * Syncs the local database with the remote server.
   * Checks connection status and sets the application to offline mode if syncing fails.
   */
  public void syncData() {
    boolean offlineMode = ConnectionService.getInstance().isOffline();
    if (offlineMode) {
      Logger.printLog("Cannot sync data while offline");
      return;
    }

    // If there is a logged-in user, sync their data
    Account localAccount = UserService.getInstance().getCurrentAccount();

    if (localAccount == null) {
      Logger.printLog("No user logged in, skipping data sync");
      return;
    }

    try {
      // Get the remote account
      Logger.printLog("Attempting to fetch remote user " + localAccount.getUsername());
      UserApiService userApiService = new UserApiService();
      UserModel userModel = userApiService.getCurrentUser(localAccount.getJWTToken());
      Account remoteAccount = userApiService.getCurrentAccount(userModel, localAccount.getJWTToken());

      Logger.printLog("Found remote user: " + remoteAccount.getUsername());

      // Check the modified date of the remote account
      if (remoteAccount.getLastModified().after(localAccount.getLastModified())) {
        Logger.printLog("Remote account is newer, skipping update");
        DateFormat DFormat = DateFormat.getDateTimeInstance();
        Logger.printLog("Remote account updated at: " + DFormat.format(remoteAccount.getLastModified()));
        Logger.printLog("Local account updated at: " + DFormat.format(localAccount.getLastModified()));
        // NOTE: Could have updated here, but it would be odd for the account to be updated while the user is using the application
      } else if(remoteAccount.getLastModified().before(localAccount.getLastModified())) {
        Logger.printLog("Local account is newer, updating remote account");
        userApiService.updateUser(localAccount, localAccount.getJWTToken());
      }
    } catch (Exception e) {
      Logger.printLog("Failed to sync data for user: " + localAccount.getUsername());
      ConnectionService.getInstance().setOffline(true); // Server must be down or broken
    }
  }

  /**
   * Shuts down the scheduler for the SyncService. Should be called when the application is closed.
   */
  public static void shutdownScheduler() {
    instance.scheduler.shutdown();
  }
}
