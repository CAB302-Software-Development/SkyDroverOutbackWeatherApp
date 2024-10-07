package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import lombok.Getter;

public class LoginState {
    @Getter
    private static Account currentAccount;

    public static void login(Account account) {
        currentAccount = account;
    }

    public static boolean isGuest() {
        return currentAccount.getIsGuest();
    }

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

    public static void updateAccount(AccountUpdateModel newAccount) {
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

        AccountDAO accountDAO = new AccountDAO();
        accountDAO.update(updatedAccount);
        currentAccount = new AccountDAO.AccountQuery()
                .whereId(currentAccount.getId())
                .getSingleResult();

    }
}
