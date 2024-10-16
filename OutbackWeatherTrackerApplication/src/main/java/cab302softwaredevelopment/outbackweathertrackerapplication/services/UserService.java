package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;

import java.util.UUID;

public class UserService {
    public static Account getGuestAccount() {
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
        return guestAccount;
    }

    public static Account getByID(UUID id) {
        return new AccountDAO.AccountQuery().whereId(id).getSingleResult();
    }

    public static boolean updateCurrentAccount(AccountUpdateModel newAccount) {
        newAccount.setId(LoginState.getCurrentAccount().getId());
        boolean result = updateAccount(newAccount);
        if (result) {
            Account updatedAccount = getByID(newAccount.getId());
            LoginState.setCurrentAccount(updatedAccount);
        }
        return result;
    }

    public static boolean updateAccount(AccountUpdateModel newAccount) {
        Account currentAccount = getByID(newAccount.getId());

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
}
