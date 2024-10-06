package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import lombok.Getter;

import java.util.UUID;

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

    public static void updateAccount(Account newAccount) {
        currentAccount.setEmail(newAccount.getEmail());
        currentAccount.setPassword(newAccount.getPassword());
        currentAccount.setCurrentTheme(newAccount.getCurrentTheme());
        currentAccount.setIsGuest(newAccount.getIsGuest());
        currentAccount.setPreferCelsius(newAccount.getPreferCelsius());

        AccountDAO accountDAO = new AccountDAO();
        accountDAO.update(currentAccount);
    }
}
