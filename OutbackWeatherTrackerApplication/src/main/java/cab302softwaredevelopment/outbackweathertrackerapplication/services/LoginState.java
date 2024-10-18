package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 * A class for managing the login state of the application.
 */
public class LoginState {
    @Getter
    private static Account currentAccount;
    @Getter
    private static boolean isOffline;

    /**
     * Logs in the specified account.
     *
     * @param account The account to log in.
     */
    public static void login(Account account) {
        currentAccount = account;
    }

    /**
     * Returns whether the current account is a guest account.
     *
     * @return Whether the current account is a guest account.
     */
    public static boolean isGuest() {
        return currentAccount.getIsGuest();
    }

    /**
     * Logs out the current account.
     */
    public static void logout() {
        Account guestAccount = new AccountDAO.AccountQuery().whereEmail("guest@guest.com").getSingleResult();
        if(guestAccount == null) {
            Account newGuestAccount = Account.builder()
                    .email("guest@guest.com")
                    .password("SecurePass1!")
                    .isGuest(true)
                    .build();

            AccountDAO accountDAO = new AccountDAO();
            accountDAO.insert(newGuestAccount);
            guestAccount = new AccountDAO.AccountQuery().whereEmail("guest@guest.com").getSingleResult();
        }

        LocationDAO locationDAO = new LocationDAO();
        if ((new LocationDAO.LocationQuery()).whereAccount(guestAccount).getSingleResult() == null) {
            locationDAO.insert(new Location(guestAccount, 153.02333324, -27.467331464, 27.0, "Brisbane"));
        }

        currentAccount = guestAccount;
    }

    /**
     * Updates the current account with the specified account.
     *
     * @param newAccount The account to update the current account with.
     * @return Whether the account was updated successfully.
     */
    public static boolean updateAccount(AccountUpdateModel newAccount) {
        Account.AccountBuilder accountBuilder = Account.builder();
        accountBuilder.id(currentAccount.getId());
        accountBuilder.isGuest(currentAccount.getIsGuest());

        if (newAccount.getEmail() != null) {
            accountBuilder.email(newAccount.getEmail());
        } else {
            accountBuilder.email(currentAccount.getEmail());
        }

        if (newAccount.getPassword() != null) {
            accountBuilder.password(newAccount.getPassword());
        } else {
            accountBuilder.password(currentAccount.getPassword());
        }

        if (newAccount.getCurrentTheme() != null) {
            accountBuilder.currentTheme(newAccount.getCurrentTheme());
        } else {
            accountBuilder.currentTheme(currentAccount.getCurrentTheme());
        }

        if (newAccount.getPreferCelsius() != null) {
            accountBuilder.preferCelsius(newAccount.getPreferCelsius());
        } else {
            accountBuilder.preferCelsius(currentAccount.getPreferCelsius());
        }

        if (newAccount.getDashboardLayouts() != null) {
            accountBuilder.dashboardLayouts(newAccount.getDashboardLayouts());
        } else {
            accountBuilder.dashboardLayouts(currentAccount.getDashboardLayouts());
        }

        if (newAccount.getSelectedLayout() != null) {
            accountBuilder.selectedLayout(newAccount.getSelectedLayout());
        } else {
            accountBuilder.selectedLayout(currentAccount.getSelectedLayout());
        }

        Account updatedAccount = accountBuilder.build();

        try {
            AccountDAO accountDAO = new AccountDAO();
            accountDAO.update(updatedAccount);
            currentAccount = new AccountDAO.AccountQuery()
                    .whereId(currentAccount.getId())
                    .getSingleResult();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets the offline status of the application.
     *
     * @param value The offline status to set.
     */
    public static void setOffline(boolean value) {
        // TODO display toast and other indicators of offline status
        isOffline = value;
    }
}
