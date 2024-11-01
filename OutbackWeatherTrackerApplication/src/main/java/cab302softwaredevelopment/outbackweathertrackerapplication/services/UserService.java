package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.LocationDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.*;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CreateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UpdateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UserLoginRequestDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.utils.WidgetConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import org.json.JSONObject;

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

    public Account getByID(String id) {
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


        Account account = new AccountDAO.AccountQuery()
                .whereEmail(email)
                .getSingleResult();

        UserApiService userApiService = new UserApiService();
        UpdateUserDTO userDTO = new UpdateUserDTO();
        userDTO.setUserName(createdAccount.getUsername());
        userDTO.setUserPassword(createdAccount.getPassword());
        userDTO.setUserEmail(createdAccount.getEmail());
        userDTO.setUserTheme(createdAccount.getCurrentTheme().toString());
        userDTO.setPreferCelsius(createdAccount.getPreferCelsius());
        userDTO.setSelectedLayout(createdAccount.getSelectedLayout());
        userDTO.setDashboardLayout(createdAccount.GetDashboardLayoutsString());
        userDTO.setLocations(new Gson().toJson(locations)); // Locations are stored as a JSON string

        CreateUserDTO result;
        try {
            result = userApiService.updateUser(createdAccount.getId(), userDTO, createdAccount.getJWTToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    public Account createUser(String email, String password) {
        String username = "TEST USERNAME"; // TODO DEBUG
        Account.AccountBuilder accountBuilder = Account.builder()
            .email(email)
            .password(password)
            .isGuest(false);

        Account newAccount = accountBuilder.build();
        UserApiService userApiService = new UserApiService();
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setUserName(username); // TODO DEBUG
        userDTO.setUserEmail(email);
        userDTO.setUserPassword(newAccount.getPassword());
        userDTO.setUserTheme(newAccount.getCurrentTheme().toString());
        userDTO.setPreferCelsius(newAccount.getPreferCelsius());
        userDTO.setSelectedLayout(newAccount.getSelectedLayout());
        String dashboardLayout = newAccount.GetDashboardLayoutsString();
        userDTO.setDashboardLayout(dashboardLayout);
        userDTO.setLocations("{}"); // No locations for now

        CreateUserDTO result;
        try {
            result = userApiService.createUser(userDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserLoginRequestDTO loginRequest = new UserLoginRequestDTO();
        loginRequest.setUserEmail(email);
        loginRequest.setPassword(newAccount.getPassword());

        String token;
        try {
            token = userApiService.login(loginRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Get the account
        UserModel userModel;
        try {
            userModel = userApiService.getCurrentUser(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, WidgetInfo[]>>() {}.getType();

        Account createdAccount = Account.builder()
            .id(userModel.getId())
            .username(userModel.getUserName())
            .password(password)
            .email(userModel.getUserEmail())
            .currentTheme(Theme.valueOf(userModel.getUserTheme()))
            .isGuest(false)
            .selectedLayout(userModel.getSelectedLayout())
            // Convert the JSON string back to a Layouts object
            .dashboardLayouts(gson.fromJson(userModel.getDashboardLayout(), type))
            .preferCelsius(userModel.getPreferCelsius())
            .JWTToken(token)
            .build();

        AccountDAO accountDAO = new AccountDAO();

        accountDAO.insert(createdAccount);

        boolean temp = newAccount.verifyPassword(password);
        assert temp;
        return createdAccount;
    }
}
