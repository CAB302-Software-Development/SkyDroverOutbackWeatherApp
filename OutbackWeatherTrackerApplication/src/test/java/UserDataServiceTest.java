
import cab302softwaredevelopment.outbackweathertrackerapplication.models.AllUserDataModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CreateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UserDataDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UserLoginRequestDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserDataService;

import cab302softwaredevelopment.outbackweathertrackerapplication.utils.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDataServiceTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private UserDataService userDataService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userDataService = new UserDataService(httpClient);
    }

    @Test
    public void testAuthenticateUserSuccess() throws Exception {
        UserLoginRequestDTO loginRequest = new UserLoginRequestDTO();

        loginRequest.setUserName("string");
        loginRequest.setPassword("string");

        String result = userDataService.login(loginRequest);

        System.out.println(result);
    }

    @Test
    public void testAuthenticateUserFailure() throws IOException, InterruptedException {
        // Arrange
        UserLoginRequestDTO loginRequest = new UserLoginRequestDTO();
        loginRequest.setUserName("wronguser");
        loginRequest.setPassword("wrongpassword");

        // Mocking the HTTP response to simulate a failed login
        when(httpResponse.statusCode()).thenReturn(401);  // Unauthorized

        // Mocking the HttpClient to return the mocked response
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDataService.login(loginRequest);
        });

        assertEquals("Login failed: 401", exception.getMessage());
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));  // Ensure the HTTP client was called once
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        // Arrange
        CreateUserDTO newUser = new CreateUserDTO();
        newUser.setUserName("newuser");
        newUser.setUserPassword("password123");
        newUser.setUserEmail("newuser@example.com");
        newUser.setUserTheme("default");

        // Mocking the HTTP response to simulate a successful user creation
        when(httpResponse.statusCode()).thenReturn(201);  // Created

        // Mocking the HttpClient to return the mocked response
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        // Act
        userDataService.createUser(newUser);

        // Assert
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));  // Ensure the HTTP client was called once
    }

    @Test
    public void testCreateUserFailure() throws IOException, InterruptedException {
        // Arrange
        CreateUserDTO newUser = new CreateUserDTO();
        newUser.setUserName("newuser");
        newUser.setUserPassword("password123");
        newUser.setUserEmail("newuser@example.com");
        newUser.setUserTheme("default");

        // Mocking the HTTP response to simulate a failed user creation
        when(httpResponse.statusCode()).thenReturn(400);  // Bad Request

        // Mocking the HttpClient to return the mocked response
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDataService.createUser(newUser);
        });

        assertEquals("Failed to create user: 400", exception.getMessage());
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));  // Ensure the HTTP client was called once
    }

    @Test
    public void testFetchUserByIdSuccess() throws Exception {
        // Arrange
        String userId = "12345";
        UserDataDTO expectedUser = new UserDataDTO();
        expectedUser.setUserName("testuser");
        expectedUser.setUserEmail("testuser@example.com");

        // Mocking the HTTP response to simulate fetching the user by ID
        when(httpResponse.statusCode()).thenReturn(200);  // OK
        when(httpResponse.body()).thenReturn(expectedUser.toString());  // Assuming you have a toJson() method

        // Mocking the HttpClient to return the mocked response
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        // Act
        AllUserDataModel result = userDataService.getUserById(userId);

        // Assert
        assertEquals(expectedUser.getUserName(), result.getUserName());
        assertEquals(expectedUser.getUserEmail(), result.getUserEmail());
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));  // Ensure the HTTP client was called once
    }

    @Test
    public void testFetchUserByIdFailure() throws IOException, InterruptedException {
        // Arrange
        String userId = "nonexistent";

        // Mocking the HTTP response to simulate a failed user fetch
        when(httpResponse.statusCode()).thenReturn(404);  // Not Found

        // Mocking the HttpClient to return the mocked response
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDataService.getUserById(userId);
        });

        assertEquals("User not found: 404", exception.getMessage());
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));  // Ensure the HTTP client was called once
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDataService.getAllUsers();
        });

        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));  // Ensure the HTTP client was called once
    }
}