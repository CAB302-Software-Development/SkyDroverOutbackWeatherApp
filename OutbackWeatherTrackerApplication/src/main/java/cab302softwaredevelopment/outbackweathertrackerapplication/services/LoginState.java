package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;

public class LoginState {
    private static Account currentAccount;
    private static boolean isGuest;

    public static void login(Account account) {
        currentAccount = account;
        isGuest = false;
    }

    public static void logout() {
        currentAccount = null;
        isGuest = true;
    }

    public static boolean isGuest() {
        return isGuest;
    }
}
