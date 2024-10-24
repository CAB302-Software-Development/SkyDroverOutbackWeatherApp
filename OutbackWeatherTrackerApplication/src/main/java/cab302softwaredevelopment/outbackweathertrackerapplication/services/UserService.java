package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class UserService {
    @Getter
    private static UserService instance = new UserService();

    @Getter @Setter
    private Account currentAccount;

    public void login(Account account) {
        currentAccount = account;
    }

    public boolean isGuest() {
        return currentAccount.getIsGuest();
    }

    public void logout(LocationCreateModel defaultLocation) {
        Account guestAccount = getGuestAccount(defaultLocation);
        currentAccount = guestAccount;
    }

    public Account getGuestAccount(LocationCreateModel defaultLocation) {
        try {
            Account guestAccount = new AccountDAO.AccountQuery().whereEmail("guest@guest.com").getSingleResult();
            new AccountDAO().delete(guestAccount.getId());
            LocationService.getInstance().deleteLocationsForUser(guestAccount);

            Account newGuestAccount = Account.builder()
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

    private void generateDefaultDashboard(Account account, Location location) {
        WidgetConfig widgetConfig = new WidgetConfig();
        widgetConfig.setLocationId(location.getId());

        WidgetInfo[] defaultDashboard = new WidgetInfo[]{
                new WidgetInfo(WidgetType.Temperature, 0, 0, 2, 2, widgetConfig.getConfig()),
                new WidgetInfo(WidgetType.Soil, 0, 2, 2, 2, widgetConfig.getConfig()),
                new WidgetInfo(WidgetType.Precipitation, 2, 0, 2, 1, widgetConfig.getConfig())
        };

        UserService.getInstance().saveLayoutForUser(account, "default", defaultDashboard);
    }

    public Account getByID(UUID id) {
        return new AccountDAO.AccountQuery().whereId(id).getSingleResult();
    }

    public boolean updateCurrentAccount(AccountUpdateModel newAccount) {
        newAccount.setId(getCurrentAccount().getId());
        boolean result = updateAccount(newAccount);
        if (result) {
            Account updatedAccount = getByID(newAccount.getId());
            setCurrentAccount(updatedAccount);
        }
        return result;
    }

    public boolean updateAccount(AccountUpdateModel newAccount) {
        Account existing = getByID(newAccount.getId());

        if (newAccount.getEmail() != null) {
            existing.setEmail(newAccount.getEmail());
        }

        if (newAccount.getCurrentTheme() != null) {
            existing.setCurrentTheme(newAccount.getCurrentTheme());
        }

        if (newAccount.getPreferCelsius() != null) {
            existing.setPreferCelsius(newAccount.getPreferCelsius());
        }

        if (newAccount.getDashboardLayouts() != null) {
            existing.setDashboardLayouts(newAccount.getDashboardLayouts());
        }

        if (newAccount.getSelectedLayout() != null) {
            existing.setSelectedLayout(newAccount.getSelectedLayout());
        }

        if (newAccount.getPassword() != null) {
            existing.setPassword(newAccount.getPassword());
        }

        try {
            AccountDAO accountDAO = new AccountDAO();
            accountDAO.update(existing);
            existing = new AccountDAO.AccountQuery()
                    .whereId(existing.getId())
                    .getSingleResult();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setCurrentTheme(Theme newTheme) {
        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setCurrentTheme(newTheme);
        return updateCurrentAccount(updateModel);
    }

    public List<WidgetInfo> getCurrentLayout() {
        HashMap<String, WidgetInfo[]> dashboardLayouts = currentAccount.getDashboardLayouts();
        WidgetInfo[] layout = dashboardLayouts.get(currentAccount.getSelectedLayout());
        return Arrays.stream(layout).collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean saveLayoutForUser(Account account, String layoutName, WidgetInfo[] value) {
        HashMap<String, WidgetInfo[]> existingLayouts = account.getDashboardLayouts();
        existingLayouts.put(layoutName, value);

        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setId(account.getId());
        updateModel.setDashboardLayouts(existingLayouts);
        return updateAccount(updateModel);
    }

    public boolean saveLayout(String layoutName, WidgetInfo[] value) {
        HashMap<String, WidgetInfo[]> existingLayouts = currentAccount.getDashboardLayouts();
        existingLayouts.put(layoutName, value);

        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setDashboardLayouts(existingLayouts);
        return updateCurrentAccount(updateModel);
    }

    public Account createUser(String email, String password, LocationCreateModel location) {
        Account createdAccount = createUser(email, password);
        LocationService.getInstance().addLocationForUser(createdAccount, location);
        List<Location> locations = LocationService.getInstance().getLocationsForUser(createdAccount);
        generateDefaultDashboard(createdAccount, locations.getFirst());

        List<Account> accounts = (new AccountDAO.AccountQuery())
                .whereEmail(email)
                .getResults();
        return accounts.getFirst();
    }

    public Account createUser(String email, String password) {
        AccountDAO accountDAO = new AccountDAO();

        Account newAccount = Account.builder()
                .email(email)
                .password(password)
                .build();

        boolean temp = newAccount.verifyPassword(password);

        accountDAO.insert(newAccount);

        Account createdAccount = (new AccountDAO.AccountQuery())
                .whereEmail(email)
                .getSingleResult();

        return createdAccount;
    }
}
