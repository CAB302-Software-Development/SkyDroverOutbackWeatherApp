package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import lombok.Getter;
import lombok.Setter;

public class LoginState {
    @Getter @Setter
    private static Account currentAccount;

    public static void login(Account account) {
        currentAccount = account;
    }

    public static boolean isGuest() {
        return currentAccount.getIsGuest();
    }

    public static void logout() {
        Account guestAccount = UserService.getGuestAccount();
        currentAccount = guestAccount;
    }
}
