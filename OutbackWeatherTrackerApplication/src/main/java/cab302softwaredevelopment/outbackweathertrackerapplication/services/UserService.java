package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters.LocationListConverter;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CreateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UpdateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetType;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import com.google.gson.Gson;
import java.text.DateFormat;
import lombok.Getter;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing user accounts and their associated data.
 * Provides functionality for user login, logout, account creation, profile updates, and layout management.
 */
public class UserService {
    @Getter
    private static UserService instance = new UserService();

    @Getter @Setter
    private Account currentAccount;

    /**
     * Logs in a user by setting the current account to the specified account.
     *
     * @param account The account to set as the currently logged-in user.
     */
    public void login(Account account) {
        currentAccount = account;
    }

    /**
     * Logs in a user by email and password.
     * @param email The email of the account to log in to.
     * @param password The password of the account to log in to.
     * @throws Exception If the login fails. (will contain a message)
     */
    public void login(String email, String password) throws Exception {
        boolean offlineMode = ConnectionService.getInstance().isOffline();
        AccountDAO accountDAO = new AccountDAO();

        Account localAccount;

        if (!offlineMode) {
            // Login to account on server
            Logger.printLog("Attempting to log in to account with email: " + email);
            UserApiService userApiService = new UserApiService();
            String token;
            token = userApiService.login(email, password);


            // Get the account
            UserModel userModel;
            Account remoteAccount;

            userModel = userApiService.getCurrentUser(token);
            remoteAccount = userApiService.getCurrentAccount(userModel, token);

            Logger.printLog("Successfully logged in to account with email: " + email);

            // Check the last modified date
            localAccount = (new AccountDAO.AccountQuery())
                .whereEmail(email)
                .getSingleResult();

            if (localAccount == null) {
                Logger.printLog("Account does not exist locally, creating account");
                // Account doesnt exist locally, create it
                accountDAO.insert(remoteAccount);
            } else {
                Logger.printLog("Account exists locally, checking for updates");
                DateFormat DFormat = DateFormat.getDateTimeInstance();
                Logger.printLog("Remote account updated at: " + DFormat.format(
                    remoteAccount.getLastModified()));
                Logger.printLog("Local account updated at: " + DFormat.format(
                    localAccount.getLastModified()));
                if (remoteAccount.getLastModified().after(localAccount.getLastModified())) {
                    Logger.printLog("Remote account is newer");
                    accountDAO.update(remoteAccount);

                    // The local locations are not up to date, update them
                    List<Location> localLocations = LocationService.getInstance()
                        .getLocationsForUser(localAccount);
                    List<Location> remoteLocations = new LocationListConverter().convertToEntityAttribute(
                        userModel.getLocations());

                    LocationDAO locationDAO = new LocationDAO();
                    // Delete all locations
                    for (Location location : localLocations) {
                        locationDAO.delete(location);
                    }

                    for (Location location : remoteLocations) {
                        locationDAO.insert(location);
                    }
                } else {
                    Logger.printLog("Local account is newer");
                    userApiService.updateUser(localAccount, localAccount.getJWTToken());
                }
            }
        }

        // Get the account
        localAccount = (new AccountDAO.AccountQuery())
            .whereEmail(email)
            .getSingleResult();

        if (localAccount == null) {
            throw new RuntimeException("No account exists with specified email.");
        }

        if (!localAccount.verifyPassword(password)) {
            throw new RuntimeException("Incorrect password");
        }
        login(localAccount);
    }

    /**
     * Checks if the current account is a guest account.
     *
     * @return True if the current account is a guest, false otherwise.
     */
    public boolean isGuest() {
        return currentAccount.getIsGuest();
    }

    /**
     * Logs out the current user and switches to a guest account with a default location.
     *
     * @param defaultLocation The default location to assign to the guest account.
     */
    public void logout(LocationCreateModel defaultLocation) {
        Account guestAccount = getGuestAccount(defaultLocation);
        currentAccount = guestAccount;
    }

