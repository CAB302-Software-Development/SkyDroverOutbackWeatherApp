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

public class LoginState {
    @Getter
    private static Account currentAccount;
    @Getter
    private static boolean isOffline;

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

    public static void setOffline(boolean value) {
        // TODO display toast and other indicators of offline status
        isOffline = value;
    }
}
