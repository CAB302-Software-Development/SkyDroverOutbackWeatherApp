package cab302softwaredevelopment.outbackweathertrackerapplication.services;


import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.converters.WidgetInfoListConverter;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.UserModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CreateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UpdateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UserLoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserApiService {
    private static final String BASE_URL = "http://localhost:8090/api/user";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public UserApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieve all users' data.
     *
     * @return List of UserDataDTO
     * @throws Exception If an error occurs (will contain the error message)
     */
    public List<UserDTO> getAllUsers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/getAllUsers"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class));
        } else {
            throw new RuntimeException("Error fetching all users: " + response.body());
        }
    }

    /**
     * Retrieve user data by username.
     *
     * @param username the username to fetch
     * @return AllUserDataModel
     * @throws Exception If an error occurs (will contain the error message)
     */
    public UserModel getUserByUsername(String username) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/getUserByUsername/" + username))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserModel.class);
        } else {
            throw new RuntimeException("Error fetching user: " + response.body());
        }
    }

    public UserModel getUserById(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/getUserById/" + id))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserModel.class);
        } else {
            throw new RuntimeException("Error fetching user: " + response.body());
        }
    }

    /**
     * Create a new user.
     *
     * @param userDTO the user data to create
     * @return CreateUserDTO
     * @throws Exception If an error occurs (will contain the error message)
     */
    public CreateUserDTO createUser(CreateUserDTO userDTO) throws Exception {
        String requestBody = objectMapper.writeValueAsString(userDTO);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/createUser"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), CreateUserDTO.class);
        } else if (response.statusCode() == 400) {
            throw new RuntimeException(response.body());
        }
        else {
            throw new RuntimeException("Error creating user: " + response.body());
        }
    }

    /**
     * Creates a default user with the given email, password, and username.
     * @param email the user's email
     * @param password the user's password
     * @param username the user's username
     * @return CreateUserDTO
     * @throws Exception if the user cannot be created
     */
    public CreateUserDTO createUser(String email, String password, String username) throws Exception {
        // To pull default values from the account class, we need to create a new account object
        Account.AccountBuilder accountBuilder = Account.builder()
            .email(email)
            .password(password)
            .isGuest(false);

        Account newAccount = accountBuilder.build();
        UserApiService userApiService = new UserApiService();
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setUsername(username);
        userDTO.setUserEmail(email);
        userDTO.setUserPassword(newAccount.getPassword());
        userDTO.setUserTheme(newAccount.getCurrentTheme().toString());
        userDTO.setPreferCelsius(newAccount.getPreferCelsius());
        userDTO.setSelectedLayout(newAccount.getSelectedLayout());
        userDTO.setCustomAlertConditions(newAccount.GetCustomAlertConditionsString());
        userDTO.setDashboardLayout(newAccount.GetDashboardLayoutsString());
        userDTO.setLocations("{}"); // No locations for now

        return userApiService.createUser(userDTO);
    }

    /**
     * Log in a user and get a JWT token.
     *
     * @param loginRequest the login request containing username and password
     * @return JWT token
     * @throws Exception If an error occurs (will contain the error message)
     */
    public String login(UserLoginRequestDTO loginRequest) throws Exception {
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Map<String, String> tokenResponse = objectMapper.readValue(response.body(), Map.class);
            return tokenResponse.get("jwt_token");
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Invalid email or password");
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("User not found");
        }
        else {
            throw new RuntimeException("Login failed: " + response.body());
        }
    }

    /**
     * Log in a user using email and password.
     *
     * @param email the user's email
     * @param password the user's password
     * @return JWT token
     * @throws Exception If an error occurs (will contain the error message)
     */
    public String login(String email, String password) throws Exception {
        // To pull default values from the account class, we need to create a new account object
        Account.AccountBuilder accountBuilder = Account.builder()
            .email(email)
            .password(password)
            .isGuest(false);
        Account newAccount = accountBuilder.build();

        UserLoginRequestDTO loginRequest = new UserLoginRequestDTO();
        loginRequest.setUserEmail(email);
        loginRequest.setPassword(newAccount.getPassword());

        return login(loginRequest);
    }

    /**
     * Delete a user by ID.
     *
     * @param userId the user ID to delete
     * @param jwtToken the JWT token for authorization
     * @return true if successful, false otherwise
     * @throws Exception If an error occurs (will contain the error message)
     */
    public boolean deleteUser(String userId, String jwtToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/deleteUser"))
            .header("Authorization", "Bearer " + jwtToken)
            .header("Content-Type", "application/json")
            .method("DELETE", HttpRequest.BodyPublishers.ofString(userId))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            throw new RuntimeException("Error deleting user: " + response.body());
        }
    }

    /**
     * Update a user.
     *
     * @param userId the user ID to update
     * @param userData the updated user data
     * @param jwtToken the JWT token for authorization
     * @return CreateUserDTO
     * @throws Exception If an error occurs (will contain the error message)
     */
    public CreateUserDTO updateUser(String userId, UpdateUserDTO userData, String jwtToken) throws Exception {
        String requestBody = objectMapper.writeValueAsString(userData);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/updateUser/" + userId))
            .header("Authorization", "Bearer " + jwtToken)
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), CreateUserDTO.class);
        } else {
            throw new RuntimeException("Error updating user: " + response.body());
        }
    }

    public CreateUserDTO updateUser(Account account, String jwtToken) throws Exception {
        List<Location> locations = LocationService.getInstance().getLocationsForUser(account);
        UserApiService userApiService = new UserApiService();
        UpdateUserDTO userDTO = new UpdateUserDTO();
        userDTO.setUsername(account.getUsername());
        userDTO.setUserEmail(account.getEmail());
        userDTO.setUserPassword(account.getPassword());
        userDTO.setPreferCelsius(account.getPreferCelsius());
        userDTO.setUserTheme(account.getCurrentTheme().toString());
        userDTO.setSelectedLayout(account.getSelectedLayout());
        userDTO.setDashboardLayout(account.GetDashboardLayoutsString());
        userDTO.setCustomAlertConditions(account.GetCustomAlertConditionsString());
        userDTO.setLocations(new Gson().toJson(locations)); // Locations are stored as a JSON string

        // Update the account modified date
        AccountDAO accountDAO = new AccountDAO();
        account.setLastModified(new Date(System.currentTimeMillis()));

        // Update the user
        CreateUserDTO result = userApiService.updateUser(account.getId(), userDTO, jwtToken);

        accountDAO.update(account);
        return result;
    }

    /**
     * Get the current user data using a JWT token.
     *
     * @param jwtToken the JWT token
     * @return AllUserDataModel
     * @throws Exception If an error occurs (will contain the error message)
     */
    public UserModel getCurrentUser(String jwtToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/getUser"))
            .header("Authorization", "Bearer " + jwtToken)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserModel.class);
        } else {
            throw new RuntimeException("Error fetching current user: " + response.body());
        }
    }

    public Account getCurrentAccount(UserModel userModel, String jwtToken) throws Exception {
        Account createdAccount = Account.builder()
            .id(userModel.getId())
            .username(userModel.getUsername())
            .password(userModel.getUserPassword()) // Need to replace with a non-hashed password since the userModel is already hashed
            .email(userModel.getUserEmail())
            .currentTheme(Theme.valueOf(userModel.getUserTheme()))
            .isGuest(false)
            .selectedLayout(userModel.getSelectedLayout())
            // Convert the JSON string back to a Layouts object
            .dashboardLayouts(new WidgetInfoListConverter().convertToEntityAttribute(userModel.getDashboardLayout()))
            .preferCelsius(userModel.getPreferCelsius())
            .JWTToken(jwtToken)
            .build();
        createdAccount.setPassword(userModel.getUserPassword(), false);
        // Since an account was created, we need to update the last modified date
        createdAccount.setLastModified(userModel.getUserAccountUpdateDate());
        return createdAccount;
    }

    public Account getCurrentAccount(String jwtToken) throws Exception {
        UserModel userModel = getCurrentUser(jwtToken);
        return getCurrentAccount(userModel, jwtToken);
    }

}