    /**
     * Creates and returns a guest account, adding a default location and a basic dashboard layout.
     * Deletes any previous guest account before creating a new one.
     *
     * @param defaultLocation The location to assign to the guest account.
     * @return The newly created guest account, or null if an error occurs.
     */
    public Account getGuestAccount(LocationCreateModel defaultLocation) {
        try {
            Account guestAccount = new AccountDAO.AccountQuery().whereEmail("guest@guest.com").getSingleResult();
            if (guestAccount != null) {
                new AccountDAO().delete(guestAccount.getId());
                LocationService.getInstance().deleteLocationsForUser(guestAccount);
            }

            Account newGuestAccount = Account.builder()
                .id("guest")
                .username("Guest")
                .email("guest@guest.com")
                .password("SecurePass1!")
                .isGuest(true)
                .build();

            AccountDAO accountDAO = new AccountDAO();
            accountDAO.insert(newGuestAccount);
            guestAccount = new AccountDAO.AccountQuery().whereEmail("guest@guest.com").getSingleResult();

            LocationService.getInstance().addLocationForUser(guestAccount, defaultLocation);
            List<Location> locations = LocationService.getInstance().getLocationsForUser(guestAccount);
            generateDefaultDashboard(guestAccount, locations.getFirst());

            return new AccountDAO.AccountQuery().whereEmail("guest@guest.com").getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a default dashboard layout with basic widgets for the specified account and location.
     *
     * @param account  The account for which to create the default dashboard.
     * @param location The location to associate with the widgets.
     */
    private void generateDefaultDashboard(Account account, Location location) {
        WidgetConfig widgetConfig = new WidgetConfig();
        widgetConfig.setLocationId(location.getId());

        WidgetInfo[] defaultDashboard = new WidgetInfo[]{
                new WidgetInfo(WidgetType.Temperature, 0, 0, 2, 1, widgetConfig.getConfig()),
                new WidgetInfo(WidgetType.Soil, 0, 2, 2, 2, widgetConfig.getConfig()),
                new WidgetInfo(WidgetType.Precipitation, 1, 0, 2, 1, widgetConfig.getConfig())
        };

        UserService.getInstance().saveLayoutForUser(account, "default", defaultDashboard);
    }

    /**
     * Retrieves an account by its unique identifier.
     *
     * @param id The ID of the account to retrieve.
     * @return The account with the specified ID, or null if not found.
     */
    public Account getByID(String id) {
        return new AccountDAO.AccountQuery().whereId(id).getSingleResult();
    }

    /**
     * Updates the current account with new profile data and sets the updated account as the current account.
     *
     * @param newAccount An AccountUpdateModel containing the updated profile information.
     * @return True if the update was successful; false otherwise.
     */
    public boolean updateCurrentAccount(AccountUpdateModel newAccount) {
        newAccount.setId(getCurrentAccount().getId());
        boolean result = updateAccount(newAccount);
        if (result) {
            Account updatedAccount = getByID(newAccount.getId());
            setCurrentAccount(updatedAccount);
        }
        return result;
    }

    /**
     * Updates an account with new profile data based on the provided AccountUpdateModel.
     *
     * @param newAccount The AccountUpdateModel containing updated profile information.
     * @return True if the update was successful; false otherwise.
     */
    public boolean updateAccount(AccountUpdateModel newAccount) {
        Account existing = getByID(newAccount.getId());

        if (newAccount.getUsername() != null) {
            existing.setUsername(newAccount.getUsername());
        }
        if (newAccount.getEmail() != null) {
            existing.setEmail(newAccount.getEmail());
        }
        if (newAccount.getPassword() != null) {
            existing.setPassword(newAccount.getPassword());
        }

        if (newAccount.getPreferCelsius() != null) {
            existing.setPreferCelsius(newAccount.getPreferCelsius());
        }

        if (newAccount.getCurrentTheme() != null) {
            existing.setCurrentTheme(newAccount.getCurrentTheme());
        }

        if (newAccount.getSelectedLayout() != null) {
            existing.setSelectedLayout(newAccount.getSelectedLayout());
        }

        if (newAccount.getJWTToken() != null) {
            existing.setJWTToken(newAccount.getJWTToken());
        }

        if (newAccount.getDashboardLayouts() != null) {
            existing.setDashboardLayouts(newAccount.getDashboardLayouts());
        }

        if (newAccount.getCustomAlertConditions() != null) {
            existing.setCustomAlertConditions(newAccount.getCustomAlertConditions());
        }

        try {
            AccountDAO accountDAO = new AccountDAO();
            existing.setLastModified(new Date(System.currentTimeMillis()));
            accountDAO.update(existing);
            existing = new AccountDAO.AccountQuery()
                    .whereId(existing.getId())
                    .getSingleResult();
            boolean offlineMode = ConnectionService.getInstance().isOffline();
            if (!offlineMode){
                // Since the account has been updated, update the user on the server
                UserApiService userApiService = new UserApiService();
                userApiService.updateUser(existing, existing.getJWTToken());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the current account with a new theme preference.
     *
     * @param newTheme The theme to set for the current account.
     * @return True if the update was successful; false otherwise.
     */
    public boolean setCurrentTheme(Theme newTheme) {
        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setCurrentTheme(newTheme);
        return updateCurrentAccount(updateModel);
    }

    /**
     * Retrieves the currently selected layout for the current account.
     *
     * @return A list of WidgetInfo objects representing the layout.
     */
    public List<WidgetInfo> getCurrentLayout() {
        HashMap<String, WidgetInfo[]> dashboardLayouts = currentAccount.getDashboardLayouts();
        WidgetInfo[] layout = dashboardLayouts.get(currentAccount.getSelectedLayout());
        return Arrays.stream(layout).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Saves a specified layout for a user account.
     *
     * @param account    The account for which to save the layout.
     * @param layoutName The name of the layout.
     * @param value      An array of WidgetInfo objects representing the layout.
     * @return True if the layout was saved successfully; false otherwise.
     */
    public boolean saveLayoutForUser(Account account, String layoutName, WidgetInfo[] value) {
        HashMap<String, WidgetInfo[]> existingLayouts = account.getDashboardLayouts();
        existingLayouts.put(layoutName, value);

        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setId(account.getId());
        updateModel.setDashboardLayouts(existingLayouts);
        return updateAccount(updateModel);
    }

    /**
     * Saves a specified layout for the current account.
     *
     * @param layoutName The name of the layout.
     * @param value      An array of WidgetInfo objects representing the layout.
     * @return True if the layout was saved successfully; false otherwise.
     */
    public boolean saveLayout(String layoutName, WidgetInfo[] value) {
        HashMap<String, WidgetInfo[]> existingLayouts = currentAccount.getDashboardLayouts();
        existingLayouts.put(layoutName, value);

        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setDashboardLayouts(existingLayouts);
        return updateCurrentAccount(updateModel);
    }

    /**
     * Creates a new user account with the specified email, password, and default location.
     * Initializes the account with a default dashboard layout.
     *
     * @param email    The email of the new account.
     * @param password The password of the new account.
     * @param location The default location to associate with the account.
     * @throws Exception If an error occurs during account creation. (will contain a message)
     * @return The newly created Account object.
     */
    public Account createUser(String email, String username, String password, LocationCreateModel location)
        throws Exception {
        boolean offlineMode = ConnectionService.getInstance().isOffline();
        if (offlineMode){
            throw new Exception("Cannot create account in offline mode.");
        }

        Account createdAccount = createUser(email, username, password);
        LocationService.getInstance().addLocationForUser(createdAccount, location);
        List<Location> locations = LocationService.getInstance().getLocationsForUser(createdAccount);
        generateDefaultDashboard(createdAccount, locations.getFirst());

        UserApiService userApiService = new UserApiService();
        userApiService.updateUser(createdAccount, createdAccount.getJWTToken());

        Account account = new AccountDAO.AccountQuery()
            .whereEmail(email)
            .getSingleResult();

        return account;
    }

    /**
     * Creates a new user account with the specified email and password.
     *
     * @param email    The email of the new account.
     * @param password The password of the new account.
     * @throws Exception If an error occurs during account creation. (will contain a message)
     * @return The newly created Account object.
     */
    public Account createUser(String email, String username, String password) throws Exception {
        UserApiService userApiService = new UserApiService();
        boolean offlineMode = ConnectionService.getInstance().isOffline();
        if (offlineMode){
            throw new Exception("Cannot create account in offline mode.");
        }
        CreateUserDTO result = userApiService.createUser(email, password, username);

        String token = userApiService.login(email, password);

        Account createdAccount = userApiService.getCurrentAccount(token);

        AccountDAO accountDAO = new AccountDAO();
        // Check if the account already exists
        Account existingAccount = new AccountDAO.AccountQuery()
            .whereEmail(email)
            .getSingleResult();

        if (existingAccount != null) {
            Logger.printLog("Account already exists, replacing account");
            // Delete the existing account
            accountDAO.delete(existingAccount);
        }

        accountDAO.insert(createdAccount);

        return createdAccount;
    }
}
