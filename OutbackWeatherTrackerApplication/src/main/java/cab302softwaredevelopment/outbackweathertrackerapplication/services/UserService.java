package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AccountUpdateModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
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

    public void logout() {
        Account guestAccount = getGuestAccount();
        currentAccount = guestAccount;
    }

    public Account getGuestAccount() {
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

        Account.AccountBuilder accountBuilder = Account.builder();
        accountBuilder.id(existing.getId());
        accountBuilder.isGuest(existing.getIsGuest());

        if (newAccount.getEmail() != null) {
            accountBuilder.email(newAccount.getEmail());
        } else {
            accountBuilder.email(existing.getEmail());
        }

        if (newAccount.getPassword() != null) {
            accountBuilder.password(newAccount.getPassword());
        } else {
            accountBuilder.password(existing.getPassword());
        }

        if (newAccount.getCurrentTheme() != null) {
            accountBuilder.currentTheme(newAccount.getCurrentTheme());
        } else {
            accountBuilder.currentTheme(existing.getCurrentTheme());
        }

        if (newAccount.getPreferCelsius() != null) {
            accountBuilder.preferCelsius(newAccount.getPreferCelsius());
        } else {
            accountBuilder.preferCelsius(existing.getPreferCelsius());
        }

        if (newAccount.getDashboardLayouts() != null) {
            accountBuilder.dashboardLayouts(newAccount.getDashboardLayouts());
        } else {
            accountBuilder.dashboardLayouts(existing.getDashboardLayouts());
        }

        if (newAccount.getSelectedLayout() != null) {
            accountBuilder.selectedLayout(newAccount.getSelectedLayout());
        } else {
            accountBuilder.selectedLayout(existing.getSelectedLayout());
        }

        Account updatedAccount = accountBuilder.build();

        try {
            AccountDAO accountDAO = new AccountDAO();
            accountDAO.update(updatedAccount);
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

    public boolean saveLayout(String layoutName, WidgetInfo[] value) {
        HashMap<String, WidgetInfo[]> existingLayouts = currentAccount.getDashboardLayouts();
        existingLayouts.put(layoutName, value);

        AccountUpdateModel updateModel = new AccountUpdateModel();
        updateModel.setDashboardLayouts(existingLayouts);
        return updateCurrentAccount(updateModel);
    }
}
